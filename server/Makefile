	@echo "  make up         - Run the Docker Compose stack"
	@echo "  make down       - Stop the containers"
	@echo "  make clean      - Clean up containers and volumes"
	@echo "  make clean-all  - Clean everything including images"
	@echo "  make logs       - View logs"
	@echo "  make restart    - Restart containers"
	@echo "  make db-create  - Create the database"
	@echo "  make db-drop    - Drop the database"
	@echo "  make db-populate- Populate the database with initial data"
	@echo "  make db-wype    - Wipe the database"
.PHONY: help up down clean logs restart clean-all

.DEFAULT_GOAL := help

include .env
include ./database/database.mk

help:
	@echo "Available commands:"
	@echo "  make up         - Run the Docker Compose stack"
	@echo "  make down       - Stop the containers"
	@echo "  make clean      - Clean up containers and volumes"
	@echo "  make clean-all  - Clean everything including images"
	@echo "  make logs       - View logs"
	@echo "  make restart    - Restart containers"
	@echo "  make db-create  - Create the database"
	@echo "  make db-drop    - Drop the database"
	@echo "  make db-wype    - Wipe the database"

up:
	docker compose up -d
	@echo "PostgreSQL container started on port $(DATABASE_PORT)"
	@echo "Database: $(DATABASE_NAME)"
	@echo "User: $(DATABASE_USER)"
	@echo "Password: $(DATABASE_PASSWORD)"

down:
	docker compose down

clean:
	docker compose down -v
	@echo "Containers and volumes removed"

clean-all:
	docker compose down -v --rmi all
	@echo "Everything removed"

logs:
	docker compose logs -f

restart: down up

