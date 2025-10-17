#!/bin/bash

# QR Scanner Launcher Script

echo "Starting QR Code Scanner..."
echo "Building application..."

mvn clean package -q

if [ $? -eq 0 ]; then
    echo "Build successful!"
    echo "Launching application..."
    java -jar target/qr-scanner-1.0.0-jar-with-dependencies.jar
else
    echo "Build failed. Please check the error messages above."
    exit 1
fi
