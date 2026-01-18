# File API

A lightweight Rust-based REST API for serving PDF documents and images for the WhereTheCar vehicle management system.

## Overview

This API provides secure file storage and retrieval for:
- PDF documents (maintenance documents)
- Images (vehicle pictures)

Files are stored in the filesystem with UUIDs as filenames, keeping the database clean and efficient.

## Features

- **Simple REST API**: Two endpoints for retrieving files
- **UUID-based storage**: Files are stored with their UUID as the filename
- **Automatic MIME type detection**: Proper content-type headers for images
- **Multiple format support**: 
  - PDFs: `.pdf`
  - Images: `.png`, `.jpg`, `.jpeg`, `.gif`, `.webp`, `.bmp`
- **Health check endpoint**: For monitoring and load balancer integration

## API Endpoints

### Health Check
```
GET /health
```
Returns the service status.

**Response:**
```json
{
  "status": "ok",
  "service": "file-api"
}
```

### Get PDF Document
```
GET /pdf/{uuid}
```
Retrieves a PDF document by its UUID.

**Parameters:**
- `uuid` (path): The UUID of the PDF document

**Response:**
- `200 OK`: Returns the PDF file with `Content-Type: application/pdf`
- `404 Not Found`: PDF not found
- `500 Internal Server Error`: Server error

**Example:**
```bash
curl http://localhost:8080/pdf/550e8400-e29b-41d4-a716-446655440000
```

### Get Picture/Image
```
GET /picture/{uuid}
```
Retrieves an image by its UUID.

**Parameters:**
- `uuid` (path): The UUID of the image

**Response:**
- `200 OK`: Returns the image file with appropriate MIME type
- `404 Not Found`: Image not found
- `500 Internal Server Error`: Server error

**Example:**
```bash
curl http://localhost:8080/picture/550e8400-e29b-41d4-a716-446655440000
```

## Configuration

Configuration is done through environment variables. Copy `.env.example` to `.env` and adjust as needed:

```bash
cp .env.example .env
```

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `FILE_API_HOST` | `0.0.0.0` | Host address to bind to |
| `FILE_API_PORT` | `8080` | Port to listen on |
| `PDF_STORAGE_PATH` | `./storage/pdf` | Directory for PDF storage |
| `PICTURE_STORAGE_PATH` | `./storage/pictures` | Directory for image storage |

## Installation

### Prerequisites

- Rust 1.70 or higher
- Cargo (comes with Rust)

### Build

```bash
# Development build
cargo build

# Production build (optimized)
cargo build --release
```

## Running

### Development Mode

```bash
# With default configuration
cargo run

# With custom environment variables
FILE_API_PORT=3000 cargo run
```

### Production Mode

```bash
# Run the optimized binary
./target/release/file-api
```

### Using systemd (Linux)

Create a systemd service file at `/etc/systemd/system/file-api.service`:

```ini
[Unit]
Description=WhereTheCar File API
After=network.target

[Service]
Type=simple
User=www-data
WorkingDirectory=/opt/file-api
EnvironmentFile=/opt/file-api/.env
ExecStart=/opt/file-api/target/release/file-api
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
```

Then:
```bash
sudo systemctl daemon-reload
sudo systemctl enable file-api
sudo systemctl start file-api
```

## Storage Structure

Files are stored in the following structure:

```
storage/
├── pdf/
│   ├── 550e8400-e29b-41d4-a716-446655440000.pdf
│   └── ...
└── pictures/
    ├── 123e4567-e89b-12d3-a456-426614174000.png
    ├── 123e4567-e89b-12d3-a456-426614174001.jpg
    └── ...
```

## File Upload

This API only handles file retrieval. File uploads should be handled by your main application, which should:

1. Generate a UUID for the file
2. Save the file to the appropriate storage directory with the UUID as filename
3. Store the UUID in the database along with the original filename
4. The file extension should match the actual file type

## Development

### Running Tests

```bash
cargo test
```

### Enable Debug Logging

```bash
RUST_LOG=debug cargo run
```

### Format Code

```bash
cargo fmt
```

### Lint

```bash
cargo clippy
```

## Docker Deployment

### Build Docker Image

```bash
docker build -t file-api:latest .
```

### Run with Docker

```bash
docker run -d \
  --name file-api \
  -p 8080:8080 \
  -v $(pwd)/storage:/app/storage \
  -e FILE_API_PORT=8080 \
  file-api:latest
```

## Performance

The API is built with Actix-web, one of the fastest web frameworks in any language. It can handle thousands of requests per second on modest hardware.

## Security Considerations

- Files are accessed by UUID only, preventing directory traversal attacks
- No file listing endpoint exists
- Consider adding authentication/authorization for production use
- Consider rate limiting for production deployments
- Use HTTPS in production (via reverse proxy like nginx)

## Troubleshooting

### Port Already in Use

Change the port in `.env`:
```
FILE_API_PORT=8081
```

### Permission Denied on Storage Directory

Ensure the user running the service has read/write permissions:
```bash
sudo chown -R $USER:$USER storage/
chmod -R 755 storage/
```

### File Not Found

- Verify the UUID is correct
- Check the file exists in the storage directory
- Verify file extension matches one of the supported formats

## License

Part of the WhereTheCar project.
