.PHONY: db-create

db-create:
	@echo "Creating the database..."
	psql postgresql://$(DATABASE_USER):$(DATABASE_PASSWORD)@localhost:$(DATABASE_PORT)/$(DATABASE_NAME) -f ./database/create.sql
	@echo "Database created successfully."

db-drop:
	@echo "Dropping database's tables and types..."
	psql postgresql://$(DATABASE_USER):$(DATABASE_PASSWORD)@localhost:$(DATABASE_PORT)/$(DATABASE_NAME) -f ./database/drop.sql
	@echo "Database dropped successfully."

db-populate:
	@echo "Populating the database with initial data..."
	psql postgresql://$(DATABASE_USER):$(DATABASE_PASSWORD)@localhost:$(DATABASE_PORT)/$(DATABASE_NAME) -f ./database/populate.sql
	@echo "Database populated successfully."

db-wype:
	@echo "Wiping the database..."
	psql postgresql://$(DATABASE_USER):$(DATABASE_PASSWORD)@localhost:$(DATABASE_PORT)/$(DATABASE_NAME) -f ./database/drop.sql
	psql postgresql://$(DATABASE_USER):$(DATABASE_PASSWORD)@localhost:$(DATABASE_PORT)/$(DATABASE_NAME) -f ./database/create.sql
	@echo "Database wiped successfully."