#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────────
# run_subaru.sh — Subaru JUnit 5 test runner (modeled after run.sh)
#
# Usage:
#   ./run_subaru.sh [platform] [region] [target] [tag-expression] [--dry-run]
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
# Examples:
#   ./run_subaru.sh
#   ./run_subaru.sh ios usa smoke
#   ./run_subaru.sh android usa all
#   ./run_subaru.sh ios ca-fr accountSettings
#   ./run_subaru.sh android usa tag "RemoteCommands and not GuestDriver"
#   ./run_subaru.sh ios usa all "Dashboard or Health"
#
# Notes:
# - Subaru tests are JUnit 5 tests under src/test/java/v2update/subaru/**
# - This runner disables the TestNG suite used by main run.sh via -DsuiteXmlFile=
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

# parse positional args while supporting optional platform + region + target
ARGS=()
for arg in "$@"; do
  if [[ "$arg" == "--dry-run" ]]; then
    DRY_RUN=true
  else
    ARGS+=("$arg")
  fi
done

if [[ ${#ARGS[@]} -ge 1 ]]; then
  case "${ARGS[0]}" in
    ios|android) PLATFORM="${ARGS[0]}" ;;
    *) ;;
  esac
fi

if [[ ${#ARGS[@]} -ge 2 ]]; then
  case "${ARGS[1]}" in
    usa|ca-fr) REGION="${ARGS[1]}" ;;
    *) ;;
  esac
fi

if [[ ${#ARGS[@]} -ge 3 ]]; then
  TARGET="${ARGS[2]}"
fi

if [[ ${#ARGS[@]} -ge 4 ]]; then
  TAG_EXPR="${ARGS[3]}"
fi

# infer missing pieces for shorthand calls:
# ./run_subaru.sh smoke
# ./run_subaru.sh ios smoke
# ./run_subaru.sh android usa tag "Dashboard"
if [[ ${#ARGS[@]} -ge 1 && "${ARGS[0]}" != "ios" && "${ARGS[0]}" != "android" ]]; then
  TARGET="${ARGS[0]}"
fi
if [[ ${#ARGS[@]} -ge 2 && "${ARGS[0]}" =~ ^(ios|android)$ && "${ARGS[1]}" != "usa" && "${ARGS[1]}" != "ca-fr" ]]; then
  TARGET="${ARGS[1]}"
fi
if [[ ${#ARGS[@]} -ge 3 && "${ARGS[0]}" =~ ^(ios|android)$ && "${ARGS[1]}" =~ ^(usa|ca-fr)$ ]]; then
  TARGET="${ARGS[2]}"
fi
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

TEST_SELECTOR="$(build_test_selector "$SEARCH_DIR")"
if [[ -z "$TEST_SELECTOR" ]]; then
  echo "✗ No runnable test classes found under $SEARCH_DIR"
  exit 1
fi

# If target is not explicit tag mode, a 4th argument can still be used as a tag filter
MAVEN_CMD=(
  mvn clean test
  -DsuiteXmlFile=
  -Dplatform="$PLATFORM"
  -Dtest="$TEST_SELECTOR"
)

if [[ -n "$TAG_EXPR" ]]; then
  MAVEN_CMD+=( -Dgroups="$TAG_EXPR" )
fi

echo "▶ Subaru run configuration"
echo "  Platform  : $PLATFORM"
echo "  Region    : $REGION"
echo "  Target    : $TARGET"
echo "  Test root : $ROOT_DIR"
echo "  Search dir: $SEARCH_DIR"
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
