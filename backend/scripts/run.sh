#!/bin/batch

echo "🔨 Останавливаю предыдущую версию..."
docker-compose stop
echo "🐳 Запускаю Docker Compose..."
docker-compose up --build -d
echo "🚀 Запуск прошел успешно"
docker ps
echo "💤 Ожидаю 10 секунд для вывода логов"
sleep 10
docker logs it_planner-backend-1
echo "🥳 Готово"
