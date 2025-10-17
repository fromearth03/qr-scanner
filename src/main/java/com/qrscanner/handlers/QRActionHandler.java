package com.qrscanner.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class QRActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(QRActionHandler.class);
    private final JFrame parentFrame;

    public QRActionHandler(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public void copyToClipboard(String text) {
        try {
            StringSelection selection = new StringSelection(text);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            showSuccessMessage("Copied to clipboard!");
        } catch (Exception e) {
            logger.error("Error copying to clipboard", e);
            showErrorMessage("Failed to copy to clipboard: " + e.getMessage());
        }
    }

    public void openURL(String url) {
        try {
            // Ensure URL has protocol
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
                logger.info("Opened URL: " + url);
            } else {
                showErrorMessage("Desktop browsing is not supported on this system.");
            }
        } catch (Exception e) {
            logger.error("Error opening URL", e);
            showErrorMessage("Failed to open URL: " + e.getMessage());
        }
    }

    public void showWiFiDetails(String wifiData) {
        try {
            Map<String, String> wifiInfo = parseWiFiData(wifiData);
            
            StringBuilder message = new StringBuilder("<html><body style='width: 300px;'>");
            message.append("<h3>WiFi Network Details</h3>");
            message.append("<table>");
            message.append("<tr><td><b>SSID:</b></td><td>").append(escapeHtml(wifiInfo.getOrDefault("SSID", "N/A"))).append("</td></tr>");
            message.append("<tr><td><b>Password:</b></td><td>").append(escapeHtml(wifiInfo.getOrDefault("Password", "N/A"))).append("</td></tr>");
            message.append("<tr><td><b>Security:</b></td><td>").append(escapeHtml(wifiInfo.getOrDefault("Security", "N/A"))).append("</td></tr>");
            message.append("<tr><td><b>Hidden:</b></td><td>").append(escapeHtml(wifiInfo.getOrDefault("Hidden", "No"))).append("</td></tr>");
            message.append("</table>");
            message.append("<br><p><i>Note: Automatic WiFi connection requires system-specific configuration.</i></p>");
            message.append("</body></html>");

            JOptionPane.showMessageDialog(
                parentFrame,
                message.toString(),
                "WiFi Information",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            logger.error("Error parsing WiFi data", e);
            showErrorMessage("Failed to parse WiFi information: " + e.getMessage());
        }
    }

    private Map<String, String> parseWiFiData(String wifiData) {
        Map<String, String> info = new HashMap<>();
        
        // WiFi QR format: WIFI:T:WPA;S:MyNetwork;P:MyPassword;H:false;;
        if (wifiData.toUpperCase().startsWith("WIFI:")) {
            String data = wifiData.substring(5);
            String[] parts = data.split(";");
            
            for (String part : parts) {
                if (part.contains(":")) {
                    String[] keyValue = part.split(":", 2);
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();
                        
                        switch (key.toUpperCase()) {
                            case "T":
                                info.put("Security", value);
                                break;
                            case "S":
                                info.put("SSID", value);
                                break;
                            case "P":
                                info.put("Password", value);
                                break;
                            case "H":
                                info.put("Hidden", value.equalsIgnoreCase("true") ? "Yes" : "No");
                                break;
                        }
                    }
                }
            }
        }
        
        return info;
    }

    public void showContactDetails(String contactData) {
        try {
            Map<String, String> contactInfo = parseContactData(contactData);
            
            StringBuilder message = new StringBuilder("<html><body style='width: 350px;'>");
            message.append("<h3>Contact Information</h3>");
            message.append("<table>");
            
            if (contactInfo.containsKey("Name")) {
                message.append("<tr><td><b>Name:</b></td><td>").append(escapeHtml(contactInfo.get("Name"))).append("</td></tr>");
            }
            if (contactInfo.containsKey("Phone")) {
                message.append("<tr><td><b>Phone:</b></td><td>").append(escapeHtml(contactInfo.get("Phone"))).append("</td></tr>");
            }
            if (contactInfo.containsKey("Email")) {
                message.append("<tr><td><b>Email:</b></td><td>").append(escapeHtml(contactInfo.get("Email"))).append("</td></tr>");
            }
            if (contactInfo.containsKey("Organization")) {
                message.append("<tr><td><b>Organization:</b></td><td>").append(escapeHtml(contactInfo.get("Organization"))).append("</td></tr>");
            }
            if (contactInfo.containsKey("Address")) {
                message.append("<tr><td><b>Address:</b></td><td>").append(escapeHtml(contactInfo.get("Address"))).append("</td></tr>");
            }
            
            message.append("</table>");
            message.append("<br><p><i>Contact information has been decoded. You can manually save this contact.</i></p>");
            message.append("</body></html>");

            JOptionPane.showMessageDialog(
                parentFrame,
                message.toString(),
                "Contact Details",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            logger.error("Error parsing contact data", e);
            showErrorMessage("Failed to parse contact information: " + e.getMessage());
        }
    }

    private Map<String, String> parseContactData(String contactData) {
        Map<String, String> info = new HashMap<>();
        
        if (contactData.toUpperCase().startsWith("BEGIN:VCARD")) {
            // Parse VCard format
            String[] lines = contactData.split("\\r?\\n");
            for (String line : lines) {
                if (line.startsWith("FN:")) {
                    info.put("Name", line.substring(3));
                } else if (line.startsWith("TEL:") || line.startsWith("TEL;")) {
                    info.put("Phone", line.substring(line.indexOf(':') + 1));
                } else if (line.startsWith("EMAIL:") || line.startsWith("EMAIL;")) {
                    info.put("Email", line.substring(line.indexOf(':') + 1));
                } else if (line.startsWith("ORG:")) {
                    info.put("Organization", line.substring(4));
                } else if (line.startsWith("ADR:") || line.startsWith("ADR;")) {
                    info.put("Address", line.substring(line.indexOf(':') + 1));
                }
            }
        } else if (contactData.toUpperCase().startsWith("MECARD:")) {
            // Parse MECARD format
            String data = contactData.substring(7);
            String[] parts = data.split(";");
            
            for (String part : parts) {
                if (part.contains(":")) {
                    String[] keyValue = part.split(":", 2);
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();
                        
                        switch (key.toUpperCase()) {
                            case "N":
                                info.put("Name", value);
                                break;
                            case "TEL":
                                info.put("Phone", value);
                                break;
                            case "EMAIL":
                                info.put("Email", value);
                                break;
                            case "ORG":
                                info.put("Organization", value);
                                break;
                            case "ADR":
                                info.put("Address", value);
                                break;
                        }
                    }
                }
            }
        }
        
        return info;
    }

    public void composeSMS(String smsData) {
        try {
            String number = smsData;
            String message = "";
            
            // Parse SMS format: smsto:+1234567890:Hello
            if (smsData.toLowerCase().startsWith("sms:") || smsData.toLowerCase().startsWith("smsto:")) {
                String[] parts = smsData.split(":", 3);
                if (parts.length >= 2) {
                    number = parts[1];
                    if (parts.length >= 3) {
                        message = parts[2];
                    }
                }
            }
            
            String uri = "sms:" + number;
            if (!message.isEmpty()) {
                uri += "?body=" + message;
            }
            
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(uri));
                logger.info("Opened SMS composer");
            } else {
                showInfoMessage("SMS Number: " + number + (message.isEmpty() ? "" : "\nMessage: " + message));
            }
        } catch (Exception e) {
            logger.error("Error composing SMS", e);
            showErrorMessage("Failed to compose SMS: " + e.getMessage());
        }
    }

    public void composeEmail(String emailData) {
        try {
            String email = emailData;
            
            // Remove mailto: prefix if present
            if (email.toLowerCase().startsWith("mailto:")) {
                email = email.substring(7);
            }
            
            String uri = "mailto:" + email;
            
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
                Desktop.getDesktop().mail(new URI(uri));
                logger.info("Opened email composer");
            } else if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(uri));
                logger.info("Opened email composer via browser");
            } else {
                showInfoMessage("Email: " + email);
            }
        } catch (Exception e) {
            logger.error("Error composing email", e);
            showErrorMessage("Failed to compose email: " + e.getMessage());
        }
    }

    public void dialNumber(String phoneData) {
        try {
            String number = phoneData;
            
            // Remove tel: prefix if present
            if (number.toLowerCase().startsWith("tel:")) {
                number = number.substring(4);
            }
            
            String uri = "tel:" + number;
            
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(uri));
                logger.info("Opened phone dialer");
            } else {
                showInfoMessage("Phone Number: " + number);
            }
        } catch (Exception e) {
            logger.error("Error dialing number", e);
            showErrorMessage("Failed to dial number: " + e.getMessage());
        }
    }

    public void openInMaps(String geoData) {
        try {
            String coords = geoData;
            
            // Parse geo format: geo:37.7749,-122.4194
            if (coords.toLowerCase().startsWith("geo:")) {
                coords = coords.substring(4);
            }
            
            // Create Google Maps URL
            String mapsUrl = "https://www.google.com/maps?q=" + coords;
            
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(mapsUrl));
                logger.info("Opened location in maps");
            } else {
                showErrorMessage("Desktop browsing is not supported on this system.");
            }
        } catch (Exception e) {
            logger.error("Error opening maps", e);
            showErrorMessage("Failed to open location in maps: " + e.getMessage());
        }
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(
            parentFrame,
            message,
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(
            parentFrame,
            message,
            "Information",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(
            parentFrame,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}
