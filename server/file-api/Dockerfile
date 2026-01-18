# Build stage
FROM rust:1.82-slim as builder

WORKDIR /app

# Install build dependencies
RUN apt-get update && apt-get install -y \
    pkg-config \
    libssl-dev \
    && rm -rf /var/lib/apt/lists/*

# Copy manifest files
COPY Cargo.toml ./

# Create a dummy main.rs to cache dependencies
RUN mkdir src && \
    echo "fn main() {}" > src/main.rs && \
    cargo build --release && \
    rm -rf src

# Copy source code
COPY src ./src

# Build the application
RUN touch src/main.rs && \
    cargo build --release

# Runtime stage
FROM debian:bookworm-slim

WORKDIR /app

# Install runtime dependencies
RUN apt-get update && apt-get install -y \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Copy the binary from builder
COPY --from=builder /app/target/release/file-api /app/file-api

# Create storage directories
RUN mkdir -p /app/storage/pdf /app/storage/pictures

# Set environment variables
ENV FILE_API_HOST=0.0.0.0
ENV FILE_API_PORT=8080
ENV PDF_STORAGE_PATH=/app/storage/pdf
ENV PICTURE_STORAGE_PATH=/app/storage/pictures
ENV RUST_LOG=info

# Expose port
EXPOSE 8080

# Run the binary
CMD ["/app/file-api"]
