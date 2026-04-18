#!/usr/bin/env bash
set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "$0")" && pwd)"
LIB_DIR="$PROJECT_ROOT/libs"
JAR_NAME="TestAutomation-1.3.29-all.jar"
JAR_PATH="$LIB_DIR/$JAR_NAME"
EXPECTED_MD5="9176919dd56e2198370b50f73583cd14"

# Fallback pattern:
# - Set TESTAUTOMATION_JAR_URL to a true direct file download URL
# - If not set, script exits with a clear instruction
JAR_URL="${TESTAUTOMATION_JAR_URL:-}"

fail() {
  echo "✗ $1" >&2
  exit 1
}

require_command() {
  command -v "$1" >/dev/null 2>&1 || fail "Required command not found: $1"
}

compute_md5() {
  local file="$1"
  md5sum "$file" | awk '{print $1}'
}

download_file() {
  local url="$1"
  local out_file="$2"

  if command -v curl >/dev/null 2>&1; then
    curl --fail --location --silent --show-error "$url" --output "$out_file"
    return
  fi

  if command -v wget >/dev/null 2>&1; then
    wget -qO "$out_file" "$url"
    return
  fi

  fail "Neither curl nor wget is available for download"
}

ensure_jar() {
  mkdir -p "$LIB_DIR"

  # Fast path: jar already present and valid
  if [[ -f "$JAR_PATH" ]]; then
    local current_md5
    current_md5="$(compute_md5 "$JAR_PATH")"
    if [[ "$current_md5" == "$EXPECTED_MD5" ]]; then
      echo "✓ $JAR_NAME already present and checksum verified"
      return 0
    fi

    echo "⚠ Existing $JAR_NAME checksum mismatch. Re-downloading..."
    rm -f "$JAR_PATH"
  fi

  if [[ -z "$JAR_URL" ]]; then
    fail "TESTAUTOMATION_JAR_URL is not set. Please export a direct download URL for $JAR_NAME and rerun."
  fi

  local tmp_file
  tmp_file="$(mktemp "$LIB_DIR/${JAR_NAME}.tmp.XXXXXX")"
  trap 'rm -f "$tmp_file"' EXIT

  echo "▶ Downloading $JAR_NAME"
  echo "  Source: $JAR_URL"
  download_file "$JAR_URL" "$tmp_file"

  local downloaded_md5
  downloaded_md5="$(compute_md5 "$tmp_file")"
  if [[ "$downloaded_md5" != "$EXPECTED_MD5" ]]; then
    fail "Checksum verification failed for downloaded JAR. Expected=$EXPECTED_MD5 Actual=$downloaded_md5"
  fi

  mv "$tmp_file" "$JAR_PATH"
  trap - EXIT

  echo "✓ Downloaded and verified $JAR_NAME"
  echo "  Path: $JAR_PATH"
}

require_command md5sum
ensure_jar
