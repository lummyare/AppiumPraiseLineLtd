#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────────
# run_subaru.sh — Subaru JUnit 5 test runner
#
# Usage:
#   ./run_subaru.sh [platform] [region] [target] [tag-expression] [--simulator|--cloud] [--dry-run]
#
#   platform (optional, default: ios)
#     ios | android
#
#   region (optional, default: usa)
#     usa      -> US tests
#     ca-fr    -> Canada French tests
#
#   target (optional, default: smoke)
#     smoke    -> smoke class for selected platform/region
#     all      -> all Subaru classes for selected platform/region
#     <area>   -> functional area directory (dashboard, remote, accountSettings, ...)
#     tag      -> run all classes, filtered by JUnit @Tag expression (next arg required)
#
# Modes:
#   --simulator  Run iOS on local Appium + local iOS Simulator (default for iOS)
#   --cloud      Run iOS using existing cloud path (optional fallback)
#
# Examples:
#   ./run_subaru.sh
#   ./run_subaru.sh ios usa smoke
#   ./run_subaru.sh ios usa tag "RemoteCommands and not GuestDriver"
#   ./run_subaru.sh ios usa smoke --simulator
#   ./run_subaru.sh ios usa all --cloud
#   ./run_subaru.sh android usa all
#
# Notes:
# - Subaru tests are JUnit 5 tests under src/test/java/v2update/subaru/**
# - This runner disables TestNG suite used by run.sh via -DsuiteXmlFile=
# - Tag filtering uses Maven Surefire `groups` (maps to JUnit 5 tags)
# ─────────────────────────────────────────────────────────────────────────────

set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJECT_ROOT"

PLATFORM="ios"
REGION="usa"
TARGET="smoke"
TAG_EXPR=""
DRY_RUN=false
IOS_MODE="simulator"   # default requested behavior for now

# Local simulator defaults (can be overridden via env)
APPIUM_PORT="${APPIUM_PORT:-4723}"
LOCAL_PROFILE="${SUBARU_LOCAL_PROFILE:-subarustage}"
SIM_PLATFORM_NAME="${IOS_SIM_PLATFORM_NAME:-iOS}"
SIM_AUTOMATION_NAME="${IOS_SIM_AUTOMATION_NAME:-XCUITest}"
SIM_DEVICE_NAME="${IOS_SIM_DEVICE_NAME:-}"
SIM_PLATFORM_VERSION="${IOS_SIM_PLATFORM_VERSION:-}"
SIM_UDID="${IOS_SIM_UDID:-}"
SIM_BUNDLE_ID="${IOS_SIM_BUNDLE_ID:-com.subaru.oneapp.stg}"
SIM_APP_PATH="${IOS_SIM_APP_PATH:-}"

usage() {
  cat <<'EOF'
Usage:
  ./run_subaru.sh [platform] [region] [target] [tag-expression] [--simulator|--cloud] [--dry-run]
  ./run_subaru.sh [--platform <ios|android>] [--region <usa|ca-fr>] [--suite <target>] [--tags <expr>]

Examples:
  ./run_subaru.sh smoke
  ./run_subaru.sh --platform ios --region usa --suite smoke
  ./run_subaru.sh --platform android --region ca-fr --suite all
  ./run_subaru.sh --platform ios --region usa --suite tag --tags "RemoteCommands and not GuestDriver"
EOF
}

