# AttendanceManagementApplication Documentation

## Overview
The `AttendanceManagementApplication` class is the main entry point for the Spring Boot application. It contains the `main` method that starts the application.

## File Location
`src/main/java/com/attendance/AttendanceManagementApplication.java`

## Libraries Used

### 1. `org.springframework.boot.SpringApplication`
- **Purpose**: Spring Boot application launcher
- **Why**: Starts the Spring Boot application context

### 2. `org.springframework.boot.autoconfigure.SpringBootApplication`
- **Purpose**: Composite annotation that includes:
  - `@Configuration`: Marks as configuration class
  - `@EnableAutoConfiguration`: Enables auto-configuration
  - `@ComponentScan`: Scans for components
- **Why**: Simplifies Spring Boot application setup

## Methods

### `main(String[] args)`
- **Purpose**: Application entry point
- **Parameters**: `String[] args` - Command line arguments
- **Why**: Standard Java main method
- **Functionality**: 
  1. Creates SpringApplication instance
  2. Runs application with this class as source
  3. Starts embedded server (Tomcat)
  4. Initializes Spring context
  5. Runs CommandLineRunner beans (like DataInitializer)

## Application Startup Flow
1. `main` method called
2. Spring Boot initializes context
3. Scans for components (@Service, @Controller, @Component)
4. Loads configuration (application.properties)
5. Initializes Firebase (FirebaseConfig)
6. Runs DataInitializer (creates default data)
7. Starts embedded web server
8. Application ready to accept requests

## Related Files
- `application.properties`: Application configuration
- All other classes: Loaded by Spring context

