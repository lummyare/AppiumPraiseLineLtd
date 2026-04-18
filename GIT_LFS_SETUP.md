# Subaru JAR Dependency Setup (No Git LFS)

`libs/TestAutomation-1.3.29-all.jar` is **not tracked in git** because it exceeds GitHub's 100 MB file limit.

Instead, this repository uses an automatic setup flow:

- `setup_dependencies.sh` downloads the JAR when needed
- `run_subaru.sh` auto-invokes `setup_dependencies.sh` if the JAR is missing
- Both scripts verify MD5 checksum:
  - `9176919dd56e2198370b50f73583cd14`

## 1) Configure direct download URL

Export a direct file URL (must return the JAR bytes directly):

```bash
export TESTAUTOMATION_JAR_URL="https://<your-direct-host>/TestAutomation-1.3.29-all.jar"
```

> Note: Page links (for example a generic folder/share page) are not enough.
> The URL must support non-interactive CLI download.

## 2) Download dependencies explicitly (optional)

```bash
./setup_dependencies.sh
```

## 3) Run Subaru tests (auto-download fallback)

```bash
./run_subaru.sh ios usa smoke
```

If the JAR is missing or invalid, `run_subaru.sh` will attempt to repair it via `setup_dependencies.sh`.

## 4) Manual verification (optional)

```bash
md5sum libs/TestAutomation-1.3.29-all.jar
```

Expected:

```text
9176919dd56e2198370b50f73583cd14
```
