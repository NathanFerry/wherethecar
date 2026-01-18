.PHONY: help build run release test clean dev docker-build docker-run

.DEFAULT_GOAL := help

help:
	@echo "Available commands:"
	@echo "  make build        - Build the project in debug mode"
	@echo "  make release      - Build the project in release mode"
	@echo "  make run          - Run the project in development mode"
	@echo "  make dev          - Run with auto-reload (requires cargo-watch)"
	@echo "  make test         - Run tests"
	@echo "  make clean        - Clean build artifacts"
	@echo "  make docker-build - Build Docker image"
	@echo "  make docker-run   - Run Docker container"

build:
	cargo build

release:
	cargo build --release

run:
	cargo run

dev:
	cargo watch -x run

test:
	cargo test

clean:
	cargo clean
	rm -rf storage/

docker-build:
	docker build -t file-api:latest .

docker-run:
	docker run -d \
		--name file-api \
		-p 8080:8080 \
		-v $(PWD)/storage:/app/storage \
		file-api:latest
