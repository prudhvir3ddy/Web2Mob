# Web-To-Mobile App Generator

A service that converts web URLs into Android applications using GitHub Actions automation. This project allows you to
quickly create a WebView-based Android app from any web URL with a custom package name

**Work in progress ⚠️:** 
Build iOS app 

### Features

- Convert any website into an Android app
- Customize package name
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

**Generate Android App**

**Endpoint:** POST /generate

**Request Body:**

```json 
{
  "url": "https://example.com",
  "package_name": "com.example.app",
  "secret": "your_app_secret"
}
```

**Response:**

- 200 OK: App generation started successfully
- 401 Unauthorized: Invalid secret
- 417 Expectation Failed: GitHub API error

### How It Works

1. The API receives a request with a URL and package name
2. It triggers a GitHub repository dispatch event
3. GitHub Actions workflow clones the Android template project
4. The workflow customizes the template with your URL and package name
5. The app is built and made available as a downloadable APK

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