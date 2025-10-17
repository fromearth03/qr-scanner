package com.qrscanner;

import com.qrscanner.ui.MainFrame;
import javax.swing.*;

public class QRScannerApp {
    public static void main(String[] args) {
        // Set the look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and show the main frame on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}