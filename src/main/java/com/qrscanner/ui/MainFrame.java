package com.qrscanner.ui;

import com.qrscanner.camera.CameraManager;
import com.qrscanner.decoder.QRDecoder;
import com.qrscanner.handlers.QRActionHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class MainFrame extends JFrame {
    private JPanel cameraPanel;
    private JPanel resultsPanel;
    private JPanel controlPanel;
    private JLabel statusLabel;
    private JButton startButton;
    private JButton stopButton;
    private JTextArea resultTextArea;
    private JPanel actionPanel;
    private CameraPanel cameraViewPanel;
    private CameraManager cameraManager;
    private QRDecoder qrDecoder;
    private QRActionHandler actionHandler;
    private Timer scanTimer;
    private Timer animationTimer;
    private float animationProgress = 0f;
    private boolean isScanning = false;
    private JLabel detectionLabel;

    public MainFrame() {
        setTitle("QR Code Scanner");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialize managers
        cameraManager = new CameraManager();
        qrDecoder = new QRDecoder();
        actionHandler = new QRActionHandler(this);
        
        initializeComponents();
        setupLayout();
        setupAnimations();
        
        setVisible(true);
    }

    private void initializeComponents() {
        // Camera panel with custom painting
        cameraViewPanel = new CameraPanel();
        cameraViewPanel.setPreferredSize(new Dimension(640, 480));
        
        // Control panel with gradient background
        controlPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(41, 128, 185),
                    getWidth(), 0, new Color(109, 213, 250)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        controlPanel.setPreferredSize(new Dimension(0, 80));
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        
        // Styled buttons
        startButton = createStyledButton("Start Scanning", new Color(46, 204, 113));
        stopButton = createStyledButton("Stop Scanning", new Color(231, 76, 60));
        stopButton.setEnabled(false);
        
        startButton.addActionListener(e -> startScanning());
        stopButton.addActionListener(e -> stopScanning());
        
        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        
        // Status label
        statusLabel = new JLabel("Ready to scan");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(Color.WHITE);
        controlPanel.add(statusLabel);
        
        // Detection label (animated)
        detectionLabel = new JLabel("");
        detectionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        detectionLabel.setForeground(new Color(46, 204, 113));
        detectionLabel.setVisible(false);
        
        // Results panel
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BorderLayout(10, 10));
        resultsPanel.setBackground(new Color(236, 240, 241));
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        resultsPanel.setPreferredSize(new Dimension(0, 200));
        
        // Result text area
        resultTextArea = new JTextArea(6, 40);
        resultTextArea.setEditable(false);
        resultTextArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        resultTextArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Decoded Content",
            0,
            0,
            new Font("Arial", Font.BOLD, 12),
            new Color(52, 152, 219)
        ));
        
        resultsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Action panel
        actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionPanel.setBackground(new Color(236, 240, 241));
        resultsPanel.add(actionPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(color.brighter());
                } else {
                    g2d.setColor(color);
                }
                
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(180, 40));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));
        
        // Main camera container
        JPanel cameraContainer = new JPanel(new BorderLayout());
        cameraContainer.setBackground(new Color(44, 62, 80));
        cameraContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Center the camera panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(44, 62, 80));
        centerPanel.add(cameraViewPanel);
        
        // Add detection label overlay
        JPanel overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setOpaque(false);
        overlayPanel.add(detectionLabel, BorderLayout.NORTH);
        
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(680, 520));
        
        centerPanel.setBounds(0, 0, 680, 520);
        overlayPanel.setBounds(0, 0, 680, 520);
        
        layeredPane.add(centerPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(overlayPanel, JLayeredPane.PALETTE_LAYER);
        
        cameraContainer.add(layeredPane, BorderLayout.CENTER);
        
        add(controlPanel, BorderLayout.NORTH);
        add(cameraContainer, BorderLayout.CENTER);
        add(resultsPanel, BorderLayout.SOUTH);
    }

    private void setupAnimations() {
        animationTimer = new Timer(30, e -> {
            animationProgress += 0.05f;
            if (animationProgress > 1.0f) {
                animationProgress = 0f;
            }
            cameraViewPanel.repaint();
        });
    }

    private void startScanning() {
        if (isScanning) return;
        
        statusLabel.setText("Initializing camera...");
        startButton.setEnabled(false);
        
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return cameraManager.initialize();
            }
            
            @Override
            protected void done() {
                try {
                    if (get()) {
                        isScanning = true;
                        statusLabel.setText("Scanning for QR codes...");
                        stopButton.setEnabled(true);
                        animationTimer.start();
                        
                        // Start scanning timer
                        scanTimer = new Timer(100, e -> performScan());
                        scanTimer.start();
                    } else {
                        JOptionPane.showMessageDialog(
                            MainFrame.this,
                            "Failed to initialize camera. Please check if a webcam is connected.",
                            "Camera Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                        statusLabel.setText("Camera initialization failed");
                        startButton.setEnabled(true);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        MainFrame.this,
                        "Error accessing camera: " + ex.getMessage(),
                        "Camera Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    statusLabel.setText("Error");
                    startButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void stopScanning() {
        if (!isScanning) return;
        
        isScanning = false;
        if (scanTimer != null) {
            scanTimer.stop();
        }
        animationTimer.stop();
        
        cameraManager.cleanup();
        cameraViewPanel.setImage(null);
        cameraViewPanel.setDetectedBox(null);
        
        statusLabel.setText("Stopped");
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        detectionLabel.setVisible(false);
    }

    private void performScan() {
        if (!isScanning) return;
        
        BufferedImage image = cameraManager.captureFrame();
        if (image != null) {
            cameraViewPanel.setImage(image);
            
            // Try to decode QR code
            QRDecoder.DecodedQR result = qrDecoder.decode(image);
            if (result != null) {
                handleQRDetection(result);
            } else {
                cameraViewPanel.setDetectedBox(null);
                detectionLabel.setVisible(false);
            }
        }
    }

    private void handleQRDetection(QRDecoder.DecodedQR result) {
        // Show detection feedback
        detectionLabel.setText("✓ QR Code Detected!");
        detectionLabel.setVisible(true);
        
        // Set bounding box
        cameraViewPanel.setDetectedBox(result.getBoundingBox());
        
        // Display result
        resultTextArea.setText(result.getText());
        
        // Play sound feedback (simple beep)
        Toolkit.getDefaultToolkit().beep();
        
        // Clear and populate action panel
        actionPanel.removeAll();
        
        JButton copyButton = createActionButton("Copy to Clipboard");
        copyButton.addActionListener(e -> actionHandler.copyToClipboard(result.getText()));
        actionPanel.add(copyButton);
        
        // Add type-specific actions
        switch (result.getType()) {
            case URL:
                JButton openButton = createActionButton("Open Link");
                openButton.addActionListener(e -> actionHandler.openURL(result.getText()));
                actionPanel.add(openButton);
                break;
                
            case WIFI:
                JButton wifiButton = createActionButton("Show WiFi Details");
                wifiButton.addActionListener(e -> actionHandler.showWiFiDetails(result.getText()));
                actionPanel.add(wifiButton);
                break;
                
            case VCARD:
                JButton contactButton = createActionButton("Show Contact");
                contactButton.addActionListener(e -> actionHandler.showContactDetails(result.getText()));
                actionPanel.add(contactButton);
                break;
                
            case SMS:
                JButton smsButton = createActionButton("Compose SMS");
                smsButton.addActionListener(e -> actionHandler.composeSMS(result.getText()));
                actionPanel.add(smsButton);
                break;
                
            case EMAIL:
                JButton emailButton = createActionButton("Compose Email");
                emailButton.addActionListener(e -> actionHandler.composeEmail(result.getText()));
                actionPanel.add(emailButton);
                break;
                
            case GEO:
                JButton mapButton = createActionButton("Open in Maps");
                mapButton.addActionListener(e -> actionHandler.openInMaps(result.getText()));
                actionPanel.add(mapButton);
                break;
                
            case PHONE:
                JButton phoneButton = createActionButton("Dial Number");
                phoneButton.addActionListener(e -> actionHandler.dialNumber(result.getText()));
                actionPanel.add(phoneButton);
                break;
        }
        
        actionPanel.revalidate();
        actionPanel.repaint();
        
        // Animate results panel
        animateResultsPanel();
    }

    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        return button;
    }

    private void animateResultsPanel() {
        final int[] height = {0};
        final int targetHeight = resultsPanel.getPreferredSize().height;
        
        Timer timer = new Timer(10, null);
        timer.addActionListener(e -> {
            height[0] += 10;
            if (height[0] >= targetHeight) {
                height[0] = targetHeight;
                timer.stop();
            }
            resultsPanel.setPreferredSize(new Dimension(0, height[0]));
            resultsPanel.revalidate();
        });
        timer.start();
    }

    // Inner class for camera display panel
    private class CameraPanel extends JPanel {
        private BufferedImage currentImage;
        private Rectangle detectedBox;

        public void setImage(BufferedImage image) {
            this.currentImage = image;
            repaint();
        }

        public void setDetectedBox(Rectangle box) {
            this.detectedBox = box;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw background
            g2d.setColor(new Color(44, 62, 80));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            if (currentImage != null) {
                // Calculate scaling to maintain aspect ratio
                double scaleX = (double) getWidth() / currentImage.getWidth();
                double scaleY = (double) getHeight() / currentImage.getHeight();
                double scale = Math.min(scaleX, scaleY);
                
                int scaledWidth = (int) (currentImage.getWidth() * scale);
                int scaledHeight = (int) (currentImage.getHeight() * scale);
                int x = (getWidth() - scaledWidth) / 2;
                int y = (getHeight() - scaledHeight) / 2;
                
                g2d.drawImage(currentImage, x, y, scaledWidth, scaledHeight, this);
                
                // Draw detected box
                if (detectedBox != null) {
                    g2d.setColor(new Color(46, 204, 113));
                    g2d.setStroke(new BasicStroke(3));
                    
                    int boxX = x + (int) (detectedBox.x * scale);
                    int boxY = y + (int) (detectedBox.y * scale);
                    int boxWidth = (int) (detectedBox.width * scale);
                    int boxHeight = (int) (detectedBox.height * scale);
                    
                    g2d.drawRect(boxX, boxY, boxWidth, boxHeight);
                    
                    // Draw corner markers for better visibility
                    int markerLength = 20;
                    g2d.setStroke(new BasicStroke(4));
                    
                    // Top-left corner
                    g2d.drawLine(boxX, boxY, boxX + markerLength, boxY);
                    g2d.drawLine(boxX, boxY, boxX, boxY + markerLength);
                    
                    // Top-right corner
                    g2d.drawLine(boxX + boxWidth, boxY, boxX + boxWidth - markerLength, boxY);
                    g2d.drawLine(boxX + boxWidth, boxY, boxX + boxWidth, boxY + markerLength);
                    
                    // Bottom-left corner
                    g2d.drawLine(boxX, boxY + boxHeight, boxX + markerLength, boxY + boxHeight);
                    g2d.drawLine(boxX, boxY + boxHeight, boxX, boxY + boxHeight - markerLength);
                    
                    // Bottom-right corner
                    g2d.drawLine(boxX + boxWidth, boxY + boxHeight, boxX + boxWidth - markerLength, boxY + boxHeight);
                    g2d.drawLine(boxX + boxWidth, boxY + boxHeight, boxX + boxWidth, boxY + boxHeight - markerLength);
                }
            } else if (isScanning) {
                // Draw scanning animation
                g2d.setColor(new Color(52, 152, 219, 100));
                int scanLineY = (int) (animationProgress * getHeight());
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(0, scanLineY, getWidth(), scanLineY);
                
                // Draw placeholder text
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                String text = "Initializing camera...";
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(text)) / 2;
                int textY = getHeight() / 2;
                g2d.drawString(text, textX, textY);
            } else {
                // Draw placeholder when not scanning
                g2d.setColor(new Color(149, 165, 166));
                g2d.setFont(new Font("Arial", Font.BOLD, 18));
                String text = "Click 'Start Scanning' to begin";
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(text)) / 2;
                int textY = getHeight() / 2;
                g2d.drawString(text, textX, textY);
                
                // Draw camera icon (simple representation)
                g2d.setColor(new Color(149, 165, 166, 100));
                g2d.fillRoundRect(getWidth() / 2 - 50, getHeight() / 2 - 80, 100, 80, 10, 10);
                g2d.setColor(new Color(149, 165, 166, 150));
                g2d.fillOval(getWidth() / 2 - 30, getHeight() / 2 - 60, 60, 60);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(640, 480);
        }
    }
}
