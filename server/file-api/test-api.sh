#!/bin/bash
# Test script for the File API

set -e

API_URL="${API_URL:-http://localhost:8080}"
TEST_DIR="$(mktemp -d)"

echo "=== File API Test Script ==="
echo "API URL: $API_URL"
echo "Test directory: $TEST_DIR"
echo ""

# Cleanup function
cleanup() {
    echo "Cleaning up test directory..."
    rm -rf "$TEST_DIR"
}
trap cleanup EXIT

# Test 1: Health Check
echo "Test 1: Health Check"
response=$(curl -s -w "\n%{http_code}" "$API_URL/health")
http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | head -n-1)

if [ "$http_code" = "200" ]; then
    echo "✓ Health check passed"
    echo "  Response: $body"
else
    echo "✗ Health check failed (HTTP $http_code)"
    exit 1
fi
echo ""

# Test 2: Test missing file (should return 404)
echo "Test 2: Request non-existent PDF"
test_uuid="00000000-0000-0000-0000-000000000000"
http_code=$(curl -s -o /dev/null -w "%{http_code}" "$API_URL/pdf/$test_uuid")

if [ "$http_code" = "404" ]; then
    echo "✓ Correctly returned 404 for missing PDF"
else
    echo "✗ Expected 404, got HTTP $http_code"
    exit 1
fi
echo ""

# Test 3: Test missing picture (should return 404)
echo "Test 3: Request non-existent picture"
http_code=$(curl -s -o /dev/null -w "%{http_code}" "$API_URL/picture/$test_uuid")

if [ "$http_code" = "404" ]; then
    echo "✓ Correctly returned 404 for missing picture"
else
    echo "✗ Expected 404, got HTTP $http_code"
    exit 1
fi
echo ""

# Test 4: Test invalid UUID format
echo "Test 4: Request with invalid UUID format"
http_code=$(curl -s -o /dev/null -w "%{http_code}" "$API_URL/pdf/not-a-uuid")

if [ "$http_code" = "404" ] || [ "$http_code" = "400" ]; then
    echo "✓ Correctly handled invalid UUID (HTTP $http_code)"
else
    echo "✗ Unexpected response for invalid UUID: HTTP $http_code"
fi
echo ""

echo "=== All tests passed! ==="
echo ""
echo "To test with actual files:"
echo "1. Create storage directories:"
echo "   mkdir -p storage/pdf storage/pictures"
echo ""
echo "2. Add test files with UUID names:"
echo "   # Generate a UUID"
echo "   TEST_UUID=\$(uuidgen | tr '[:upper:]' '[:lower:]')"
echo "   # Create a test PDF"
echo "   echo 'Test PDF content' > storage/pdf/\$TEST_UUID.pdf"
echo "   # Create a test image"
echo "   convert -size 100x100 xc:blue storage/pictures/\$TEST_UUID.png"
echo ""
echo "3. Retrieve the files:"
echo "   curl $API_URL/pdf/\$TEST_UUID > downloaded.pdf"
echo "   curl $API_URL/picture/\$TEST_UUID > downloaded.png"
