# Quick Start Guide

## What Was Built

A lightweight Rust-based REST API for serving PDF documents and images for the WhereTheCar vehicle management system.

## Features

✅ **Two simple endpoints:**
- `GET /pdf/{uuid}` - Retrieve PDF documents
- `GET /picture/{uuid}` - Retrieve images (png, jpg, jpeg, gif, webp, bmp)
- `GET /health` - Health check

✅ **UUID-based storage** - Files named with UUIDs for security  
✅ **Automatic MIME type detection**  
✅ **Fast and efficient** - Built with Actix-web  
✅ **Docker ready** - Includes Dockerfile and docker-compose integration  
✅ **Production ready** - Optimized release builds  

## Quick Start

### 1. Run Standalone (Development)

```bash
cd /home/nathan/Workspace/wherethecar/server/file-api

# Copy environment config
cp .env.example .env

# Build and run
cargo run

# Server starts on http://localhost:8080
```

### 2. Run with Docker Compose (Production)

```bash
cd /home/nathan/Workspace/wherethecar/server

# Start all services (database + file-api)
make up

# View logs
make logs

# Stop services
make down
```

The File API will be available at `http://localhost:8080`

## Testing

### Manual Test

```bash
# Health check
curl http://localhost:8080/health

# Expected: {"status":"ok","service":"file-api"}
```

### Create Test Files

```bash
# Generate a UUID
TEST_UUID=$(uuidgen | tr '[:upper:]' '[:lower:]')

# Create test PDF
echo "Test PDF content" > storage/pdf/$TEST_UUID.pdf

# Create test image (requires ImageMagick)
convert -size 100x100 xc:blue storage/pictures/$TEST_UUID.png

# Retrieve them
curl http://localhost:8080/pdf/$TEST_UUID > downloaded.pdf
curl http://localhost:8080/picture/$TEST_UUID > downloaded.png
```

### Automated Test

```bash
./test-api.sh
```

## Integration with Your Java App

See [INTEGRATION.md](INTEGRATION.md) for detailed integration guide with your Java application.

### Quick Example

```java
FileApiService fileService = new FileApiService("http://localhost:8080");

// Upload a picture
UUID pictureUuid = fileService.uploadPicture(uploadedFile, "my-car.jpg");

// Store in database
picture.setUuid(pictureUuid);
picture.setName("my-car.jpg");
pictureRepository.save(picture);

// Later, retrieve it
String imageUrl = fileService.getPictureUrl(pictureUuid);
// Use in UI: imageView.setImage(new Image(imageUrl));
```

## File Storage Structure

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

## Configuration

Edit `.env` file:

```bash
FILE_API_HOST=0.0.0.0
FILE_API_PORT=8080
PDF_STORAGE_PATH=./storage/pdf
PICTURE_STORAGE_PATH=./storage/pictures
```

## Common Commands

```bash
# Development
cargo run                    # Run in debug mode
cargo build --release        # Build optimized binary
cargo test                   # Run tests
RUST_LOG=debug cargo run     # Enable debug logging

# Docker
docker-compose up -d file-api        # Start only file-api
docker-compose logs -f file-api      # View logs
docker-compose restart file-api      # Restart service
make file-api-build                  # Rebuild container

# Production
./target/release/file-api    # Run optimized binary
```

## Troubleshooting

### Server won't start

Check if port 8080 is available:
```bash
lsof -i :8080
# If in use, change port in .env
```

### File not found (404)

1. Check the file exists: `ls storage/pdf/` or `ls storage/pictures/`
2. Verify UUID is correct
3. Check file extension matches supported types

### Permission denied

```bash
# Fix storage directory permissions
chmod -R 755 storage/
```

## Next Steps

1. ✅ **Done:** File API is built and ready to use
2. **TODO:** Integrate with your Java application using the examples in INTEGRATION.md
3. **TODO:** Update your database schema to store file UUIDs
4. **TODO:** Implement file upload in your Java application
5. **TODO:** Deploy to production server

## Documentation

- [README.md](README.md) - Full API documentation
- [INTEGRATION.md](INTEGRATION.md) - Java integration guide
- [Dockerfile](Dockerfile) - Docker configuration
- [docker-compose.yml](../docker-compose.yml) - Complete stack

## Performance

The API is extremely fast:
- Written in Rust with Actix-web (one of the fastest web frameworks)
- Zero-copy file serving
- Minimal memory footprint
- Can handle thousands of requests per second

## Security Notes

- Files are accessed by UUID only (no directory traversal possible)
- No file listing endpoint
- Add authentication in production (via reverse proxy recommended)
- Use HTTPS in production (nginx/Caddy recommended)

## Support

For issues or questions:
1. Check the logs: `docker-compose logs file-api` or console output
2. Review [INTEGRATION.md](INTEGRATION.md) for Java integration help
3. Test with `curl` commands to isolate issues

---

**You're all set!** The File API is ready to handle your vehicle pictures and maintenance documents. 🚗📄🖼️
