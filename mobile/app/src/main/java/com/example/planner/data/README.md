# Data Layer Documentation

## Overview

The data layer is responsible for data retrieval, storage, and manipulation. It implements the repository interfaces defined in the domain layer and handles all network and database operations.

## Structure

```
data/
├── dao/               # Data Access Objects for Room database
├── database/          # Database configuration
├── dto/               # Data Transfer Objects for API
├── mapper/            # Mappers between DTOs and domain models
├── model/             # Data models (Room entities)
├── network/           # Network-related components
└── repository/        # Repository implementations
```

## Key Components

### 1. DTOs (Data Transfer Objects)

DTOs are used for API communication:

- **Auth DTOs**: For authentication (login, register)
- **Project DTOs**: For project operations
- **Task DTOs**: For task operations
- **User DTOs**: For user operations
- **Employee DTOs**: For employee operations

### 2. Mappers

Mappers convert between DTOs and domain models:

- **DomainMappers**: Converts DTOs to domain models
- **UI Mappers**: Converts domain models to UI models

### 3. Repositories

Repository implementations:

- **AuthRepository**: Handles authentication operations
- **UserRepository**: Handles user operations
- **ProjectRepository**: Handles project operations
- **TaskRepository**: Handles task operations
- **ProjectRepositoryImpl**: New implementation using domain models

### 4. Network

Network-related components:

- **ApiService**: Retrofit service interface
- **RetrofitInstance**: Retrofit configuration
- **AuthInterceptor**: Adds authentication token to requests
- **TokenManager**: Manages authentication tokens
- **MoshiAdapters**: Custom Moshi adapters for date/time

### 5. Database

Room database configuration:

- **PlannerDatabase**: Main database configuration
- **DAOs**: Data Access Objects for each entity
- **Converters**: Type converters for Room

## Architecture Principles

1. **Separation of Concerns**: Data layer handles data operations only
2. **Single Source of Truth**: Repository pattern provides consistent data access
3. **Error Handling**: Consistent error handling with Result type
4. **Network First**: Prioritize network operations, use cache as fallback
5. **Thread Safety**: All operations should be thread-safe

## Key Features

### 1. Network Operations

All network operations use Retrofit and return Result type:

```kotlin
suspend fun getProjects(): Result<List<Project>> {
    return try {
        val response = api.getProjects()
        if (response.isSuccessful) {
            Result.success(response.body()?.toDomain() ?: emptyList())
        } else {
            Result.failure(NetworkException("Failed to load projects"))
        }
    } catch (e: Exception) {
        Result.failure(NetworkException("Network error occurred", e))
    }
}
```

### 2. Error Handling

All errors are wrapped in custom exceptions:

- **NetworkException**: For network-related errors
- **ValidationException**: For validation errors
- **AuthenticationException**: For authentication errors

### 3. Token Management

TokenManager handles authentication tokens:

```kotlin
fun saveToken(token: String, expiresAt: Date?)
fun getToken(): String?
fun isTokenValid(): Boolean
fun clearToken()
```

### 4. Mappers

Mappers provide clean conversion between layers:

```kotlin
fun ProjectListingDto.toDomain(): Project {
    return Project(
        id = id,
        name = name,
        createdAt = "",
        updatedAt = ""
    )
}
```

## Best Practices

1. **Use Result type**: Always return Result for better error handling
2. **Handle errors consistently**: Use custom exceptions for different error types
3. **Validate input**: Validate input parameters before making requests
4. **Use coroutines**: All operations should be suspend functions
5. **Thread safety**: Ensure all operations are thread-safe
6. **Document APIs**: Add documentation for all public methods

## Testing

Data layer should be tested with:

- **Unit tests**: For mappers and utility functions
- **Integration tests**: For repository implementations
- **Mock tests**: For network operations

## Future Improvements

1. Add database caching for offline support
2. Implement better error recovery strategies
3. Add logging for better debugging
4. Improve token management with refresh tokens
5. Add more comprehensive testing
