#!/bin/bash

echo "🔨 Останавливаю предыдущую версию..."
sudo docker-compose stop
echo "🐳 Запускаю Docker Compose..."
sudo docker-compose up --build -d
echo "🚀 Запуск прошел успешно"
sudo docker ps
