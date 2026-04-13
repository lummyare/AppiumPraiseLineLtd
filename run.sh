#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────────
# run.sh — Appium test runner shortcut
#
# Usage:
#   ./run.sh              → runs all @login scenarios (both accounts)
#   ./run.sh 21mm         → runs @21mmEVDummy1 only  (sub2_21mm@mail.tmnact.io)
#   ./run.sh 24mm         → runs @24MMEVDummy1 only  (subarustg02_21mm@mail.tmnact.io)
#   ./run.sh all          → runs all @login scenarios
#   ./run.sh smoke        → runs @smoke scenarios
#   ./run.sh explore      → runs @uiexplorer — dumps all screen UI elements to logs/ui-elements/
#   ./run.sh tag @MyTag   → runs any custom tag
#
# ── Onboarding E2E shortcuts ──────────────────────────────────────────────────
#   ./run.sh signin       → runs @SignIn_Email_PhoneLogin (OB_E2E_001–005)
#   ./run.sh resetpwd     → runs @Reset_Password (OB_E2E_006–008)
#   ./run.sh newdevice    → runs @SignIn_NewDevice (OB_E2E_009–010)
#   ./run.sh unverified   → runs @SignIn_UnverifiedMobile (OB_E2E_011–013)
#   ./run.sh register     → runs @CreateAccount_Registration (OB_E2E_014–016)
#   ./run.sh cvn          → runs @CreateAccount_Registration_CVN (OB_E2E_017–018)
#   ./run.sh emailupdate  → runs @CreateAccount_Registration_EmailUpdate (OB_E2E_019–020)
#   ./run.sh phoneverify  → runs @CreateAccount_Registration_PhoneVerification (OB_E2E_021–022)
#   ./run.sh emailremind  → runs @CreateAccount_Registration_EmailVerificationReminder (OB_E2E_023–024)
#   ./run.sh social       → runs @CreateAccount_Social_Registration (OB_E2E_025–027)
#   ./run.sh language     → runs @ChangeRegionLanguage (OB_E2E_028–029)
#   ./run.sh addvin       → runs @AddVINScanEnter (OB_E2E_030–032)
#   ./run.sh addqr        → runs @AddVINQRCODE (OB_E2E_033–034)
#   ./run.sh subscription → runs @SubscriptionTrialEnrollment (OB_E2E_035–036)
#   ./run.sh biometric    → runs @BiometricLogin (OB_E2E_037–039)
#   ./run.sh keepsignin   → runs @KeepSignIn (OB_E2E_040–042)
#   ./run.sh onboarding   → runs ALL 42 onboarding E2E scenarios
#
# Adding a new profile later:
#   1. Create src/test/resources/credentials/MyNewProfile.properties
#   2. Add a scenario tagged @MyNewProfile in Login.feature
#   3. Run: ./run.sh tag @MyNewProfile
# ─────────────────────────────────────────────────────────────────────────────

set -e

# ── Auto-detect Java 17 ──────────────────────────────────────────────────────
# /usr/libexec/java_home -v 17 can silently return a different version if 17
# is not installed. We verify the actual major version reported by javac.
setup_java() {
  # Try to find a JDK whose path contains '17' first (most specific)
  local candidate
  candidate=$(/usr/libexec/java_home -v 17 2>/dev/null || true)

  if [ -n "$candidate" ]; then
    # Verify the JDK at that path actually reports version 17
    local actual_version
    actual_version=$("$candidate/bin/javac" -version 2>&1 | awk '{print $2}' | cut -d. -f1)
    if [ "$actual_version" = "17" ]; then
      export JAVA_HOME="$candidate"
      echo "✓ Using Java 17: $JAVA_HOME"
      return
    fi
  fi

  # Fallback: scan all installed JVMs and pick the first that is 17+
  while IFS= read -r jvm_path; do
    local ver
    ver=$("$jvm_path/Contents/Home/bin/javac" -version 2>&1 | awk '{print $2}' | cut -d. -f1)
    if [ "$ver" -ge 17 ] 2>/dev/null; then
      export JAVA_HOME="$jvm_path/Contents/Home"
      echo "✓ Using Java $ver (from $JAVA_HOME)"
      return
    fi
  done < <(/usr/libexec/java_home -V 2>&1 | grep -oE '/[^ ]+\.jdk' | head -20)

  echo "✗ No Java 17+ JDK found."
  echo "  Install with: brew install --cask temurin@17"
  exit 1
}

# ── Resolve the Cucumber tag from the argument ───────────────────────────────
resolve_tag() {
  case "$1" in
    21mm)         echo "@21mmEVDummy1" ;;
    24mm)         echo "@24MMEVDummy1" ;;
    all|"")       echo "@login" ;;
    smoke)        echo "@smoke" ;;
    explore)      echo "@uiexplorer" ;;  # ./run.sh explore → dumps all screen elements
    # ── Onboarding E2E shortcuts ─────────────────────────────────────────────
    signin)       echo "@SignIn_Email_PhoneLogin" ;;
    resetpwd)     echo "@Reset_Password" ;;
    newdevice)    echo "@SignIn_NewDevice" ;;
    unverified)   echo "@SignIn_UnverifiedMobile" ;;
    register)     echo "@CreateAccount_Registration" ;;
    cvn)          echo "@CreateAccount_Registration_CVN" ;;
    emailupdate)  echo "@CreateAccount_Registration_EmailUpdate" ;;
    phoneverify)  echo "@CreateAccount_Registration_PhoneVerification" ;;
    emailremind)  echo "@CreateAccount_Registration_EmailVerificationReminder" ;;
    social)       echo "@CreateAccount_Social_Registration" ;;
    language)     echo "@ChangeRegionLanguage" ;;
    addvin)       echo "@AddVINScanEnter" ;;
    addqr)        echo "@AddVINQRCODE" ;;
    subscription) echo "@SubscriptionTrialEnrollment" ;;
    biometric)    echo "@BiometricLogin" ;;
    keepsignin)   echo "@KeepSignIn" ;;
    onboarding)   echo "@SignIn_Email_PhoneLogin or @Reset_Password or @SignIn_NewDevice or @SignIn_UnverifiedMobile or @CreateAccount_Registration or @CreateAccount_Registration_CVN or @CreateAccount_Registration_EmailUpdate or @CreateAccount_Registration_PhoneVerification or @CreateAccount_Registration_EmailVerificationReminder or @CreateAccount_Social_Registration or @ChangeRegionLanguage or @AddVINScanEnter or @AddVINQRCODE or @SubscriptionTrialEnrollment or @BiometricLogin or @KeepSignIn" ;;
    tag)          echo "$2" ;;           # ./run.sh tag @CustomTag
    *)
      # Treat anything starting with @ as a direct tag
      if [[ "$1" == @* ]]; then
        echo "$1"
      else
        echo "Unknown shortcut: '$1'"
        echo "Use: 21mm | 24mm | all | smoke | tag @YourTag"
        exit 1
      fi
      ;;
  esac
}

# ── Main ─────────────────────────────────────────────────────────────────────
setup_java

TAG=$(resolve_tag "$1" "$2")
echo "▶ Running scenarios tagged: $TAG"
echo ""

cd "$(dirname "$0")"   # ensure we're in the project root regardless of where script is called from

mvn clean test -Dcucumber.filter.tags="($TAG) and not @ignore"
