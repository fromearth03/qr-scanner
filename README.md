# QR Code Scanner

A feature-rich Java Swing desktop application for scanning and decoding QR codes with a modern, animated user interface. Built with ZXing and Webcam Capture libraries.

![Java](https://img.shields.io/badge/Java-11%2B-blue)
![Maven](https://img.shields.io/badge/Maven-3.6%2B-red)
![License](https://img.shields.io/badge/License-MIT-green)

## ✨ Features

### 🎥 Live Camera Integration
- Real-time webcam video feed with smooth rendering
- Automatic aspect ratio preservation
- Camera permission handling
- Multiple camera support

### 🔍 Universal QR Code Support
- **URLs/Websites** - Open links in default browser
- **WiFi Credentials** - Display network name, password, and security type
- **Contact Information** - Parse VCard and MECARD formats
- **SMS Messages** - Compose SMS with pre-filled content
- **Email Addresses** - Open email client
- **Phone Numbers** - Dial numbers
- **Geographic Coordinates** - Open locations in maps
- **Plain Text** - Display any text content

### 🎨 Animated User Interface
- Gradient backgrounds with smooth color transitions
- Rounded, styled buttons with hover effects
- Visual QR code highlighting with green bounding box
- Animated detection feedback
- Smooth panel transitions
- Scanning line animation

### 🔔 Interactive Feedback
- Visual bounding box with corner markers
- "QR Code Detected!" animated message
- Sound feedback (beep) on successful scan
- Copy-to-clipboard functionality
- Type-specific action buttons

### 🛡️ Robust Error Handling
- Camera availability checks
- Permission denial handling
- Graceful failure recovery
- User-friendly error messages
- Comprehensive logging

## 🚀 Quick Start

```bash
# Clone the repository
git clone https://github.com/fromearth03/qr-scanner.git
cd qr-scanner

# Build the application
mvn clean package

# Run the application
java -jar target/qr-scanner-1.0.0-jar-with-dependencies.jar
```

Or use the provided launcher:
```bash
./run.sh
```

**See [QUICKSTART.md](QUICKSTART.md) for detailed installation and usage instructions.**

## 📋 Requirements

- Java 11 or higher
- Maven 3.6 or higher (for building)
- Webcam or built-in camera
- Operating System: Windows, macOS, or Linux

## 🏗️ Architecture

```
com.qrscanner
├── QRScannerApp.java          # Application entry point
├── ui/
│   └── MainFrame.java         # Main UI with animations
├── camera/
│   └── CameraManager.java     # Webcam management
├── decoder/
│   └── QRDecoder.java         # QR detection & decoding
└── handlers/
    └── QRActionHandler.java   # Type-specific actions
```

## 🔧 Technologies

- **UI Framework**: Java Swing
- **QR Decoding**: ZXing (Zebra Crossing) 3.5.2
- **Camera Access**: Webcam Capture 0.3.12
- **Logging**: SLF4J 1.7.36
- **Build Tool**: Apache Maven

## 📚 Documentation

- **[QUICKSTART.md](QUICKSTART.md)** - Quick start guide and basic usage
- **[FEATURES.md](FEATURES.md)** - Detailed feature documentation
- **[TESTING.md](TESTING.md)** - Testing guidelines and verification matrix

## 🎯 Usage

1. **Launch** the application
2. **Click "Start Scanning"** to activate the camera
3. **Point camera** at a QR code
4. **View results** in the decoded content area
5. **Take action** using the provided buttons (Open, Copy, etc.)
6. **Click "Stop Scanning"** when finished

## 📸 Screenshots

### Main Interface
The application features a clean, modern interface with:
- Blue gradient control panel
- Live camera feed display
- Results panel with action buttons

### QR Detection
When a QR code is detected:
- Green bounding box appears
- Corner markers highlight the code
- Decoded content is displayed
- Relevant action buttons appear

## 🔒 Privacy & Security

- All processing is done locally on your device
- No data is sent to external servers
- Camera access is only active when scanning
- Camera is properly released when not in use

## 🐛 Troubleshooting

### Camera Not Found
```
✗ Issue: "No camera detected"
✓ Solution: Check camera connection, close other camera apps, restart application
```

### QR Not Detected
```
✗ Issue: QR code not being recognized
✓ Solution: Ensure good lighting, hold steady, adjust distance (6-12 inches)
```

### Application Won't Start
```
✗ Issue: Application fails to launch
✓ Solution: Verify Java 11+ installed, rebuild with 'mvn clean package'
```

**See [TESTING.md](TESTING.md) for comprehensive troubleshooting guide.**

## 🛠️ Building from Source

### Compile Only
```bash
mvn clean compile
```

### Package JAR
```bash
mvn clean package
```

### Run with Maven
```bash
mvn exec:java -Dexec.mainClass="com.qrscanner.QRScannerApp"
```

### View Dependency Tree
```bash
mvn dependency:tree
```

## 📦 Dependencies

The application uses the following open-source libraries:

| Library | Version | Purpose |
|---------|---------|---------|
| ZXing Core | 3.5.2 | QR code decoding engine |
| ZXing JavaSE | 3.5.2 | Java SE extensions for ZXing |
| Webcam Capture | 0.3.12 | Cross-platform webcam access |
| SLF4J API | 1.7.36 | Logging facade |
| SLF4J Simple | 1.7.36 | Simple logging implementation |

All dependencies are automatically downloaded by Maven during build.

## 🤝 Contributing

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is open source and available under the MIT License.

## 🙏 Acknowledgments

- [ZXing](https://github.com/zxing/zxing) - Multi-format 1D/2D barcode image processing library
- [Webcam Capture](https://github.com/sarxos/webcam-capture) - Webcam access library for Java
- [SLF4J](http://www.slf4j.org/) - Simple Logging Facade for Java

## 📧 Support

For bugs, questions, or feature requests:
- Open an issue on GitHub
- Check existing documentation in the docs/ folder
- Review the troubleshooting section in [TESTING.md](TESTING.md)

---

**Made with ❤️ using Java and Swing**
