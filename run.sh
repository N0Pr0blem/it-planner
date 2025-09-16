#!/bin/bash

echo "🔨 Останавливаю предыдущую версию..."
sudo docker-compose stop
echo "🐳 Запускаю Docker Compose..."
sudo docker-compose up --build
echo "🚀 Запуск прошел успешно"
sudo docker ps
echo "💤 Ожидаю 10 секунд для вывода логов"
sleep 5
sudo docker logs it_planner-backend-1
echo "🥳 Готово"
