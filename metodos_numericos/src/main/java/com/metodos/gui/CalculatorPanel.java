package com.metodos.gui;

import com.metodos.methods.IntegrationMethod;
import com.metodos.methods.IntegrationResult;
import com.metodos.gui.components.VirtualKeyboard;
import com.metodos.parser.MathParser;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.util.Locale;

public class CalculatorPanel extends JPanel {
    private final MainFrame mainFrame;
    // Inputs
    private JTextField functionField;
    private JTextField limitAField;
    private JTextField limitBField;
    private JTextField subdivisionsField;
    private JLabel subdivisionsLabel;
    
    // Fraction input components
    private JTextField numeratorField;
    private JTextField denominatorField;
    private JPanel functionCardPanel;
    private CardLayout functionCardLayout;
    private JButton btnToggleFraction;
    private boolean isFractionMode = false;
    // Focus tracking
    private JTextField activeField;

    // Output
    private JLabel resultValueLabel;
    private JTextArea stepsArea;
    private JScrollPane stepsScrollPane;
    
    // Layout components
    private VirtualKeyboard virtualKeyboard;

    // Formatting
    private final DecimalFormat df = new DecimalFormat("#.##########");

    public CalculatorPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        
        setLayout(new BorderLayout());
        setOpaque(false);

        initComponents();
    }

    private void initComponents() {
        // --- 1. WORKSPACE PANEL (CENTER) ---
        JPanel workspacePanel = new JPanel(new BorderLayout());
        workspacePanel.setOpaque(false);
        workspacePanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Upper section containing inputs & results in a split or vertical layout
        JPanel upperContent = new JPanel(new GridBagLayout());
        upperContent.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(6, 6, 6, 6);

        // -- INPUTS PANEL --
        JPanel inputsPanel = new JPanel(new GridBagLayout());
        inputsPanel.setBackground(new Color(15, 45, 54)); // Slate Teal container
        inputsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(25, 68, 82), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbcIn = new GridBagConstraints();
        gbcIn.fill = GridBagConstraints.HORIZONTAL;
        gbcIn.insets = new Insets(5, 5, 5, 5);

        // Create the math-style integral panel
        JPanel mathPanel = new JPanel(new GridBagLayout());
        mathPanel.setOpaque(false);
        mathPanel.setBorder(new EmptyBorder(5, 5, 10, 5));

        GridBagConstraints gbcM = new GridBagConstraints();
        gbcM.fill = GridBagConstraints.NONE;
        gbcM.anchor = GridBagConstraints.CENTER;
        gbcM.insets = new Insets(0, 5, 0, 5);

        // Left stacked panel for limits and integral symbol
        JPanel integralLeftPanel = new JPanel(new GridBagLayout());
        integralLeftPanel.setOpaque(false);
        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.gridx = 0;
        gbcL.fill = GridBagConstraints.HORIZONTAL;

        // Upper limit (b)
        limitBField = new JTextField("pi", 4);
        limitBField.setFont(new Font("Consolas", Font.PLAIN, 12));
        limitBField.setHorizontalAlignment(JTextField.CENTER);
        limitBField.setBackground(new Color(23, 62, 74));
        limitBField.setForeground(Color.WHITE);
        limitBField.setCaretColor(Color.WHITE);
        limitBField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 95, 110)),
                new EmptyBorder(2, 4, 2, 4)
        ));
        gbcL.gridy = 0;
        integralLeftPanel.add(limitBField, gbcL);

        // Integral Symbol in the middle
        IntegralSymbol integralSymbol = new IntegralSymbol(new Color(45, 170, 195));
        gbcL.gridy = 1;
        gbcL.fill = GridBagConstraints.NONE;
        gbcL.anchor = GridBagConstraints.CENTER;
        gbcL.insets = new Insets(3, 0, 3, 0);
        integralLeftPanel.add(integralSymbol, gbcL);

        // Lower limit (a)
        limitAField = new JTextField("0", 4);
        limitAField.setFont(new Font("Consolas", Font.PLAIN, 12));
        limitAField.setHorizontalAlignment(JTextField.CENTER);
        limitAField.setBackground(new Color(23, 62, 74));
        limitAField.setForeground(Color.WHITE);
        limitAField.setCaretColor(Color.WHITE);
        limitAField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 95, 110)),
                new EmptyBorder(2, 4, 2, 4)
        ));
        gbcL.gridy = 2;
        gbcL.fill = GridBagConstraints.HORIZONTAL;
        gbcL.insets = new Insets(0, 0, 0, 0);
        integralLeftPanel.add(limitAField, gbcL);

        gbcM.gridx = 0;
        gbcM.gridy = 0;
        gbcM.weightx = 0.0;
        gbcM.fill = GridBagConstraints.NONE;
        mathPanel.add(integralLeftPanel, gbcM);

        // Center function field inside a card layout for fraction support
        functionCardLayout = new CardLayout();
        functionCardPanel = new JPanel(functionCardLayout);
        functionCardPanel.setOpaque(false);

        // Card 1: Single field
        functionField = new JTextField("x^2 * sin(x)", 18);
        functionField.setFont(new Font("Consolas", Font.PLAIN, 16));
        functionField.setBackground(new Color(23, 62, 74));
        functionField.setForeground(Color.WHITE);
        functionField.setCaretColor(Color.WHITE);
        functionField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 95, 110)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        functionCardPanel.add(functionField, "single");

        // Card 2: Fraction Panel
        JPanel fractionPanel = new JPanel();
        fractionPanel.setBackground(new Color(23, 62, 74));
        fractionPanel.setBorder(BorderFactory.createLineBorder(new Color(40, 95, 110)));
        fractionPanel.setLayout(new BoxLayout(fractionPanel, BoxLayout.Y_AXIS));

        numeratorField = new JTextField(18);
        numeratorField.setFont(new Font("Consolas", Font.PLAIN, 16));
        numeratorField.setHorizontalAlignment(JTextField.CENTER);
        numeratorField.setBackground(new Color(23, 62, 74));
        numeratorField.setForeground(Color.WHITE);
        numeratorField.setCaretColor(Color.WHITE);
        numeratorField.setOpaque(false);
        numeratorField.setBorder(new EmptyBorder(4, 10, 4, 10));

        JPanel dividingLine = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(40, 95, 110));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        dividingLine.setPreferredSize(new Dimension(0, 2));
        dividingLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        dividingLine.setOpaque(false);

        denominatorField = new JTextField(18);
        denominatorField.setFont(new Font("Consolas", Font.PLAIN, 16));
        denominatorField.setHorizontalAlignment(JTextField.CENTER);
        denominatorField.setBackground(new Color(23, 62, 74));
        denominatorField.setForeground(Color.WHITE);
        denominatorField.setCaretColor(Color.WHITE);
        denominatorField.setOpaque(false);
        denominatorField.setBorder(new EmptyBorder(4, 10, 4, 10));

        fractionPanel.add(numeratorField);
        fractionPanel.add(dividingLine);
        fractionPanel.add(denominatorField);

        functionCardPanel.add(fractionPanel, "fraction");

        gbcM.gridx = 1;
        gbcM.fill = GridBagConstraints.HORIZONTAL;
        gbcM.weightx = 1.0;
        mathPanel.add(functionCardPanel, gbcM);

        // Right "dx" label
        JLabel dxLabel = new JLabel("dx");
        dxLabel.setFont(new Font("Georgia", Font.ITALIC | Font.BOLD, 22));
        dxLabel.setForeground(new Color(190, 210, 215));
        gbcM.gridx = 2;
        gbcM.fill = GridBagConstraints.NONE;
        gbcM.weightx = 0.0;
        mathPanel.add(dxLabel, gbcM);

        // Fraction toggle button (Col 3)
        btnToggleFraction = new JButton("a/b");
        btnToggleFraction.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        btnToggleFraction.setBackground(new Color(19, 56, 68));
        btnToggleFraction.setForeground(new Color(45, 170, 195));
        btnToggleFraction.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 95, 110)),
                new EmptyBorder(4, 8, 4, 8)
        ));
        btnToggleFraction.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggleFraction.setFocusPainted(false);
        btnToggleFraction.addActionListener(e -> toggleFractionMode());
        
        gbcM.gridx = 3;
        gbcM.fill = GridBagConstraints.NONE;
        gbcM.weightx = 0.0;
        mathPanel.add(btnToggleFraction, gbcM);

        // Add mathPanel as Row 0 spanning full width in inputsPanel
        gbcIn.gridx = 0; gbcIn.gridy = 0; gbcIn.gridwidth = 4;
        gbcIn.weightx = 1.0;
        gbcIn.fill = GridBagConstraints.HORIZONTAL;
        inputsPanel.add(mathPanel, gbcIn);

        // Row 1: Subdivision parameters
        JPanel paramPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        paramPanel.setOpaque(false);

        subdivisionsLabel = new JLabel("Subdivisiones (n):");
        subdivisionsLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        subdivisionsLabel.setForeground(new Color(190, 210, 215));
        paramPanel.add(subdivisionsLabel);

        subdivisionsField = new JTextField("4", 6);
        subdivisionsField.setFont(new Font("Consolas", Font.PLAIN, 14));
        subdivisionsField.setBackground(new Color(23, 62, 74));
        subdivisionsField.setForeground(Color.WHITE);
        subdivisionsField.setCaretColor(Color.WHITE);
        subdivisionsField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 95, 110)),
                new EmptyBorder(4, 6, 4, 6)
        ));
        paramPanel.add(subdivisionsField);

        gbcIn.gridx = 0; gbcIn.gridy = 1; gbcIn.gridwidth = 4;
        gbcIn.insets = new Insets(10, 5, 10, 5);
        inputsPanel.add(paramPanel, gbcIn);

        // Row 2: Buttons
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonRow.setOpaque(false);

        JButton clearBtn = new JButton("Limpiar Campos");
        clearBtn.setBackground(new Color(44, 62, 80));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        clearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearBtn.addActionListener(e -> clearInputs());

        JButton calculateBtn = new JButton("Calcular Integral");
        calculateBtn.setBackground(new Color(29, 120, 242));
        calculateBtn.setForeground(Color.WHITE);
        calculateBtn.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
        calculateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        calculateBtn.addActionListener(e -> performCalculation());

        buttonRow.add(clearBtn);
        buttonRow.add(calculateBtn);

        gbcIn.gridx = 0; gbcIn.gridy = 2; gbcIn.gridwidth = 4;
        gbcIn.insets = new Insets(5, 5, 0, 5);
        inputsPanel.add(buttonRow, gbcIn);

        // Focus Tracking Listeners to wire the Virtual Keyboard
        activeField = functionField; // Default
        FocusAdapter focusAdapter = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                activeField = (JTextField) e.getSource();
                if (virtualKeyboard != null) {
                    virtualKeyboard.setTargetField(activeField);
                }
            }
        };
        functionField.addFocusListener(focusAdapter);
        numeratorField.addFocusListener(focusAdapter);
        denominatorField.addFocusListener(focusAdapter);
        limitAField.addFocusListener(focusAdapter);
        limitBField.addFocusListener(focusAdapter);
        subdivisionsField.addFocusListener(focusAdapter);

        // Transition to denominator when typing '/' in numerator
        numeratorField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                if (e.getKeyChar() == '/') {
                    e.consume(); // prevent '/' character from being typed
                    denominatorField.requestFocusInWindow();
                }
            }
        });

        // Place Inputs Panel in GridBag Layout
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5; gbc.weighty = 0.0;
        upperContent.add(inputsPanel, gbc);

        // -- RESULTS & DETAILS PANEL (Right side of split workspace) --
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(new Color(15, 45, 54));
        resultsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(25, 68, 82), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // Result value display
        JPanel resHeader = new JPanel(new GridLayout(2, 1));
        resHeader.setOpaque(false);
        resHeader.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel resultTitle = new JLabel("RESULTADO APROXIMADO:");
        resultTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        resultTitle.setForeground(new Color(45, 170, 195));
        resHeader.add(resultTitle);

        resultValueLabel = new JLabel("---");
        resultValueLabel.setFont(new Font("Segoe UI Light", Font.BOLD, 28));
        resultValueLabel.setForeground(Color.WHITE);
        resHeader.add(resultValueLabel);

        resultsPanel.add(resHeader, BorderLayout.NORTH);

        // Steps display
        stepsArea = new JTextArea();
        stepsArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        stepsArea.setBackground(new Color(9, 31, 38));
        stepsArea.setForeground(new Color(210, 230, 235));
        stepsArea.setEditable(false);
        stepsArea.setMargin(new Insets(10, 10, 10, 10));

        stepsScrollPane = new JScrollPane(stepsArea);
        stepsScrollPane.setBorder(BorderFactory.createLineBorder(new Color(25, 68, 82)));
        stepsScrollPane.getViewport().setBackground(new Color(9, 31, 38));

        // Encapsulate step scroll view inside a panel that has a toggle
        JPanel stepsContainer = new JPanel(new BorderLayout());
        stepsContainer.setOpaque(false);
        
        JLabel stepTitleLabel = new JLabel("Procedimiento paso a paso:");
        stepTitleLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        stepTitleLabel.setForeground(new Color(190, 210, 215));
        stepTitleLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
        stepsContainer.add(stepTitleLabel, BorderLayout.NORTH);
        stepsContainer.add(stepsScrollPane, BorderLayout.CENTER);

        resultsPanel.add(stepsContainer, BorderLayout.CENTER);

        // Place Results Panel in GridBag Layout
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.5; gbc.weighty = 1.0;
        upperContent.add(resultsPanel, gbc);

        workspacePanel.add(upperContent, BorderLayout.CENTER);
        add(workspacePanel, BorderLayout.CENTER);

        // --- 2. VIRTUAL KEYBOARD PANEL (SOUTH) ---
        virtualKeyboard = new VirtualKeyboard(activeField, this::performCalculation);
        virtualKeyboard.setPreferredSize(new Dimension(0, 260));
        add(virtualKeyboard, BorderLayout.SOUTH);

        // Set subdivision label correctly on load
        onMethodChanged(mainFrame.getCurrentMethod());
    }

    private void clearInputs() {
        functionField.setText("");
        numeratorField.setText("");
        denominatorField.setText("");
        limitAField.setText("");
        limitBField.setText("");
        subdivisionsField.setText("");
        resultValueLabel.setText("---");
        stepsArea.setText("");
        if (isFractionMode) {
            numeratorField.requestFocusInWindow();
        } else {
            functionField.requestFocusInWindow();
        }
    }

    /**
     * Executes the calculation based on user parameters,
     * validates inputs, handles constants like pi and e,
     * parses the math expression, executes the integration algorithm,
     * updates the result label, fills out the step-by-step panel,
     * and updates calculation history.
     */
    public void performCalculation() {
        String rawExpr;
        if (isFractionMode) {
            String num = numeratorField.getText().trim();
            String den = denominatorField.getText().trim();
            if (num.isEmpty() && den.isEmpty()) {
                rawExpr = "";
            } else {
                if (num.isEmpty()) num = "0";
                if (den.isEmpty()) den = "1";
                rawExpr = "(" + num + ") / (" + den + ")";
            }
        } else {
            rawExpr = functionField.getText();
        }
        String rawA = limitAField.getText();
        String rawB = limitBField.getText();
        String rawN = subdivisionsField.getText();

        // 1. Inputs validation
        if (rawExpr.trim().isEmpty() || rawA.trim().isEmpty() || rawB.trim().isEmpty() || rawN.trim().isEmpty()) {
            showError("Todos los campos de entrada son obligatorios.");
            return;
        }

        double a, b;
        int n;
        MathParser parser;

        try {
            // Support pi and e inside integration limits
            String cleanA = rawA.toLowerCase().trim().replace("pi", String.valueOf(Math.PI)).replace("e", String.valueOf(Math.E));
            String cleanB = rawB.toLowerCase().trim().replace("pi", String.valueOf(Math.PI)).replace("e", String.valueOf(Math.E));
            
            // In case of complex limit expressions, we can parse them using MathParser too
            a = evaluateLimit(cleanA);
            b = evaluateLimit(cleanB);
        } catch (Exception e) {
            showError("Los límites de integración 'a' y 'b' deben ser valores numéricos válidos o constantes (pi, e).");
            return;
        }

        try {
            n = Integer.parseInt(rawN.trim());
        } catch (NumberFormatException e) {
            showError("El campo de subdivisiones o bloques debe ser un número entero positivo.");
            return;
        }

        try {
            parser = new MathParser(rawExpr);
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
            return;
        }

        // 2. Integration Execution
        IntegrationMethod method = mainFrame.getCurrentMethod();
        try {
            IntegrationResult calcResult = method.calculate(parser, a, b, n);
            
            // Format and display numerical result
            double result = calcResult.getResult();
            resultValueLabel.setText(df.format(result));
            
            // Display step logs
            StringBuilder stepsText = new StringBuilder();
            for (String step : calcResult.getSteps()) {
                stepsText.append(step).append("\n");
            }
            stepsArea.setText(stepsText.toString());
            stepsArea.setCaretPosition(0); // scroll to top

            // Add entry to history
            String historyEntry = String.format(Locale.US,
                    "Método: %s\nFunción: f(x) = %s\nLímites: [%.4f, %.4f]  |  n = %d\nResultado: %.10f\nFecha: %s\n-----------------------",
                    method.getName(), rawExpr, a, b, n, result, java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
            mainFrame.addHistoryEntry(historyEntry);

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (ArithmeticException e) {
            showError(e.getMessage());
        }
    }

    private double evaluateLimit(String input) throws Exception {
        // Simple evaluator for limit variables
        // If it's a direct double
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            // Try evaluating through exp4j
            MathParser p = new MathParser(input);
            return p.evaluate(0); // evaluate with 0 (no variables expected)
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error en los datos ingresados", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Responds to changes in selected method, updating input labels dynamically.
     */
    public void onMethodChanged(IntegrationMethod method) {
        if (method.getName().equals("Método de Simpson Abierto")) {
            subdivisionsLabel.setText("Bloques (N):");
            subdivisionsField.setText("2"); // default blocks
        } else {
            subdivisionsLabel.setText("Subdivisiones (n):");
            if (method.getName().equals("Método de Simpson 1/3")) {
                subdivisionsField.setText("4"); // default even
            } else if (method.getName().equals("Método de Simpson 3/8")) {
                subdivisionsField.setText("6"); // default mult of 3
            } else if (method.getName().equals("Método de Boole")) {
                subdivisionsField.setText("4"); // default mult of 4
            } else {
                subdivisionsField.setText("4"); // default trapezoidal
            }
        }
        resultValueLabel.setText("---");
        stepsArea.setText("");
    }

    public void toggleFractionMode() {
        isFractionMode = !isFractionMode;
        if (isFractionMode) {
            // Transitioning from Simple to Fraction
            String currentText = functionField.getText().trim();
            if (currentText.contains("/")) {
                // Try to split into numerator and denominator
                int slashIdx = currentText.indexOf('/');
                String num = currentText.substring(0, slashIdx).trim();
                String den = currentText.substring(slashIdx + 1).trim();
                // Clean outer parentheses if any
                if (num.startsWith("(") && num.endsWith(")")) num = num.substring(1, num.length() - 1);
                if (den.startsWith("(") && den.endsWith(")")) den = den.substring(1, den.length() - 1);
                numeratorField.setText(num);
                denominatorField.setText(den);
            } else {
                numeratorField.setText(currentText);
                denominatorField.setText("");
            }
            functionCardLayout.show(functionCardPanel, "fraction");
            numeratorField.requestFocusInWindow();
            activeField = numeratorField;
            if (virtualKeyboard != null) {
                virtualKeyboard.setTargetField(numeratorField);
            }
        } else {
            // Transitioning from Fraction to Simple
            String num = numeratorField.getText().trim();
            String den = denominatorField.getText().trim();
            String combined = "";
            if (!num.isEmpty() && !den.isEmpty()) {
                combined = "(" + num + ") / (" + den + ")";
            } else if (!num.isEmpty()) {
                combined = num;
            }
            functionField.setText(combined);
            functionCardLayout.show(functionCardPanel, "single");
            functionField.requestFocusInWindow();
            activeField = functionField;
            if (virtualKeyboard != null) {
                virtualKeyboard.setTargetField(functionField);
            }
        }
        
        // Update toggle button style or text
        btnToggleFraction.setText(isFractionMode ? "x/y" : "a/b");
        btnToggleFraction.setForeground(isFractionMode ? new Color(74, 222, 128) : new Color(45, 170, 195)); // Green if active, cyan otherwise
    }

    private static class IntegralSymbol extends JComponent {
        private final Color color;

        public IntegralSymbol(Color color) {
            this.color = color;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            int w = getWidth();
            int h = getHeight();
            
            int topX = w * 3 / 4;
            int topY = 4;
            int botX = w / 4;
            int botY = h - 4;
            
            g2.draw(new java.awt.geom.CubicCurve2D.Float(
                topX, topY,
                w / 10.0f, topY,
                w * 9.0f / 10.0f, botY,
                botX, botY
            ));
            
            g2.fillOval(topX - 2, topY - 2, 4, 4);
            g2.fillOval(botX - 2, botY - 2, 4, 4);
            
            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(28, 70);
        }
    }
}


