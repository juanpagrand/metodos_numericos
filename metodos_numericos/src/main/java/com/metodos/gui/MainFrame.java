package com.metodos.gui;

import com.metodos.methods.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel titleLabel;
    private SidebarPanel sidebarPanel;
    private boolean sidebarVisible = true;

    // View Panels
    private CalculatorPanel calculatorPanel;
    private HelpPanel helpPanel;
    private HistoryPanel historyPanel;

    // Active numerical integration methods
    private final List<IntegrationMethod> integrationMethods;
    private IntegrationMethod currentMethod;
    private final List<String> calculationHistory;

    public MainFrame() {
        // Window Configuration
        setTitle("Calculadora de Métodos de Integración Numérica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setMinimumSize(new Dimension(850, 650));
        setLocationRelativeTo(null);

        // Initialize state
        calculationHistory = new ArrayList<>();
        integrationMethods = new ArrayList<>();
        integrationMethods.add(new TrapezoidalMethod());
        integrationMethods.add(new Simpson13Method());
        integrationMethods.add(new Simpson38Method());
        integrationMethods.add(new BooleMethod());
        integrationMethods.add(new SimpsonAbiertoMethod());
        currentMethod = integrationMethods.get(0); // Default to Trapezoidal

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // 1. HEADER PANEL
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(19, 56, 68)); // Light teal header
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(9, 31, 38)));

        // Menu Toggle Button
        JButton menuButton = new JButton();
        menuButton.setIcon(new HamburgerIcon(Color.WHITE));
        menuButton.setBackground(new Color(19, 56, 68));
        menuButton.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        menuButton.setFocusPainted(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuButton.addActionListener(e -> toggleSidebar());

        // Header Title
        titleLabel = new JLabel("Método Trapezoidal");
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(menuButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        // Empty spacer to balance the layout
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(60, 60));
        headerPanel.add(spacer, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // 2. MAIN BODY PANEL
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBackground(new Color(11, 37, 46));

        // Create Sidebar
        sidebarPanel = new SidebarPanel(this);
        bodyPanel.add(sidebarPanel, BorderLayout.WEST);

        // Create Content Panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);

        // Initialize view panels
        calculatorPanel = new CalculatorPanel(this);
        helpPanel = new HelpPanel();
        historyPanel = new HistoryPanel(this);

        contentPanel.add(calculatorPanel, "calculator");
        contentPanel.add(helpPanel, "help");
        contentPanel.add(historyPanel, "history");

        bodyPanel.add(contentPanel, BorderLayout.CENTER);
        add(bodyPanel, BorderLayout.CENTER);
    }

    private void toggleSidebar() {
        sidebarVisible = !sidebarVisible;
        sidebarPanel.setVisible(sidebarVisible);
        revalidate();
        repaint();
    }

    /**
     * Changes the current view in the CardLayout.
     */
    public void showView(String viewName) {
        cardLayout.show(contentPanel, viewName);
        if (viewName.equals("calculator")) {
            titleLabel.setText(currentMethod.getName());
        } else if (viewName.equals("help")) {
            titleLabel.setText("Fórmulas y Ayuda");
        } else if (viewName.equals("history")) {
            titleLabel.setText("Historial de Cálculos");
            historyPanel.refreshHistory();
        }
    }

    /**
     * Changes the active numerical integration method.
     */
    public void setIntegrationMethod(IntegrationMethod method) {
        this.currentMethod = method;
        titleLabel.setText(method.getName());
        calculatorPanel.onMethodChanged(method);
        showView("calculator");
    }

    public List<IntegrationMethod> getIntegrationMethods() {
        return integrationMethods;
    }

    public IntegrationMethod getCurrentMethod() {
        return currentMethod;
    }

    public void addHistoryEntry(String entry) {
        calculationHistory.add(0, entry); // Insert at beginning (newest first)
    }

    public List<String> getCalculationHistory() {
        return calculationHistory;
    }

    public void clearHistory() {
        calculationHistory.clear();
        historyPanel.refreshHistory();
    }

    private static class HamburgerIcon implements Icon {
        private final Color color;

        public HamburgerIcon(Color color) {
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            // Draw three horizontal lines
            g2.drawLine(x + 2, y + 6, x + 18, y + 6);
            g2.drawLine(x + 2, y + 11, x + 18, y + 11);
            g2.drawLine(x + 2, y + 16, x + 18, y + 16);
            
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return 20;
        }

        @Override
        public int getIconHeight() {
            return 22;
        }
    }
}
