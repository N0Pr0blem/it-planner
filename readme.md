# Чтоб запустить эту шайтан машину нужно 
1) склонить репу
2) поставить docker/docker-compose
3) создать .env в корне с таким содержанием
```
MINIO_ROOT_USER={желаемый логин}
MINIO_ROOT_PASSWORD={желаемый пароль}

MINIO_ACCESS_KEY=//сюда вернемся позже
MINIO_SECRET_KEY=//сюда вернемся позже
MINIO_ENDPOINT=http://localhost:9000
MINIO_BUCKET_NAME=it-planner

POSTGRES_USER={желаемый логин}
POSTGRES_PASSWORD={желаемый пароль}

JWT_SECRET=b5f59337a612a2a7dc07328f3e7d1a04722967c7f86df20a499a7d3f91ff2a7c
JWT_PASSWORD_SECRET=F2K2DZ82odq$13e8aENggaMbb_fAkl-nJL4AEVBX43g
JWT_EXPIRATION=7200
```
4) запускаем docker-compose
5) после билда заходим на localhost:9001, авторизируемся, создаем бакет it-planner и генерируем Access и Secret ключи
6) вставляем сгенерированные ключи в .env в поля MINIO_ACCESS_KEY и MINIO_SECRET_KEY

# Готово =)