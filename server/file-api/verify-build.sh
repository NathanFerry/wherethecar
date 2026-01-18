#!/bin/bash
# Build verification script

set -e

echo "=== File API Build Verification ==="
echo ""

cd /home/nathan/Workspace/wherethecar/server/file-api

echo "1. Checking Rust installation..."
if ! command -v cargo &> /dev/null; then
    echo "✗ Cargo not found. Install Rust from https://rustup.rs/"
    exit 1
fi
echo "✓ Cargo found: $(cargo --version)"
echo ""

echo "2. Building debug version..."
cargo build 2>&1 | tail -5
if [ -f target/debug/file-api ]; then
    echo "✓ Debug binary built successfully"
    ls -lh target/debug/file-api
else
    echo "✗ Debug binary not found"
    exit 1
fi
echo ""

echo "3. Building release version..."
cargo build --release 2>&1 | tail -5
if [ -f target/release/file-api ]; then
    echo "✓ Release binary built successfully"
    ls -lh target/release/file-api
else
    echo "✗ Release binary not found"
    exit 1
fi
echo ""

echo "4. Checking file structure..."
for dir in storage/pdf storage/pictures; do
    if [ -d "$dir" ]; then
        echo "✓ $dir exists"
    else
        echo "! $dir missing (will be created on first run)"
    fi
done
echo ""

echo "5. Checking configuration..."
if [ -f .env ]; then
    echo "✓ .env file exists"
    echo "  Configuration:"
    grep -E "^[^#]" .env | sed 's/^/    /'
else
    echo "! .env not found, will use defaults"
fi
echo ""

echo "=== Build Verification Complete ==="
echo ""
echo "To run the API:"
echo "  Development:  cargo run"
echo "  Production:   ./target/release/file-api"
echo "  Docker:       cd .. && make up"
echo ""
echo "API will be available at: http://localhost:8080"
