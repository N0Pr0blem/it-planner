# UI Layer Documentation

## Overview

The UI layer is responsible for presenting data to the user and handling user interactions. It uses Jetpack Compose for declarative UI and follows the MVVM (Model-View-ViewModel) pattern.

## Structure

```
ui/
├── components/       # Reusable UI components
├── navigation/       # Navigation components
├── screens/          # Screen components
├── theme/            # Theme and styling
└── viewmodel/        # ViewModel components
```

## Key Components

### 1. Screens

Screen components represent different views in the application:

- **Auth Screens**: Login, Register, Verify
- **Project Screens**: Projects list, Project details, Create project
- **Task Screens**: Task details, Create task, Time tracking
- **User Screens**: Personal account, User info

### 2. ViewModels

ViewModels handle business logic and state management:

- **AuthViewModel**: Handles authentication state
- **ProjectsViewModel**: Handles projects state
- **ProjectDetailsViewModel**: Handles project details state
- **TaskDetailsViewModel**: Handles task details state
- **CreateTaskViewModel**: Handles task creation state
- **ProfileViewModel**: Handles user profile state

### 3. Navigation

Navigation components handle routing between screens:

- **NavRoutes**: Defines all navigation routes
- **AppNavHost**: Main navigation host

### 4. Theme

Theme components define the visual style:

- **Color**: Color palette
- **Type**: Typography
- **Theme**: Main theme configuration

### 5. Components

Reusable UI components:

- **Buttons**: Custom buttons
- **Cards**: Custom card components
- **Dialogs**: Custom dialog components
- **Inputs**: Custom input components

## Architecture Principles

1. **Declarative UI**: Uses Jetpack Compose for declarative UI
2. **Unidirectional Data Flow**: Data flows from ViewModel to UI
3. **State Management**: Uses StateFlow for state management
4. **Separation of Concerns**: UI handles presentation only
5. **Reusability**: Maximize component reusability

## Key Features

### 1. State Management

All screens use StateFlow for state management:

```kotlin
class ProjectsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProjectsUiState())
    val uiState: StateFlow<ProjectsUiState> = _uiState.asStateFlow()
    
    fun loadProjects() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            // Load projects
            _uiState.value = _uiState.value.copy(isLoading = false, projects = projects)
        }
    }
}
```

### 2. Navigation

Navigation uses Jetpack Navigation with Compose:

```kotlin
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavRoutes.Login.route) {
        composable(NavRoutes.Login.route) {
            LoginScreen(onLogin = { username, password ->
                navController.navigate(NavRoutes.Projects.route)
            })
        }
        composable(NavRoutes.Projects.route) {
            ProjectsScreen(onProjectClick = { project ->
                navController.navigate(NavRoutes.ProjectDetails.createRoute(project.id))
            })
        }
    }
}
```

### 3. Error Handling

All ViewModels handle errors consistently:

```kotlin
.onFailure { e ->
    val errorMessage = when (e) {
        is ValidationException -> e.message
        is NetworkException -> "Network error: ${e.message}"
        else -> "Operation failed: ${e.message}"
    }
    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
}
```

### 4. Screen Wrappers

Screen wrappers provide data to screens:

```kotlin
@Composable
fun ProjectsScreenWithData(
    uiState: ProjectsUiState,
    onAddProject: () -> Unit,
    onProjectClick: (ProjectUi) -> Unit,
    onDeleteProject: (ProjectUi) -> Unit
) {
    ProjectsScreen(
        projects = uiState.projects,
        onAddProject = onAddProject,
        onProjectClick = onProjectClick,
        onDeleteProject = onDeleteProject
    )
}
```

## Best Practices

1. **Keep screens dumb**: Screens should only handle presentation
2. **Use ViewModel**: All business logic should be in ViewModel
3. **Handle errors**: Always handle errors in ViewModel
4. **Use state**: Use StateFlow for all state management
5. **Document components**: Add documentation for all public components
6. **Test UI**: Write UI tests for critical screens

## Testing

UI layer should be tested with:

- **Unit tests**: For ViewModels
- **UI tests**: For screens and components
- **Integration tests**: For navigation flows

## Future Improvements

1. Add more reusable components
2. Improve error handling with better UI feedback
3. Add animations for better user experience
4. Improve accessibility
5. Add dark theme support

## Known Issues

The project currently has some compilation errors that need to be addressed:

1. **DomainMappers.kt**: Unresolved references to 'firstName' and 'secondName'
2. **ProjectRepositoryImpl.kt**: Type mismatches between String and TaskUrgency/TaskComplexity enums
3. **AuthViewModel.kt**: Syntax errors in when expressions

These issues are related to the KSP compilation problems and will be resolved once the KSP configuration is fixed.