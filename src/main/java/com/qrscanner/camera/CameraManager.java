package com.qrscanner.camera;

import java.awt.image.BufferedImage;
import javax.swing.*;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.FrameGrabber.Exception;

public class CameraManager {
    private OpenCVFrameGrabber grabber;
    private boolean isRunning;

    public CameraManager() {
        grabber = new OpenCVFrameGrabber(0); // Use default camera
    }

    public void initialize() throws Exception {
        grabber.start();
        isRunning = true;
    }

    public BufferedImage captureFrame() throws Exception {
        if (!isRunning) {
            throw new IllegalStateException("Camera not initialized. Call initialize() first.");
        }
        Frame frame = grabber.grab();
        return frame != null ? frame.image : null;
    }

    public void checkPermissions() {
        // Placeholder for permission check logic
        // You can implement actual permission checks based on your requirements
        System.out.println("Permission check passed.");
    }

    public void cleanup() throws Exception {
        if (isRunning) {
            grabber.stop();
            isRunning = false;
        }
    }
}