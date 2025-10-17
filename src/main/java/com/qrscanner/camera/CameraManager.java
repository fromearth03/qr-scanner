package com.qrscanner.camera;

import com.github.sarxos.webcam.Webcam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.List;

public class CameraManager {
    private static final Logger logger = LoggerFactory.getLogger(CameraManager.class);
    private Webcam webcam;
    private boolean isRunning;
    private static final Dimension CAPTURE_SIZE = new Dimension(640, 480);

    public CameraManager() {
        // Constructor - webcam will be initialized when initialize() is called
    }

    public boolean initialize() {
        try {
            // Check for available webcams
            List<Webcam> webcams = Webcam.getWebcams();
            if (webcams.isEmpty()) {
                logger.error("No webcam detected");
                return false;
            }

            // Get default webcam
            webcam = Webcam.getDefault();
            if (webcam == null) {
                logger.error("Could not get default webcam");
                return false;
            }

            // Set custom view size
            webcam.setViewSize(CAPTURE_SIZE);

            // Open the webcam
            boolean opened = webcam.open();
            if (!opened) {
                logger.error("Failed to open webcam");
                return false;
            }

            isRunning = true;
            logger.info("Camera initialized successfully");
            return true;

        } catch (Exception e) {
            logger.error("Error initializing camera", e);
            return false;
        }
    }

    public BufferedImage captureFrame() {
        if (!isRunning || webcam == null) {
            logger.warn("Camera not initialized or not running");
            return null;
        }

        try {
            if (!webcam.isOpen()) {
                logger.warn("Webcam is not open");
                return null;
            }
            return webcam.getImage();
        } catch (Exception e) {
            logger.error("Error capturing frame", e);
            return null;
        }
    }

    public void checkPermissions() {
        // Webcam Capture library handles permissions internally
        // This method can be used to check if webcams are available
        List<Webcam> webcams = Webcam.getWebcams();
        if (webcams.isEmpty()) {
            logger.warn("No webcams available - permission may be denied or no camera connected");
        } else {
            logger.info("Found " + webcams.size() + " webcam(s)");
        }
    }

    public void cleanup() {
        if (webcam != null && webcam.isOpen()) {
            try {
                webcam.close();
                logger.info("Camera closed successfully");
            } catch (Exception e) {
                logger.error("Error closing camera", e);
            }
        }
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning && webcam != null && webcam.isOpen();
    }
}