# parse args and strip flags from positional processing
ARGS=()
while [[ $# -gt 0 ]]; do
  case "$1" in
    --dry-run)
      DRY_RUN=true
      shift
      ;;
    --simulator)
      IOS_MODE="simulator"
      shift
      ;;
    --cloud)
      IOS_MODE="cloud"
      shift
      ;;
    --platform)
      [[ $# -ge 2 ]] || { echo "✗ --platform requires a value"; exit 1; }
      PLATFORM="$2"
      shift 2
      ;;
    --platform=*)
      PLATFORM="${1#*=}"
      shift
      ;;
    --region)
      [[ $# -ge 2 ]] || { echo "✗ --region requires a value"; exit 1; }
      REGION="$2"
      shift 2
      ;;
    --region=*)
      REGION="${1#*=}"
      shift
      ;;
    --suite|--target)
      [[ $# -ge 2 ]] || { echo "✗ $1 requires a value"; exit 1; }
      TARGET="$2"
      shift 2
      ;;
    --suite=*|--target=*)
      TARGET="${1#*=}"
      shift
      ;;
    --tags|--tag-expression)
      [[ $# -ge 2 ]] || { echo "✗ $1 requires a value"; exit 1; }
      TAG_EXPR="$2"
      shift 2
      ;;
    --tags=*|--tag-expression=*)
      TAG_EXPR="${1#*=}"
      shift
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    --)
      shift
      while [[ $# -gt 0 ]]; do
        ARGS+=("$1")
        shift
      done
      ;;
    -*)
      echo "✗ Unknown option: $1"
      usage
      exit 1
      ;;
    *)
      ARGS+=("$1")
      shift
      ;;
  esac
done

