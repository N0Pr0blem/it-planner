# Domain слой

## Назначение
Чистая бизнес-логика IT Planner без зависимостей от Android: описывает сущности, контракты репозиториев и use case, через которые UI обращается к данным.

## Структура
- exception/ — доменные исключения для валидации, авторизации, сети и запретных операций.
- model/ — сущности: User, UserProfile, Project, Task, TaskStatus/Urgency/Complexity, TrackingRecord/Summary, RepoFile, ProjectMember/Role, SimpleMessage и др.
- repository/ — интерфейсы AuthRepository, UserRepository, ProjectRepository, TaskRepository, RepoRepository.
- usecase/ — набор сценариев, сгруппированных по доменам.

## Ключевые use case
- Аутентификация: Login, Register, Verify, ResendVerification, Logout, IsLoggedIn, GetCurrentUserId.
- Профиль: GetProfile, UpdateProfile.
- Проекты: GetProjects, GetProject, CreateProject, DeleteProject.
- Задачи: GetProjectTasks, GetTask, CreateTask, UpdateTask, DeleteTask, AssignTask.
- Трекинг времени и файлы задач: GetTaskDetails, GetTaskFiles, UploadTaskFile, DownloadTaskFile, DeleteTaskFile, GetTaskTracking, CreateTracking, DeleteTracking.
- Участники: GetProjectEmployees, InviteEmployee, DeleteEmployee, UpdateEmployeeRole.
- Репозиторий проекта: GetRepoFiles, UploadRepoFile, DownloadRepoFile, DeleteRepoFile.

## Принципы
- Входные данные валидируются в use case, ошибки возвращаются как `Result<T>` с доменными исключениями.
- Все классы — чистый Kotlin: легко тестировать без Android фреймворка.

## Тестирование
Юнит-тесты use case и мапперов находятся в `app/src/test/java/com/example/planner` (папки `domain` и `ui/viewmodel`).
