# QR Scanner - Feature Demonstration

## Overview
This document provides a detailed walkthrough of the QR Scanner application's features and capabilities.

## User Interface Components

### 1. Control Panel (Top)
- **Gradient Background**: Blue gradient from #2980B9 to #6DD5FA
- **Start Scanning Button**: Green (#2ECC71) with rounded corners and hover effects
- **Stop Scanning Button**: Red (#E74C3C) with rounded corners and hover effects
- **Status Label**: White text showing current application state

### 2. Camera Panel (Center)
- **Background**: Dark blue-gray (#2C3E50)
- **Camera View**: 640x480 live feed with aspect ratio preservation
- **Placeholder**: Shows "Click 'Start Scanning' to begin" when idle
- **Scanning Animation**: Animated scan line when initializing
- **Detection Overlay**: Green bounding box with corner markers when QR detected

### 3. Results Panel (Bottom)
- **Background**: Light gray (#ECF0F1)
- **Content Area**: Scrollable text area with blue border
- **Action Buttons**: Dynamic buttons based on QR code type
- **Smooth Animation**: Slides in when QR is detected

## Animation Features

### Gradient Backgrounds
```java
GradientPaint gradient = new GradientPaint(
    0, 0, new Color(41, 128, 185),
    getWidth(), 0, new Color(109, 213, 250)
);
```

### Rounded Buttons
- 15px border radius
- Hover effect (lighter color)
- Press effect (darker color)
- Hand cursor on hover

### Scanning Animation
- Horizontal scan line moving from top to bottom
- Semi-transparent blue color
- Continuous loop during initialization

### Detection Animation
- Bounding box with 3px green stroke
- Corner markers (20px length, 4px stroke)
- Detection label fade-in

### Results Panel Animation
- Height animates from 0 to 200px
- 10px increments every 10ms
- Smooth slide-in effect

## QR Code Type Handling

### 1. URL/Website
**Detection Pattern**: Starts with `http://`, `https://`, or `www.`

**Example QR Content**:
```
https://www.example.com
```

**Actions**:
- Copy to Clipboard
- Open Link (launches default browser)

**Implementation**: Uses `Desktop.browse(URI)` API

---

### 2. WiFi Network
**Detection Pattern**: Starts with `WIFI:`

**Example QR Content**:
```
WIFI:T:WPA;S:MyNetwork;P:password123;H:false;;
```

**Parsed Fields**:
- T: Security type (WPA, WEP, WPA2, etc.)
- S: SSID (network name)
- P: Password
- H: Hidden network (true/false)

**Actions**:
- Copy to Clipboard
- Show WiFi Details (displays parsed information)

**Display**:
- HTML formatted table
- SSID, Password, Security, Hidden status
- Note about manual connection

---

### 3. Contact Information (VCard)
**Detection Pattern**: Starts with `BEGIN:VCARD`

**Example QR Content**:
```
BEGIN:VCARD
VERSION:3.0
FN:John Doe
TEL:+1234567890
EMAIL:john@example.com
ORG:Example Corp
ADR:123 Main St, City, State
END:VCARD
```

**Parsed Fields**:
- FN: Full name
- TEL: Phone number
- EMAIL: Email address
- ORG: Organization
- ADR: Address

**Actions**:
- Copy to Clipboard
- Show Contact (displays formatted details)

---

### 4. Contact Information (MECARD)
**Detection Pattern**: Starts with `MECARD:`

**Example QR Content**:
```
MECARD:N:John Doe;TEL:+1234567890;EMAIL:john@example.com;;
```

**Parsed Fields**:
- N: Name
- TEL: Telephone
- EMAIL: Email
- ORG: Organization
- ADR: Address

**Actions**: Same as VCard

---

### 5. SMS Message
**Detection Pattern**: Starts with `sms:` or `smsto:`

**Example QR Content**:
```
sms:+1234567890:Hello World
smsto:+1234567890:Message text
```

**Parsed Components**:
- Phone number
- Message body (optional)

**Actions**:
- Copy to Clipboard
- Compose SMS (opens SMS app if supported)

---

### 6. Email Address
**Detection Pattern**: Starts with `mailto:` or contains `@` and `.`

**Example QR Content**:
```
mailto:test@example.com
test@example.com
```

**Actions**:
- Copy to Clipboard
- Compose Email (opens email client)

**Implementation**: Uses `Desktop.mail(URI)` API

---

### 7. Phone Number
**Detection Pattern**: Starts with `tel:` or matches phone pattern

**Example QR Content**:
```
tel:+1234567890
+1-234-567-8900
```

**Actions**:
- Copy to Clipboard
- Dial Number (opens phone app if supported)

---

### 8. Geographic Location
**Detection Pattern**: Starts with `geo:`

**Example QR Content**:
```
geo:37.7749,-122.4194
geo:37.7749,-122.4194?q=San Francisco
```

**Parsed Components**:
- Latitude
- Longitude
- Optional query parameter

**Actions**:
- Copy to Clipboard
- Open in Maps (opens Google Maps in browser)

**URL Format**: `https://www.google.com/maps?q=LAT,LONG`

---

### 9. Plain Text
**Detection Pattern**: Any text not matching other patterns

**Example QR Content**:
```
Hello, this is plain text!
Product ID: ABC123
Serial: XYZ789
```

**Actions**:
- Copy to Clipboard

---

## Error Handling

### Camera Errors

#### No Camera Found
```
Error Message: "Failed to initialize camera. 
Please check if a webcam is connected."
```
**Cause**: No webcam devices detected
**Recovery**: Connect camera and restart application

#### Camera Access Denied
```
Error Message: "Error accessing camera: [details]"
```
**Cause**: Permission denied or camera in use
**Recovery**: Grant permissions or close other applications

#### Camera Initialization Failed
```
Status: "Camera initialization failed"
```
**Cause**: Hardware error or driver issue
**Recovery**: Check camera drivers and connections

### QR Decode Errors
- Silently handled (no QR code found is normal)
- Logged for debugging
- No user notification (to avoid spam during scanning)

### Action Errors

#### Browser Not Supported
```
Error Message: "Desktop browsing is not supported on this system."
```
**Platforms**: Some minimal Linux installations

#### Email Client Not Available
**Fallback**: Shows email address in dialog

#### SMS/Phone Not Supported
**Fallback**: Shows number/message in dialog

## User Experience Flow

### Startup Sequence
1. Application window appears
2. UI components load with animations
3. Status shows "Ready to scan"
4. User sees placeholder in camera panel

### Scanning Sequence
1. User clicks "Start Scanning"
2. Status: "Initializing camera..."
3. Camera permission requested (if needed)
4. Live feed appears
5. Status: "Scanning for QR codes..."
6. Scanning animation starts
7. Stop button enabled

### Detection Sequence
1. QR code enters camera view
2. Green bounding box appears
3. Corner markers highlight QR
4. "QR Code Detected!" message shown
5. Beep sound plays
6. Decoded content appears in results
7. Action buttons populate
8. Results panel animates in

### Action Sequence
1. User clicks action button
2. Action executes (browser opens, etc.)
3. Success/error feedback shown
4. Scanning continues

### Stop Sequence
1. User clicks "Stop Scanning"
2. Camera feed stops
3. Camera released
4. Placeholder shown
5. Status: "Stopped"
6. Start button re-enabled

## Technical Implementation

### Thread Management
- Main UI thread: Swing Event Dispatch Thread
- Camera initialization: SwingWorker background thread
- Scanning loop: Swing Timer (100ms interval)
- Animation: Swing Timer (30ms interval)

### Memory Management
- Frames captured and released immediately
- BufferedImage reused per cycle
- Proper cleanup on stop
- No memory leaks

### Performance Optimization
- Aspect ratio calculation cached
- Bounding box calculations optimized
- ZXing configured with TRY_HARDER hint
- Limited to QR_CODE format only

### Code Organization
```
com.qrscanner
├── QRScannerApp          - Entry point
├── ui
│   └── MainFrame         - UI and animation
├── camera
│   └── CameraManager     - Camera handling
├── decoder
│   └── QRDecoder         - QR detection/decoding
└── handlers
    └── QRActionHandler   - Type-specific actions
```

## Customization Options

### Change Colors
Edit `MainFrame.java`:
- Control panel gradient: lines 73-78
- Button colors: createStyledButton() calls
- Detection box color: line 465

### Change Timing
Edit `MainFrame.java`:
- Scan interval: line 259 (Timer constructor)
- Animation speed: line 195 (Timer constructor)

### Change Camera Resolution
Edit `CameraManager.java`:
- CAPTURE_SIZE constant: line 15

### Add New QR Types
1. Add enum value to `QRDecoder.QRType`
2. Add detection logic in `determineQRType()`
3. Add case in `handleQRDetection()`
4. Create handler method in `QRActionHandler`

## Best Practices for Use

1. **Lighting**: Ensure good lighting on QR code
2. **Distance**: Hold QR 6-12 inches from camera
3. **Angle**: Face QR code directly to camera
4. **Stability**: Hold camera/QR steady for detection
5. **Size**: QR should fill 20-50% of frame
6. **Quality**: Use high-quality, clear QR codes

## Troubleshooting

### QR Not Detected
- Check lighting
- Adjust distance
- Ensure QR is in focus
- Try different angles
- Verify QR code quality

### Camera Feed Laggy
- Close other camera applications
- Reduce system load
- Check camera drivers

### Actions Not Working
- Verify default applications set
- Check OS permissions
- Ensure network connectivity (for URLs)

### Application Crashes
- Check Java version (requires 11+)
- Verify all dependencies installed
- Check console for error logs
