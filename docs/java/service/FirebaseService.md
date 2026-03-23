# FirebaseService Documentation

## Overview
The `FirebaseService` class provides a generic abstraction layer for Firebase Realtime Database operations. It handles CRUD operations, queries, and provides both asynchronous and synchronous methods.

## File Location
`src/main/java/com/attendance/service/FirebaseService.java`

## Libraries Used

### 1. `com.google.api.core.ApiFuture`
- **Purpose**: Asynchronous API operations for Firebase
- **Why**: Firebase operations are asynchronous, ApiFuture handles the async nature

### 2. `com.google.firebase.database.*`
- **Purpose**: Firebase Realtime Database SDK
- **Why**: Core Firebase functionality for database operations
- **Classes Used**:
  - `DatabaseReference`: Reference to database location
  - `FirebaseDatabase`: Firebase database instance
  - `DataSnapshot`: Snapshot of data from database
  - `DatabaseError`: Error handling
  - `ValueEventListener`: Listener for data changes
  - `Query`: Database queries

### 3. `org.springframework.beans.factory.annotation.Autowired`
- **Purpose**: Dependency injection
- **Why**: Injects FirebaseDatabase instance

### 4. `org.springframework.stereotype.Service`
- **Purpose**: Marks class as Spring service
- **Why**: Enables component scanning

### 5. `java.util.concurrent.CompletableFuture`
- **Purpose**: Modern async programming in Java
- **Why**: Converts Firebase callbacks to CompletableFuture for better async handling

### 6. `java.util.concurrent.TimeUnit`
- **Purpose**: Time unit specification
- **Why**: Used for timeout in synchronous methods

## Methods

### Asynchronous Methods (Return CompletableFuture)

#### `save(String path, T object)`
- **Purpose**: Saves object to Firebase at specified path
- **Parameters**: 
  - `String path` - Firebase path
  - `T object` - Object to save
- **Returns**: `CompletableFuture<String>` - Generated key
- **Why**: Generic save operation for any object type

#### `saveWithKey(String path, String key, T object)`
- **Purpose**: Saves object with specific key
- **Parameters**: 
  - `String path` - Firebase path
  - `String key` - Specific key to use
  - `T object` - Object to save
- **Returns**: `CompletableFuture<Void>`
- **Why**: Allows saving with known key (e.g., user ID)

#### `get(String path, Class<T> clazz)`
- **Purpose**: Gets single object by path
- **Parameters**: 
  - `String path` - Firebase path
  - `Class<T> clazz` - Class type to deserialize
- **Returns**: `CompletableFuture<T>` - Object if found, null otherwise
- **Why**: Retrieves single object from Firebase

#### `getAll(String path, Class<T> clazz)`
- **Purpose**: Gets all objects from path as map
- **Parameters**: 
  - `String path` - Firebase path
  - `Class<T> clazz` - Class type
- **Returns**: `CompletableFuture<Map<String, T>>` - Map of key-value pairs
- **Why**: Retrieves all children as map with keys

#### `getList(String path, Class<T> clazz)`
- **Purpose**: Gets all objects from path as list
- **Parameters**: 
  - `String path` - Firebase path
  - `Class<T> clazz` - Class type
- **Returns**: `CompletableFuture<List<T>>` - List of objects
- **Why**: Retrieves all children as list (keys not included)

#### `query(String path, String orderBy, Object value, Class<T> clazz)`
- **Purpose**: Queries objects with filter
- **Parameters**: 
  - `String path` - Firebase path
  - `String orderBy` - Field to order by
  - `Object value` - Value to match
  - `Class<T> clazz` - Class type
- **Returns**: `CompletableFuture<List<T>>` - List of matching objects
- **Why**: Filters data based on field value
- **Supports**: String, Number, Boolean values

#### `delete(String path)`
- **Purpose**: Deletes object at path
- **Parameters**: `String path` - Firebase path
- **Returns**: `CompletableFuture<Void>`
- **Why**: Removes data from Firebase

#### `exists(String path)`
- **Purpose**: Checks if path exists
- **Parameters**: `String path` - Firebase path
- **Returns**: `CompletableFuture<Boolean>` - True if exists
- **Why**: Validates data existence

#### `count(String path)`
- **Purpose**: Counts objects at path
- **Parameters**: `String path` - Firebase path
- **Returns**: `CompletableFuture<Long>` - Count of children
- **Why**: Gets number of items without loading all data

### Synchronous Methods (With 10-second timeout)

All synchronous methods wrap async methods with timeout:
- `getSync(String path, Class<T> clazz)`
- `getAllSync(String path, Class<T> clazz)`
- `getListSync(String path, Class<T> clazz)`
- `saveSync(String path, Object object)`
- `saveWithKeySync(String path, String key, Object object)`
- `deleteSync(String path)`
- `existsSync(String path)`
- `countSync(String path)`

**Purpose**: Provides blocking versions for convenience
**Timeout**: 10 seconds
**Why**: Some operations need synchronous behavior

## Error Handling
- All methods throw `RuntimeException` on failure
- Wraps Firebase exceptions for consistent error handling
- Provides clear error messages

## Usage Pattern
```java
// Async
firebaseService.save("users", user)
    .thenAccept(key -> System.out.println("Saved: " + key));

// Sync
String key = firebaseService.saveSync("users", user);
```

## Related Files
- All Firebase*Service classes use this for database operations
- `FirebaseConfig.java`: Configures Firebase connection

