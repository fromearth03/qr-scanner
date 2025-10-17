package com.qrscanner.decoder;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

public class QRDecoder {
    private static final Logger logger = LoggerFactory.getLogger(QRDecoder.class);
    private final MultiFormatReader reader;

    public QRDecoder() {
        reader = new MultiFormatReader();
        
        // Configure hints for better detection
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, java.util.Arrays.asList(BarcodeFormat.QR_CODE));
        reader.setHints(hints);
    }

    public DecodedQR decode(BufferedImage image) {
        if (image == null) {
            return null;
        }

        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            
            Result result = reader.decode(bitmap);
            
            if (result != null) {
                String text = result.getText();
                QRType type = determineQRType(text);
                Rectangle boundingBox = calculateBoundingBox(result);
                
                logger.info("QR Code detected - Type: " + type + ", Text: " + text);
                return new DecodedQR(text, type, boundingBox);
            }
        } catch (NotFoundException e) {
            // No QR code found - this is normal, don't log
        } catch (Exception e) {
            logger.error("Error decoding QR code", e);
        } finally {
            reader.reset();
        }
        
        return null;
    }

    private Rectangle calculateBoundingBox(Result result) {
        ResultPoint[] points = result.getResultPoints();
        if (points == null || points.length < 2) {
            return new Rectangle(0, 0, 100, 100); // Default box
        }

        // Find min/max coordinates
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;

        for (ResultPoint point : points) {
            if (point != null) {
                minX = Math.min(minX, point.getX());
                minY = Math.min(minY, point.getY());
                maxX = Math.max(maxX, point.getX());
                maxY = Math.max(maxY, point.getY());
            }
        }

        int x = (int) minX;
        int y = (int) minY;
        int width = (int) (maxX - minX);
        int height = (int) (maxY - minY);

        // Add some padding
        int padding = 10;
        x = Math.max(0, x - padding);
        y = Math.max(0, y - padding);
        width += 2 * padding;
        height += 2 * padding;

        return new Rectangle(x, y, width, height);
    }

    private QRType determineQRType(String text) {
        if (text == null || text.isEmpty()) {
            return QRType.TEXT;
        }

        String lower = text.toLowerCase();

        // Check for URL
        if (lower.startsWith("http://") || lower.startsWith("https://") || lower.startsWith("www.")) {
            return QRType.URL;
        }

        // Check for WiFi
        if (lower.startsWith("wifi:")) {
            return QRType.WIFI;
        }

        // Check for Email
        if (lower.startsWith("mailto:") || (lower.contains("@") && lower.contains("."))) {
            return QRType.EMAIL;
        }

        // Check for SMS
        if (lower.startsWith("sms:") || lower.startsWith("smsto:")) {
            return QRType.SMS;
        }

        // Check for Phone
        if (lower.startsWith("tel:") || lower.matches("^\\+?[0-9\\-\\s()]+$")) {
            return QRType.PHONE;
        }

        // Check for VCard
        if (lower.startsWith("begin:vcard") || lower.startsWith("mecard:")) {
            return QRType.VCARD;
        }

        // Check for Geo location
        if (lower.startsWith("geo:")) {
            return QRType.GEO;
        }

        // Default to plain text
        return QRType.TEXT;
    }

    public enum QRType {
        URL, WIFI, EMAIL, SMS, PHONE, VCARD, GEO, TEXT
    }

    public static class DecodedQR {
        private final String text;
        private final QRType type;
        private final Rectangle boundingBox;

        public DecodedQR(String text, QRType type, Rectangle boundingBox) {
            this.text = text;
            this.type = type;
            this.boundingBox = boundingBox;
        }

        public String getText() {
            return text;
        }

        public QRType getType() {
            return type;
        }

        public Rectangle getBoundingBox() {
            return boundingBox;
        }
    }
}
