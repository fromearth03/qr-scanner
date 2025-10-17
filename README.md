# QR Code Scanner

A Java Swing desktop application for scanning QR codes with an animated, visually appealing user interface.

## Features

- **Live Camera Feed**: Display live video feed from your webcam
- **Real-time QR Detection**: Continuously scan for QR codes with visual feedback
- **Universal QR Support**: Handle all major QR code types:
  - URLs/Websites - Open links in your default browser
  - WiFi Credentials - Display network details
  - Contact Information (VCard/MECARD) - View contact details
  - SMS/Phone Numbers - Compose SMS or dial numbers
  - Email Addresses - Compose emails
  - Plain Text - Display content
  - Geographic Coordinates - Open locations in maps

- **Interactive UI**:
  - Animated gradient backgrounds
  - Visual QR code highlighting with green bounding box
  - Sound feedback on detection
  - Smooth animations and transitions
  - Copy-to-clipboard functionality

- **Error Handling**:
  - Camera permission and availability checks
  - Clear error messages and user guidance

## Requirements

- Java 11 or higher
- Webcam/Camera device
- Maven (for building)

## Dependencies

- ZXing (QR code decoding)
- Webcam Capture (camera access)
- SLF4J (logging)

## Building

```bash
mvn clean compile
```

## Running

```bash
mvn exec:java -Dexec.mainClass="com.qrscanner.QRScannerApp"
```

Or build a JAR with dependencies:

```bash
mvn clean package
java -jar target/qr-scanner-1.0.0-jar-with-dependencies.jar
```

## Usage

1. Launch the application
2. Click "Start Scanning" to begin
3. Point your camera at a QR code
4. The application will detect and decode the QR code
5. View the decoded content and use the action buttons
6. Click "Stop Scanning" when finished

## Architecture

- `QRScannerApp.java` - Main application entry point
- `ui/MainFrame.java` - Main UI frame with animated components
- `camera/CameraManager.java` - Webcam management
- `decoder/QRDecoder.java` - QR code detection and decoding
- `handlers/QRActionHandler.java` - Action handlers for different QR types

## License

This project is open source and available under the MIT License.
