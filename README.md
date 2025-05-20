# Web-To-Mobile App Generator

<img width="1447" alt="Screenshot 2025-05-03 at 10 26 50 PM" src="https://github.com/user-attachments/assets/5560b697-5819-4f03-8b48-c23ea3d25abd" />

A service that converts web URLs into Android applications using GitHub Actions automation. This project allows you to
quickly create a WebView-based Android or iOS app from any web URL with a custom package name/bundle identifier.

Frontend is hosted in: https://prudhvir3ddy.github.io/wtm

### Features

- Convert any website into an Android app
- Convert any website into an iOS app
- Customize package name / bundle identifier
- Automated build process via GitHub Actions
- REST API for app generation

### Setup

**Prerequisites**

- Docker (recommended) or JDK 17+
- Kotlin 1.8+

**Environment Variables**

```bash
GITHUB_TOKEN=your_github_personal_access_token
APP_SECRET=your_app_secret
```

**Running Locally**

**Using Docker**

1. Build the Docker image:

```
docker build -t wtm-backend .
```

2. Run the container:

```
docker run -p 8080:8080 --env-file .env wtm-backend
```

**Without Docker**

Set environment variables:

```
export GITHUB_TOKEN=your_github_personal_access_token
export APP_SECRET=your_app_secret
```

**Run the application:**

```
./gradlew run
```

### API Usage

**Generate App (Android/iOS)**

**Endpoint:** POST /generate

**Request Body:**

```json
{
  "url": "https://example.com",
  "package_name": "com.example.app",
  "platform": "android", // Optional: "ios" or "android". Defaults to "android".
  "secret": "your_app_secret"
}
```
**Note:** The `platform` field is optional. If omitted, it defaults to `android`. Specify `ios` to generate an iOS app.

**Response:**

- 200 OK: App generation started successfully
- 401 Unauthorized: Invalid secret
- 417 Expectation Failed: GitHub API error

### How It Works

1. The API receives a request with a URL, package name/bundle identifier, and optionally, a platform (defaults to Android).
2. It triggers a specific GitHub repository dispatch event based on the platform (`build-app` for Android, `build-ios-app` for iOS).
3. The corresponding GitHub Actions workflow clones the respective native template project (Android or iOS).
4. The workflow customizes the template with your URL and package name/bundle identifier.
5. The app is built (APK for Android, IPA/xcarchive for iOS) and made available as a downloadable artifact.

### CORS Configuration
The API allows requests from:

- localhost (any port, HTTP)
- prudhvir3ddy.github.io (HTTPS)

### Security
- Authentication is handled via an app secret
- GitHub token is stored as an environment variable
- No sensitive information is stored in the codebase

### License
MIT
