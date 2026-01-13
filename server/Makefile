.PHONY: run stop clean logs restart clean-all

# Run the Docker Compose stack
run:
	docker compose up -d
	@echo "PostgreSQL container started on port 5432"
	@echo "Database: wherethecar"
	@echo "User: wherethecar"
	@echo "Password: wherethecar"

# Stop the containers
stop:
	docker compose down

# Clean up containers and volumes
clean:
	docker compose down -v
	@echo "Containers and volumes removed"

# Clean everything including images
clean-all:
	docker compose down -v --rmi all
	@echo "Everything removed"

# View logs
logs:
	docker compose logs -f

# Restart containers
restart: stop run