# Positional compatibility:
#   ./run_subaru.sh smoke
#   ./run_subaru.sh ios smoke
#   ./run_subaru.sh ios usa tag "Dashboard"
POS_INDEX=0
if [[ ${#ARGS[@]} -gt $POS_INDEX ]]; then
  case "${ARGS[$POS_INDEX]}" in
    ios|android)
      PLATFORM="${ARGS[$POS_INDEX]}"
      ((POS_INDEX+=1))
      ;;
  esac
fi

if [[ ${#ARGS[@]} -gt $POS_INDEX ]]; then
  case "${ARGS[$POS_INDEX]}" in
    usa|ca-fr)
      REGION="${ARGS[$POS_INDEX]}"
      ((POS_INDEX+=1))
      ;;
  esac
fi

if [[ ${#ARGS[@]} -gt $POS_INDEX ]]; then
  TARGET="${ARGS[$POS_INDEX]}"
  ((POS_INDEX+=1))
fi

if [[ -z "$TAG_EXPR" && ${#ARGS[@]} -gt $POS_INDEX ]]; then
  TAG_EXPR="${ARGS[$POS_INDEX]}"
fi

case "$PLATFORM" in
  ios|android) ;;
  *)
    echo "✗ Invalid platform '$PLATFORM' (expected ios|android)"
    exit 1
    ;;
esac

case "$REGION" in
  usa|ca-fr) ;;
  *)
    echo "✗ Invalid region '$REGION' (expected usa|ca-fr)"
    exit 1
    ;;
esac

if [[ "$TARGET" == "tag" ]]; then
  if [[ -z "$TAG_EXPR" ]]; then
    echo "✗ tag mode requires a tag expression"
    echo "  Example: ./run_subaru.sh ios usa tag \"RemoteCommands and not GuestDriver\""
    exit 1
  fi
  TARGET="all"
fi
resolve_root_dir() {
  if [[ "$PLATFORM" == "ios" && "$REGION" == "usa" ]]; then
    echo "src/test/java/v2update/subaru/ios/usa"
  elif [[ "$PLATFORM" == "ios" && "$REGION" == "ca-fr" ]]; then
    echo "src/test/java/v2update/subaru/ios/canada/french"
  elif [[ "$PLATFORM" == "android" && "$REGION" == "usa" ]]; then
    echo "src/test/java/v2update/subaru/android/usa"
  elif [[ "$PLATFORM" == "android" && "$REGION" == "ca-fr" ]]; then
    echo "src/test/java/v2update/subaru/android/canada/French"
  else
    echo ""
  fi
}

ROOT_DIR="$(resolve_root_dir)"
if [[ -z "$ROOT_DIR" || ! -d "$ROOT_DIR" ]]; then
  echo "✗ Could not resolve Subaru test directory for platform=$PLATFORM region=$REGION"
  exit 1
fi

resolve_search_dir() {
  local target="$1"
  case "$target" in
    smoke) echo "$ROOT_DIR/smoke" ;;
    all) echo "$ROOT_DIR" ;;
    *)
      if [[ -d "$ROOT_DIR/$target" ]]; then
        echo "$ROOT_DIR/$target"
      else
        echo ""
      fi
      ;;
  esac
}

SEARCH_DIR="$(resolve_search_dir "$TARGET")"
if [[ -z "$SEARCH_DIR" || ! -d "$SEARCH_DIR" ]]; then
  echo "✗ Unknown target '$TARGET' for $PLATFORM/$REGION"
  echo "  Try: smoke | all | accountSettings | dashboard | remote | find | health | vehicleInfo | vehicles | ..."
  exit 1
fi

build_test_selector() {
  local search_dir="$1"
  local classes=()

  while IFS= read -r file; do
    # keep only files that actually declare JUnit @Test methods
    if grep -q "@Test" "$file"; then
      class_name="${file#src/test/java/}"
      class_name="${class_name%.java}"
      class_name="${class_name//\//.}"
      classes+=("$class_name")
    fi
  done < <(find "$search_dir" -type f -name "*.java" ! -name "package-info.java" | sort)

  if [[ ${#classes[@]} -eq 0 ]]; then
    echo ""
    return
  fi

  local joined
  joined=$(IFS=, ; echo "${classes[*]}")
  echo "$joined"
}

first_booted_ios_simulator() {
  if ! command -v xcrun >/dev/null 2>&1; then
    return 1
  fi

  xcrun simctl list devices 2>/dev/null | awk '
    /^-- iOS / { ver=$3; next }
    /\(Booted\)/ && $0 !~ /unavailable/ {
      line=$0
      sub(/^ +/, "", line)
      udid=""
      if (match(line, /\([0-9A-F-][0-9A-F-]*\)/)) {
        udid=substr(line, RSTART+1, RLENGTH-2)
      }
      if (udid != "") {
        name=line
        sub(/ \([0-9A-F-][0-9A-F-]*\) \(Booted\).*/, "", name)
        print udid "|" name "|" ver
        exit
      }
    }
  '
}

ensure_testautomation_jar() {
  local jar_path="libs/TestAutomation-1.3.29-all.jar"

  if [[ ! -f "$jar_path" ]]; then
    echo "✗ Missing required dependency: $jar_path"
    echo "  Run: git lfs pull --include=\"$jar_path\""
    exit 1
  fi

  local size_bytes
  size_bytes=$(wc -c < "$jar_path" | tr -d ' ')

  if grep -aq '^version https://git-lfs.github.com/spec/v1' "$jar_path"; then
    echo "⚠ Detected Git LFS pointer for $jar_path (size: ${size_bytes} bytes)."
    echo "  Attempting to fetch the real JAR via Git LFS..."

    if command -v git >/dev/null 2>&1 && git lfs version >/dev/null 2>&1; then
      git lfs pull --include="$jar_path" || true
    fi

    if grep -aq '^version https://git-lfs.github.com/spec/v1' "$jar_path"; then
      echo "✗ Git LFS content is still not available for $jar_path"
      echo "  Fix it with:"
      echo "    1) git lfs install"
      echo "    2) git lfs pull --include=\"$jar_path\""
      echo "  Then rerun this script."
      exit 1
    fi
  fi

  size_bytes=$(wc -c < "$jar_path" | tr -d ' ')
  if [[ "$size_bytes" -lt 10240 ]]; then
    echo "✗ $jar_path looks too small (${size_bytes} bytes)."
    echo "  This is likely not a valid JAR. Re-run: git lfs pull --include=\"$jar_path\""
    exit 1
  fi
}

ensure_testautomation_jar
TEST_SELECTOR="$(build_test_selector "$SEARCH_DIR")"
if [[ -z "$TEST_SELECTOR" ]]; then
  echo "✗ No runnable test classes found under $SEARCH_DIR"
  exit 1
fi

MAVEN_CMD=(
  mvn clean test
  -DsuiteXmlFile=
  -Dplatform="$PLATFORM"
  -Dtest="$TEST_SELECTOR"
)

if [[ -n "$TAG_EXPR" ]]; then
  MAVEN_CMD+=( -Dgroups="$TAG_EXPR" )
fi

if [[ "$PLATFORM" == "ios" && "$IOS_MODE" == "simulator" ]]; then
  # Auto-detect already-booted simulator unless explicit IOS_SIM_UDID was provided
  if [[ -z "$SIM_UDID" ]]; then
    BOOTED_INFO="$(first_booted_ios_simulator || true)"
    if [[ -n "$BOOTED_INFO" ]]; then
      SIM_UDID="${BOOTED_INFO%%|*}"
      REST="${BOOTED_INFO#*|}"
      BOOTED_NAME="${REST%%|*}"
      BOOTED_VERSION="${REST##*|}"

      if [[ -z "$SIM_DEVICE_NAME" ]]; then
        SIM_DEVICE_NAME="$BOOTED_NAME"
      fi
      if [[ -z "$SIM_PLATFORM_VERSION" ]]; then
        SIM_PLATFORM_VERSION="$BOOTED_VERSION"
      fi
    fi
  fi

  MAVEN_CMD+=(
    -Dlocal="$LOCAL_PROFILE"
    -Dport="$APPIUM_PORT"
    -DcloudApp=
    -Dios.simulator.platformName="$SIM_PLATFORM_NAME"
    -Dios.simulator.automationName="$SIM_AUTOMATION_NAME"
    -Dios.local.bundleId="$SIM_BUNDLE_ID"
  )

  if [[ -n "$SIM_UDID" ]]; then
    MAVEN_CMD+=( -Dios.simulator.udid="$SIM_UDID" )
  fi
  if [[ -n "$SIM_DEVICE_NAME" ]]; then
    MAVEN_CMD+=( -Dios.simulator.deviceName="$SIM_DEVICE_NAME" )
  fi
  if [[ -n "$SIM_PLATFORM_VERSION" ]]; then
    MAVEN_CMD+=( -Dios.simulator.platformVersion="$SIM_PLATFORM_VERSION" )
  fi
  if [[ -n "$SIM_APP_PATH" ]]; then
    MAVEN_CMD+=( -Dios.local.app.path="$SIM_APP_PATH" )
  fi
fi

echo "▶ Subaru run configuration"
echo "  Platform  : $PLATFORM"
echo "  Region    : $REGION"
echo "  Target    : $TARGET"
echo "  Test root : $ROOT_DIR"
echo "  Search dir: $SEARCH_DIR"
if [[ "$PLATFORM" == "ios" ]]; then
  echo "  iOS Mode  : $IOS_MODE"
  if [[ "$IOS_MODE" == "simulator" ]]; then
    echo "  Local     : $LOCAL_PROFILE"
    echo "  Appium    : http://127.0.0.1:${APPIUM_PORT}/wd/hub"
    echo "  Bundle ID : $SIM_BUNDLE_ID"
    if [[ -n "$SIM_UDID" ]]; then
      echo "  Sim UDID  : $SIM_UDID"
    else
      echo "  Sim UDID  : (not resolved; framework will use config fallback)"
    fi
    if [[ -n "$SIM_DEVICE_NAME" ]]; then
      echo "  Sim Name  : $SIM_DEVICE_NAME"
    fi
    if [[ -n "$SIM_PLATFORM_VERSION" ]]; then
      echo "  iOS Ver   : $SIM_PLATFORM_VERSION"
    fi
  fi
fi
if [[ -n "$TAG_EXPR" ]]; then
  echo "  Tags      : $TAG_EXPR"
else
  echo "  Tags      : (none)"
fi

echo ""
echo "▶ Maven command"
printf '  %q ' "${MAVEN_CMD[@]}"
echo ""

if [[ "$DRY_RUN" == true ]]; then
  echo ""
  echo "Dry run enabled. Command not executed."
  exit 0
fi

"${MAVEN_CMD[@]}"