# Android APK Files

Drop the Android APK file for Subaru OneApp here before running Android tests.

The framework auto-discovers the **latest `.apk`** in this folder — no config change needed.

## Why APKs are not committed
APK files are typically 300-400 MB, exceeding GitHub's 100 MB limit. 

## How to add an APK
1. Copy your debug APK to this folder:
   ```
   cp ~/Downloads/app-subaruOne-naSubaru-stage-debug.apk src/test/resources/apps/
   ```
2. Run Android tests:
   ```
   ./run.sh android resetpwd
   ```

The framework picks up the newest `.apk` in this folder automatically.
