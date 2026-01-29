# Data слой

## Назначение
Реализует контракты репозиториев: получает данные из REST API, кеширует и при необходимости сохраняет в Room, преобразуя DTO в доменные модели.

## Стек
- Retrofit + Moshi для HTTP и сериализации
- OkHttp Logging Interceptor
- Room с конвертерами дат и enum
- Hilt для DI
- Kotlin coroutines
- TokenManager + AuthInterceptor для Bearer-токена
- Встроенный in-memory CacheModule с TTL 5 минут

## Структура
- dao/ — DAO для Room.
- database/ — `PlannerDatabase` с сущностями пользователей, проектов, задач, файлов, трекинга.
- dto/ — транспортные модели для auth, проекта, задачи, профиля, сотрудников, репозитория, трекинга.
- mapper/ — преобразование DTO ↔ доменные модели.
- model/ — Room-сущности и enum для проекта, задач, файлов, пользователей, трекинга.
- network/ — `ApiService` (эндпоинты), `AuthInterceptor`, `TokenManager`, `MoshiAdapters`.
- repository/ — реализации: AuthRepositoryImpl, ProjectRepositoryImpl (domain), TaskRepositoryImpl, UserRepositoryImpl, ProjectRepositoryLegacy (работает с RepoRepository и файловым API).
- di/ — CacheModule с кешем проектов/задач/участников.

## Основные потоки данных
1) UI → use case → репозиторий (domain) → реализация в data → `ApiService` / Room.
2) Ответы преобразуются мапперами в доменные модели и возвращаются как `Result<T>`.
3) TokenManager сохраняет токен, AuthInterceptor добавляет его ко всем запросам.
4) Room используется для локальных сущностей; `fallbackToDestructiveMigration()` включён для простой миграции.

## Работа с файлами
API поддерживает загрузку/скачивание/удаление файлов задач и репозитория через Multipart и `ResponseBody` (см. `ApiService`).

## Тестирование
Юнит-тесты мапперов и взаимодействия репозиториев находятся в `app/src/test/java/com/example/planner/data`.
