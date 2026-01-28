 Android‑клиент для управления проектами и задачами команды.

  Коротко:

  - Функции: аутентификация/верификация, список проектов, детали проекта (участники, задачи, репозиторий файлов),
    создание/редактирование/назначение задач, трекинг времени, профиль пользователя.
  - Стек: Kotlin, Jetpack Compose, Hilt, Coroutines/Flow, Retrofit + Moshi, OkHttp, Room (складка под кеш), JWT‑токены.
  - Архитектура: Clean Architecture — Domain (use case + модели), Data (DTO, mapper, репозитории), UI (Compose +
    ViewModel). DI модули в Hilt
  - Навигация: Jetpack Navigation Compose.
  - Настройка окружения: BASE_URL в BuildConfig/gradle.properties; токен хранится в TokenManager (SharedPreferences).


  Проект реализован в рамках учебной дисциплины. Бэкенд был написан не мной, но местами правился. 
  Оригинал https://github.com/N0Pr0blem/it-planner
