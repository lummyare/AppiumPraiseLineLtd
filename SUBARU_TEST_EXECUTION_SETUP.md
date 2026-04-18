### Subaru Test Execution Setup

This document captures the requested analysis and the runnable setup for Subaru tests using the same CLI-driven approach as `run.sh` from `main`.

### 1) Analysis of existing `run.sh` in `main`

#### How it works
- `run.sh` is a Bash wrapper around Maven test execution.
- It resolves a **shortcut** (e.g., `smoke`, `signin`, `resetpwd`) into a **Cucumber tag expression**.
- It executes:

```bash
mvn clean test -Dplatform=<ios|android> -Dcucumber.filter.tags="(<resolved-tag>) and not @ignore"
```

- It includes environment helpers for:
  - Java setup (`JAVA_HOME` / Java 17+)
  - ffmpeg detection (recording support)
  - Android SDK exports (`ANDROID_HOME`, `ANDROID_SDK_ROOT`)
  - optional `@record` behavior

#### Parameters accepted by `run.sh`
- `platform` (optional): `ios` (default) or `android`
- `scenario/shortcut` (optional): `21mm`, `24mm`, `all`, `smoke`, `explore`, onboarding aliases, `tag @CustomTag`
- optional `@record` anywhere in args

#### iOS vs Android handling
- Platform is parsed from the first arg if it is `ios|android`, else defaults to `ios`.
- Passed to Maven as `-Dplatform=<platform>`.
- Test code consumes this property and runs on the corresponding platform setup paths.

#### Tag usage in `run.sh`
- It resolves aliases to **Cucumber tags** (e.g., `smoke -> @smoke`, `all -> @login`).
- It supports direct custom tags via `tag @MyTag` or direct `@MyTag`.
- Final filtering is done via `-Dcucumber.filter.tags`.

#### Framework used by `run.sh`
- `run.sh` targets **Cucumber + TestNG** flow (via `testng.xml` and Cucumber runner).

---

### 2) Subaru test structure in `feature/subaru-integration-clean`

#### High-level structure
- Subaru tests live under:
  - `src/test/java/v2update/subaru/ios/...`
  - `src/test/java/v2update/subaru/android/...`
- Branch comparison:
  - `main`: Subaru tree not present
  - `feature/subaru-integration-clean`: Subaru tree present (134 Java files)

#### Platform / region organization
- Android:
  - `v2update/subaru/android/usa/...`
  - `v2update/subaru/android/canada/French/...`
- iOS:
  - `v2update/subaru/ios/usa/...`
  - `v2update/subaru/ios/canada/french/...`

#### Functional organization (examples)
- `accountSettings`, `dashboard`, `remote`, `find`, `vehicleInfo`, `vehicles`, `health`, `pay`, `shop`, `smoke`, `status`, etc.

#### Test classes and annotations
- Subaru classes use **JUnit 5** annotations (`org.junit.jupiter.api`).
- Common annotations in Subaru classes:
  - `@Test`, `@BeforeAll`, `@AfterAll`, `@Order`, `@Tag`, `@ExtendWith(ReportPortalExtension.class)`
- Example classes:
  - `SubaruSmokeIOS`, `SubaruSmokeAndroid`
  - `SubaruHealthIOS`, `SubaruHealthAndroid`
  - `SubaruVehicleStatusIOS`, `SubaruVehicleStatusAndroid`

#### Tags found in Subaru tests
Common JUnit `@Tag` values include:
- `Dashboard`, `Health`, `Status`
- `RemoteCommands`, `GuestDriver`
- `Account`, `AccountSettings`, `PersonalInfo`, `SecuritySettings`, `NotificationSettings`, `LinkedAccounts`, `DriveConnect`, `HelpAndFeedback`, `DataPrivacyPortal`
- `VehicleInfo`, `Vehicles`, `Subscriptions`
- `Find`, `Destinations`, `Pay`, `Shop`, `ChargeManagement`, `Deeplinks`, `SignOut`

#### Naming convention
- Most classes start with `Subaru`.
- iOS classes usually end with `IOS`.
- Android classes usually end with `Android`.
- Canada French classes often include `CAFrench` in class name.
- Folder structure carries platform + region + functional area consistently.

---

### 3) New Subaru execution script

A new script was added:

- `run_subaru.sh`

This keeps the original `run.sh` behavior intact for existing Cucumber/TestNG runs while adding Subaru-specific JUnit 5 execution.

#### Why separate script (instead of replacing `run.sh`)
- Existing `run.sh` is Cucumber/TestNG-oriented.
- Subaru tests are JUnit 5 class-based tests with `@Tag` annotations.
- A dedicated script avoids breaking existing main workflows.

#### What `run_subaru.sh` does
- Accepts platform/region/target similar to shortcut-style execution.
- Resolves target directory for Subaru tests.
- Auto-builds the Maven `-Dtest=` class selector from discovered Java test classes.
- Disables TestNG suite for this run path using `-DsuiteXmlFile=`.
- Supports JUnit tag filtering with `-Dgroups="<expression>"`.

---

### 4) How to run Subaru tests

#### Script usage

```bash
./run_subaru.sh [platform] [region] [target] [tag-expression] [--dry-run]
```

- `platform`: `ios` (default) | `android`
- `region`: `usa` (default) | `ca-fr`
- `target`:
  - `smoke` (default)
  - `all`
  - functional area folder name, e.g. `accountSettings`, `dashboard`, `remote`, `find`, `vehicleInfo`, `vehicles`
  - `tag` (use next argument as tag expression)

#### Subaru iOS examples

```bash
# default = ios usa smoke
./run_subaru.sh

# iOS USA smoke
./run_subaru.sh ios usa smoke

# iOS USA all Subaru tests
./run_subaru.sh ios usa all

# iOS Canada French dashboard area
./run_subaru.sh ios ca-fr dashboard
```

#### Subaru Android examples

```bash
# Android USA smoke
./run_subaru.sh android usa smoke

# Android USA all
./run_subaru.sh android usa all

# Android Canada French remote area
./run_subaru.sh android ca-fr remote
```

#### Tag filtering examples

```bash
# Explicit tag mode
./run_subaru.sh ios usa tag "RemoteCommands"

# Tag expression with logical operators
./run_subaru.sh android usa tag "Dashboard and not GuestDriver"

# Apply tag expression on top of target=all
./run_subaru.sh ios ca-fr all "Account and NotificationSettings"
```

#### Dry-run preview

```bash
./run_subaru.sh android usa all "Dashboard or Health" --dry-run
```

This prints the resolved Maven command without running tests.

---

### 5) Files added

- `run_subaru.sh`
- `SUBARU_TEST_EXECUTION_SETUP.md`

---

### 6) Notes

- This setup intentionally preserves existing `run.sh` for Cucumber/TestNG flows.
- Use `run_subaru.sh` for Subaru JUnit 5 flows.
- If your local machine does not have Maven/Java configured, install/verify them first before running.
