package com.metodos.gui;

import com.metodos.methods.IntegrationMethod;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarPanel extends JPanel {
    private final MainFrame mainFrame;
    private JPanel methodsSubpanel;
    private boolean methodsExpanded = true;
    private JButton activeMethodButton = null;

    public SidebarPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        
        // Configuration
        setPreferredSize(new Dimension(240, 0));
        setBackground(new Color(9, 31, 38)); // Darker teal sidebar
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(5, 18, 22)));
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        // TOP SECTION - BRANDING
        JPanel brandPanel = new JPanel(new BorderLayout());
        brandPanel.setOpaque(false);
        brandPanel.setBorder(new EmptyBorder(25, 20, 20, 20));

        JLabel brandLabel = new JLabel("CÁLCULO NUMÉRICO");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        brandLabel.setForeground(new Color(45, 170, 195)); // Bright teal/cyan
        brandPanel.add(brandLabel, BorderLayout.NORTH);

        JLabel subBrandLabel = new JLabel("Integración Definida");
        subBrandLabel.setFont(new Font("Segoe UI Light", Font.ITALIC, 12));
        subBrandLabel.setForeground(new Color(150, 175, 180));
        brandPanel.add(subBrandLabel, BorderLayout.SOUTH);

        add(brandPanel, BorderLayout.NORTH);

        // CENTER SECTION - SCROLLABLE MENU
        JPanel menuContainer = new JPanel();
        menuContainer.setOpaque(false);
        menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));

        // 1. Calculator Header/Menu Item
        JButton calcHeaderBtn = createSidebarButton("  Calculadora", true);
        menuContainer.add(calcHeaderBtn);

        // Subpanel for integration methods
        methodsSubpanel = new JPanel();
        methodsSubpanel.setOpaque(false);
        methodsSubpanel.setLayout(new BoxLayout(methodsSubpanel, BoxLayout.Y_AXIS));
        methodsSubpanel.setBorder(new EmptyBorder(0, 20, 10, 0));

        for (IntegrationMethod method : mainFrame.getIntegrationMethods()) {
            JButton methodBtn = createMethodButton(method);
            methodsSubpanel.add(methodBtn);
            
            // Set default active button
            if (method == mainFrame.getCurrentMethod()) {
                activeMethodButton = methodBtn;
                setButtonActive(methodBtn, true);
            }
        }
        menuContainer.add(methodsSubpanel);

        // Toggle methods subpanel when clicking the header
        calcHeaderBtn.addActionListener(e -> {
            methodsExpanded = !methodsExpanded;
            methodsSubpanel.setVisible(methodsExpanded);
            revalidate();
            repaint();
        });

        // 2. Formulas / Help Item
        JButton helpBtn = createSidebarButton("  Fórmulas y Ayuda", false);
        helpBtn.addActionListener(e -> {
            clearActiveMethodHighlight();
            mainFrame.showView("help");
        });
        menuContainer.add(helpBtn);
        menuContainer.add(Box.createVerticalStrut(5));

        // 3. History Item
        JButton historyBtn = createSidebarButton("  Historial", false);
        historyBtn.addActionListener(e -> {
            clearActiveMethodHighlight();
            mainFrame.showView("history");
        });
        menuContainer.add(historyBtn);

        // Put in scroll pane for safety, but hide scrollbars
        JScrollPane scrollPane = new JScrollPane(menuContainer);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        add(scrollPane, BorderLayout.CENTER);

        // BOTTOM SECTION - USER OR APP VERSION
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel versionLabel = new JLabel("v1.0.0 - Métodos Numéricos");
        versionLabel.setFont(new Font("Segoe UI Light", Font.PLAIN, 10));
        versionLabel.setForeground(new Color(110, 130, 135));
        footerPanel.add(versionLabel, BorderLayout.CENTER);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private JButton createSidebarButton(String text, boolean isHeader) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(9, 31, 38));
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(240, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(17, 49, 58));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(9, 31, 38));
            }
        });

        return btn;
    }

    private JButton createMethodButton(IntegrationMethod method) {
        JButton btn = new JButton("•  " + method.getName());
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(new Color(190, 210, 215));
        btn.setBackground(new Color(9, 31, 38));
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(220, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != activeMethodButton) {
                    btn.setBackground(new Color(15, 45, 54));
                    btn.setForeground(Color.WHITE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != activeMethodButton) {
                    btn.setBackground(new Color(9, 31, 38));
                    btn.setForeground(new Color(190, 210, 215));
                }
            }
        });

        btn.addActionListener(e -> {
            if (activeMethodButton != null) {
                setButtonActive(activeMethodButton, false);
            }
            activeMethodButton = btn;
            setButtonActive(btn, true);
            mainFrame.setIntegrationMethod(method);
        });

        return btn;
    }

    private void setButtonActive(JButton btn, boolean active) {
        if (active) {
            btn.setBackground(new Color(23, 76, 92)); // Lighter active blue-teal
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(45, 170, 195)), // cyan active bar
                new EmptyBorder(8, 11, 8, 15)
            ));
        } else {
            btn.setBackground(new Color(9, 31, 38));
            btn.setForeground(new Color(190, 210, 215));
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        }
    }

    private void clearActiveMethodHighlight() {
        if (activeMethodButton != null) {
            setButtonActive(activeMethodButton, false);
            activeMethodButton = null;
        }
    }
}
