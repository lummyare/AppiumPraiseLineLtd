# Git LFS Setup for Subaru Integration

The file `libs/TestAutomation-1.3.29-all.jar` is stored in Git LFS.
If Git LFS objects are not pulled, Maven/Java will fail with errors like:

- `zip END header not found`
- `error reading libs/TestAutomation-1.3.29-all.jar`

## One-time setup

```bash
git lfs install
```

## Pull the JAR content

```bash
git lfs pull --include="libs/TestAutomation-1.3.29-all.jar"
```

## Verify

```bash
wc -c libs/TestAutomation-1.3.29-all.jar
file libs/TestAutomation-1.3.29-all.jar
```

A valid file should be a large zip/JAR binary (not a tiny text pointer file).

## Note

This JAR is currently larger than GitHub's normal file size limit for non-LFS blobs, so it must remain in Git LFS.
