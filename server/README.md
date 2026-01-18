# Server
This is the server part. It will not be run at application start and need to be run separately.  
You can deploy it on other servers.

## Components

### PostgreSQL Database
The main database for storing vehicle, maintenance, reservation, and agent data.

### File API (Rust)
A lightweight REST API for serving files (PDFs and images) stored in the filesystem.
- **PDF Endpoint**: `GET /pdf/{uuid}`
- **Picture Endpoint**: `GET /picture/{uuid}`
- **Health Check**: `GET /health`

See [file-api/README.md](file-api/README.md) for detailed documentation.

## Requirements
- docker & docker-compose
- postgresql-client (sudo apt install postgresql-client)

## Quick Start

```bash
# Start all services (database + file-api)
make up

# View logs
make logs

# Stop all services
make down
```

The File API will be available at `http://localhost:8080`

## File Storage

Files are stored in Docker volumes to persist data across container restarts:
- PDFs: `/app/storage/pdf/` in the container
- Pictures: `/app/storage/pictures/` in the container

Files should be named with their UUID (e.g., `550e8400-e29b-41d4-a716-446655440000.pdf`).
