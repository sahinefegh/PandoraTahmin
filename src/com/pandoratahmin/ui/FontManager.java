package com.pandoratahmin.ui;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

public class FontManager {
    private static Font customF1Font;

    static {
        loadCustomFont();
    }

    private static void loadCustomFont() {
        try {
            InputStream is = FontManager.class.getResourceAsStream("/F1-Regular.otf");
            if (is != null) {
                customF1Font = Font.createFont(Font.TRUETYPE_FONT, is);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customF1Font);
            } else {
                System.err.println("Uyarı: F1-Regular.otf bulunamadı! Varsayılan font kullanılacak.");
                customF1Font = new Font("Verdana", Font.BOLD, 16);
            }
        } catch (Exception e) {
            System.err.println("Font yüklenirken hata oluştu: " + e.getMessage());
            customF1Font = new Font("Verdana", Font.BOLD, 16);
        }
    }

    public static Font getFont(int style, float size) {
        if (customF1Font != null) {
            return customF1Font.deriveFont(style, size);
        }
        return new Font("Verdana", style, (int)size);
    }
}
