# Release Process

## Creating a New Release

1. **Update Version Numbers**
   - Update `versionCode` and `versionName` in `app/build.gradle`
   - Create a new changelog file in `metadata/en-US/changelogs/{versionCode}.txt`

2. **Create Git Tag**
   ```bash
   git tag -a v3.0 -m "Release version 3.0"
   git push origin v3.0
   ```

3. **GitHub Actions**
   - The release workflow will automatically trigger on tag push
   - It will build a signed release APK and create a GitHub release

4. **F-Droid Submission**
   - F-Droid will automatically detect the new release via the metadata files
   - The app will be built on F-Droid's infrastructure using the metadata configuration

## Setup Instructions for Maintainers

### GitHub Secrets (for signed releases)
Set up these secrets in your GitHub repository:
- `SIGNING_KEY`: Base64 encoded keystore file
- `ALIAS`: Key alias
- `KEY_STORE_PASSWORD`: Keystore password  
- `KEY_PASSWORD`: Key password

### Generating a Keystore
```bash
keytool -genkey -v -keystore assistral-release-key.keystore -alias assistral -keyalg RSA -keysize 2048 -validity 10000
```

### Converting to Base64 for GitHub Secrets
```bash
base64 assistral-release-key.keystore | tr -d '\n'
```

## Current Release: v3.0

- **Version Code**: 300
- **Version Name**: 3.0
- **Release Date**: 2025-01-XX
- **Key Changes**: Complete migration from gptAssist/ChatGPT to Assistral/Mistral Le Chat

## Release History

### v3.0 (300) - 2025-01-XX
- Complete rewrite from gptAssist to Assistral
- Migrated from ChatGPT to Mistral Le Chat
- Updated all branding, package names, and metadata
- First release targeting F-Droid distribution