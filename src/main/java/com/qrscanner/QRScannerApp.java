package com.qrscanner;

import javax.swing.*;

public class QRScannerApp {
    public static void main(String[] args) {
        // Set the look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the main frame
        JFrame frame = new JFrame("QR Scanner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // Center the frame
        
        // Add components (you can add your own components here later)
        // frame.add(new YourComponent());

        // Make the frame visible
        frame.setVisible(true);
    }
}