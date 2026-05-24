package com.metodos;

import com.formdev.flatlaf.FlatDarkLaf;
import com.metodos.gui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Color;

public class App {
    public static void main(String[] args) {
        // Setup FlatLaf Dark theme
        SwingUtilities.invokeLater(() -> {
            try {
                FlatDarkLaf.setup();
                
                // Customize UI defaults for a modern feel
                UIManager.put("Button.arc", 12);
                UIManager.put("Component.arc", 12);
                UIManager.put("TextComponent.arc", 12);
                UIManager.put("ScrollBar.showButtons", true);
                UIManager.put("ScrollBar.thumbArc", 999);
                
                // Color overrides
                UIManager.put("Panel.background", new Color(11, 37, 46)); // Deep Teal
                UIManager.put("OptionPane.background", new Color(11, 37, 46));
                UIManager.put("Button.background", new Color(19, 56, 68)); // Slate Teal
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("Label.foreground", Color.WHITE);
                
                // Launch Main Frame
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
