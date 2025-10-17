# QR Scanner - Quick Start Guide

## Installation

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher  
- A webcam or built-in camera

### Verify Java Installation
```bash
java -version
# Should show Java 11 or higher
```

### Verify Maven Installation
```bash
mvn -version
# Should show Maven 3.6 or higher
```

## Quick Start (3 Steps)

### Step 1: Clone the Repository
```bash
git clone https://github.com/fromearth03/qr-scanner.git
cd qr-scanner
```

### Step 2: Build the Application
```bash
mvn clean package
```

This will:
- Download all dependencies (ZXing, Webcam Capture, SLF4J)
- Compile the Java source files
- Create a JAR file with all dependencies

### Step 3: Run the Application
```bash
java -jar target/qr-scanner-1.0.0-jar-with-dependencies.jar
```

Or use the provided script:
```bash
./run.sh
```

## First Time Use

1. **Launch Application**: The QR Scanner window will open
2. **Grant Camera Permission**: Allow camera access if prompted
3. **Start Scanning**: Click the green "Start Scanning" button
4. **Scan a QR Code**: Point your camera at any QR code
5. **View Results**: See the decoded content and available actions
6. **Take Action**: Click action buttons (Open Link, Copy, etc.)
7. **Stop Scanning**: Click the red "Stop Scanning" button when done

## Example QR Codes to Test

### URL
Create a QR code with: `https://www.google.com`

### WiFi Network
Create a QR code with: `WIFI:T:WPA;S:TestNetwork;P:password123;H:false;;`

### Contact (MECARD)
Create a QR code with: `MECARD:N:John Doe;TEL:+1234567890;EMAIL:john@example.com;;`

### Email
Create a QR code with: `mailto:test@example.com`

### SMS
Create a QR code with: `sms:+1234567890:Hello World`

### Phone
Create a QR code with: `tel:+1234567890`

### Location
Create a QR code with: `geo:37.7749,-122.4194`

### Plain Text
Create a QR code with: `This is a test message!`

## Online QR Code Generators

You can create test QR codes at:
- https://www.qr-code-generator.com/
- https://www.the-qrcode-generator.com/
- https://qr.io/

## Troubleshooting

### "No camera found"
- Check if your webcam is connected
- Close other applications using the camera
- Try restarting the application

### "Permission denied"
- Grant camera access when prompted
- Check system privacy settings

### Application won't start
- Verify Java 11+ is installed: `java -version`
- Check if JAR file exists in target/ directory
- Try rebuilding: `mvn clean package`

### QR code not detected
- Ensure good lighting
- Hold QR code 6-12 inches from camera
- Keep camera and QR steady
- Try different angles

## UI Overview

```
┌─────────────────────────────────────────────────────┐
│  Control Panel (Blue Gradient)                      │
│  [Start Scanning]  [Stop Scanning]  Status: Ready   │
├─────────────────────────────────────────────────────┤
│                                                      │
│              Camera Feed Area                       │
│         (640x480 live video)                        │
│                                                      │
│         ╔═══════════════════╗                       │
│         ║   QR Code Here    ║  ← Green box when     │
│         ║                   ║     detected          │
│         ╚═══════════════════╝                       │
│                                                      │
├─────────────────────────────────────────────────────┤
│  Results Panel                                      │
│  ┌───────────────────────────────────────────────┐ │
│  │ Decoded Content:                              │ │
│  │ https://www.example.com                       │ │
│  └───────────────────────────────────────────────┘ │
│                                                      │
│  [Copy to Clipboard]  [Open Link]                  │
└─────────────────────────────────────────────────────┘
```

## Features at a Glance

✓ Real-time QR code detection  
✓ Visual highlighting with green box  
✓ Sound feedback on detection  
✓ Animated, gradient UI  
✓ Support for 8+ QR code types  
✓ One-click actions (open, copy, etc.)  
✓ Comprehensive error handling  
✓ Cross-platform (Windows, Mac, Linux)  

## Advanced Usage

### Build JAR Only
```bash
mvn package
```

### Run with Maven
```bash
mvn exec:java -Dexec.mainClass="com.qrscanner.QRScannerApp"
```

### Clean Build
```bash
mvn clean
```

### View Dependencies
```bash
mvn dependency:tree
```

## File Locations

- **Source Code**: `src/main/java/com/qrscanner/`
- **JAR File**: `target/qr-scanner-1.0.0-jar-with-dependencies.jar`
- **Dependencies**: Downloaded to `~/.m2/repository/`

## Next Steps

- Read [FEATURES.md](FEATURES.md) for detailed feature documentation
- Read [TESTING.md](TESTING.md) for testing guidelines
- Explore the source code in `src/main/java/`

## Support

For issues or questions:
1. Check [TESTING.md](TESTING.md) troubleshooting section
2. Review [FEATURES.md](FEATURES.md) for detailed usage
3. Check application logs (console output)
4. Open an issue on GitHub

## Version

**Version**: 1.0.0  
**Java**: 11+  
**Build Tool**: Maven 3.6+  

---

**Happy Scanning! 📷**
