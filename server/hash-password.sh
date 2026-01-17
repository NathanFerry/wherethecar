#!/bin/bash

if [ -z "$1" ]; then
    echo "Usage: ./hash-password.sh <password>"
    echo ""
    echo "Example hashes:"
    echo "  password -> 5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"
    echo "  admin    -> 8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918"
    exit 1
fi

PASSWORD="$1"
HASH=$(echo -n "$PASSWORD" | sha256sum | cut -d' ' -f1)

echo "Password: $PASSWORD"
echo "SHA-256:  $HASH"
