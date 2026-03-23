# FirebaseConfig Documentation

## Overview
The `FirebaseConfig` class configures and initializes Firebase Realtime Database connection. It loads credentials and sets up Firebase Admin SDK.

## File Location
`src/main/java/com/attendance/config/FirebaseConfig.java`

## Libraries Used

### 1. `com.google.auth.oauth2.GoogleCredentials`
- **Purpose**: Google Cloud credentials
- **Why**: Authenticates with Firebase

### 2. `com.google.firebase.FirebaseApp`
- **Purpose**: Firebase application instance
- **Why**: Main Firebase SDK entry point

### 3. `com.google.firebase.FirebaseOptions`
- **Purpose**: Firebase configuration options
- **Why**: Sets database URL and credentials

### 4. `com.google.firebase.database.FirebaseDatabase`
- **Purpose**: Firebase Realtime Database instance
- **Why**: Database operations

### 5. `org.springframework.beans.factory.annotation.Value`
- **Purpose**: Injects property values
- **Why**: Reads configuration from application.properties

### 6. `org.springframework.context.annotation.Bean`
- **Purpose**: Creates Spring beans
- **Why**: Provides FirebaseDatabase bean

### 7. `jakarta.annotation.PostConstruct`
- **Purpose**: Executes after bean construction
- **Why**: Initializes Firebase on startup

## Configuration Properties

### `firebase.database.url`
- **Type**: String
- **Purpose**: Firebase Realtime Database URL
- **Example**: `https://your-project.firebaseio.com`
- **Why**: Required for database connection

### `firebase.service.account.path`
- **Type**: String (optional)
- **Purpose**: Path to Firebase service account JSON file
- **Why**: Alternative to environment variable
- **Default**: Empty (uses environment variable)

## Methods

### `initialize()`
- **Annotation**: `@PostConstruct`
- **Purpose**: Initializes Firebase on application startup
- **Why**: Sets up Firebase connection before other beans use it
- **Functionality**:
  1. Checks if Firebase already initialized
  2. Tries to load credentials from file path (if provided)
  3. Falls back to `GOOGLE_APPLICATION_CREDENTIALS` environment variable
  4. Falls back to default credentials
  5. Throws exception if no credentials found
  6. Initializes FirebaseApp with options
  7. Prints success message

### `firebaseDatabase()`
- **Annotation**: `@Bean`
- **Purpose**: Provides FirebaseDatabase bean
- **Returns**: `FirebaseDatabase` instance
- **Why**: Other services can inject this for database operations

## Credential Loading Priority
1. File path from `firebase.service.account.path` property
2. `GOOGLE_APPLICATION_CREDENTIALS` environment variable
3. Default application credentials
4. Throws exception if none found

## Error Handling
- Provides clear error messages
- Suggests configuration options
- Throws RuntimeException with helpful messages

## Related Files
- `application.properties`: Configuration file
- `FirebaseService.java`: Uses FirebaseDatabase
- All Firebase*Service classes: Use FirebaseDatabase

