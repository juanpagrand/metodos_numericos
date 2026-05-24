package com.metodos.gui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VirtualKeyboard extends JPanel {
    private JTextField targetField;
    private final CardLayout cardLayout;
    private final JPanel cardsPanel;
    private Runnable onEnterAction;

    // Color definitions to match guide image
    private final Color bgColor = new Color(243, 244, 246); // Very light grey bg
    private final Color btnBgNumber = new Color(229, 231, 235); // Light grey number btn
    private final Color btnBgFunc = new Color(255, 255, 255); // White function btn
    private final Color btnBgAction = new Color(209, 213, 219); // Darker action btn
    private final Color btnBgEnter = new Color(37, 99, 235); // Blue enter btn
    private final Color textColorDark = new Color(31, 41, 55); // Near black text
    private final Color textColorLight = Color.WHITE;

    public VirtualKeyboard(JTextField targetField, Runnable onEnterAction) {
        this.targetField = targetField;
        this.onEnterAction = onEnterAction;

        setLayout(new BorderLayout());
        setBackground(bgColor);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(209, 213, 219)),
                new EmptyBorder(10, 15, 10, 15)
        ));

        // 1. TOP BAR - TAB SELECTORS & ACTIONS
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(0, 0, 8, 0));

        // Tab selection panel (Left)
        JPanel tabSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        tabSelectionPanel.setOpaque(false);

        JButton btnTabPpal = createTabButton("ppal", true);
        JButton btnTabAbc = createTabButton("abc", false);
        JButton btnTabFnc = createTabButton("fnc", false);

        tabSelectionPanel.add(btnTabPpal);
        tabSelectionPanel.add(btnTabAbc);
        tabSelectionPanel.add(btnTabFnc);

        // Angle unit selection (Center)
        JPanel angleUnitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        angleUnitPanel.setOpaque(false);
        JButton radBtn = new JButton("RAD");
        radBtn.setFont(new Font("Segoe UI", Font.BOLD, 10));
        radBtn.setBackground(new Color(219, 234, 254)); // Light blue selected
        radBtn.setForeground(new Color(29, 78, 216));
        radBtn.setFocusPainted(false);
        radBtn.setPreferredSize(new Dimension(42, 22));
        radBtn.setBorder(BorderFactory.createLineBorder(new Color(191, 219, 254)));
        radBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton gradBtn = new JButton("GRAD");
        gradBtn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        gradBtn.setBackground(Color.WHITE);
        gradBtn.setForeground(Color.GRAY);
        gradBtn.setFocusPainted(false);
        gradBtn.setPreferredSize(new Dimension(45, 22));
        gradBtn.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
        gradBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        radBtn.addActionListener(e -> {
            radBtn.setBackground(new Color(219, 234, 254));
            radBtn.setForeground(new Color(29, 78, 216));
            gradBtn.setBackground(Color.WHITE);
            gradBtn.setForeground(Color.GRAY);
        });
        gradBtn.addActionListener(e -> {
            gradBtn.setBackground(new Color(219, 234, 254));
            gradBtn.setForeground(new Color(29, 78, 216));
            radBtn.setBackground(Color.WHITE);
            radBtn.setForeground(Color.GRAY);
        });

        angleUnitPanel.add(radBtn);
        angleUnitPanel.add(gradBtn);
        tabSelectionPanel.add(angleUnitPanel);

        topBar.add(tabSelectionPanel, BorderLayout.WEST);

        // Actions panel (Right)
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setOpaque(false);

        JButton clearBtn = new JButton("borrar todo");
        clearBtn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        clearBtn.setForeground(textColorDark);
        clearBtn.setContentAreaFilled(false);
        clearBtn.setBorder(null);
        clearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearBtn.addActionListener(e -> {
            if (this.targetField != null) {
                this.targetField.setText("");
                this.targetField.requestFocusInWindow();
            }
        });

        JLabel wrenchIcon = new JLabel("🔧");
        wrenchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        wrenchIcon.setForeground(textColorDark);
        wrenchIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        actionsPanel.add(clearBtn);
        actionsPanel.add(wrenchIcon);
        topBar.add(actionsPanel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // 2. MAIN KEYPAD PANELS (CardLayout)
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setOpaque(false);

        // Initialize Cards
        JPanel ppalCard = createPpalCard();
        JPanel abcCard = createAbcCard();
        JPanel fncCard = createFncCard();

        cardsPanel.add(ppalCard, "ppal");
        cardsPanel.add(abcCard, "abc");
        cardsPanel.add(fncCard, "fnc");

        add(cardsPanel, BorderLayout.CENTER);

        // Tab Switching Actions
        btnTabPpal.addActionListener(e -> {
            setTabActive(btnTabPpal, btnTabAbc, btnTabFnc);
            cardLayout.show(cardsPanel, "ppal");
        });
        btnTabAbc.addActionListener(e -> {
            setTabActive(btnTabAbc, btnTabPpal, btnTabFnc);
            cardLayout.show(cardsPanel, "abc");
        });
        btnTabFnc.addActionListener(e -> {
            setTabActive(btnTabFnc, btnTabPpal, btnTabAbc);
            cardLayout.show(cardsPanel, "fnc");
        });
    }

    public void setTargetField(JTextField targetField) {
        this.targetField = targetField;
    }

    private JButton createTabButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(null);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (active) {
            btn.setForeground(new Color(37, 99, 235)); // Blue active text
            btn.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(37, 99, 235)));
        } else {
            btn.setForeground(Color.GRAY);
        }
        return btn;
    }

    private void setTabActive(JButton active, JButton... inactives) {
        active.setForeground(new Color(37, 99, 235));
        active.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(37, 99, 235)));
        for (JButton btn : inactives) {
            btn.setForeground(Color.GRAY);
            btn.setBorder(null);
        }
    }

    private JButton createKey(String label, String insertValue, Color bg, Color fg) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });

        btn.addActionListener(e -> {
            if (insertValue != null) {
                if (insertValue.equals("BACKSPACE")) {
                    backspace();
                } else if (insertValue.equals("LEFT")) {
                    moveCaret(false);
                } else if (insertValue.equals("RIGHT")) {
                    moveCaret(true);
                } else if (insertValue.equals("ENTER")) {
                    if (onEnterAction != null) onEnterAction.run();
                } else {
                    insertText(insertValue);
                }
            }
        });

        return btn;
    }

    // Grid Keyboards Creator
    private JPanel createPpalCard() {
        JPanel panel = new JPanel(new GridLayout(4, 9, 6, 6));
        panel.setOpaque(false);

        // Row 1
        panel.add(createKey("a²", "^2", btnBgFunc, textColorDark));
        panel.add(createKey("aᵇ", "^", btnBgFunc, textColorDark));
        panel.add(createKey("|a|", "abs(", btnBgFunc, textColorDark));
        panel.add(createKey("7", "7", btnBgNumber, textColorDark));
        panel.add(createKey("8", "8", btnBgNumber, textColorDark));
        panel.add(createKey("9", "9", btnBgNumber, textColorDark));
        panel.add(createKey("÷", "/", btnBgFunc, textColorDark));
        panel.add(createKey("%", "%", btnBgFunc, textColorDark));
        panel.add(createKey("a/b", "/", btnBgFunc, textColorDark));

        // Row 2
        panel.add(createKey("√", "sqrt(", btnBgFunc, textColorDark));
        panel.add(createKey("ⁿ√", "^(1/", btnBgFunc, textColorDark));
        panel.add(createKey("π", "pi", btnBgFunc, textColorDark));
        panel.add(createKey("4", "4", btnBgNumber, textColorDark));
        panel.add(createKey("5", "5", btnBgNumber, textColorDark));
        panel.add(createKey("6", "6", btnBgNumber, textColorDark));
        panel.add(createKey("×", "*", btnBgFunc, textColorDark));
        panel.add(createKey("←", "LEFT", btnBgAction, textColorDark));
        panel.add(createKey("→", "RIGHT", btnBgAction, textColorDark));

        // Row 3
        panel.add(createKey("sin", "sin(", btnBgFunc, textColorDark));
        panel.add(createKey("cos", "cos(", btnBgFunc, textColorDark));
        panel.add(createKey("tan", "tan(", btnBgFunc, textColorDark));
        panel.add(createKey("1", "1", btnBgNumber, textColorDark));
        panel.add(createKey("2", "2", btnBgNumber, textColorDark));
        panel.add(createKey("3", "3", btnBgNumber, textColorDark));
        panel.add(createKey("-", "-", btnBgFunc, textColorDark));
        panel.add(createKey("DEL", "BACKSPACE", btnBgAction, textColorDark));
        // Empty placeholder space or expand the previous button
        JPanel placeHolder = new JPanel();
        placeHolder.setOpaque(false);
        panel.add(placeHolder);

        // Row 4
        panel.add(createKey("(", "(", btnBgFunc, textColorDark));
        panel.add(createKey(")", ")", btnBgFunc, textColorDark));
        panel.add(createKey("x", "x", btnBgFunc, textColorDark)); // replace comma with variable x for integration
        panel.add(createKey("0", "0", btnBgNumber, textColorDark));
        panel.add(createKey(".", ".", btnBgNumber, textColorDark));
        panel.add(createKey("ans", "ans", btnBgFunc, textColorDark));
        panel.add(createKey("+", "+", btnBgFunc, textColorDark));
        panel.add(createKey("CALC", "ENTER", btnBgEnter, textColorLight));
        // Empty placeholder space
        JPanel placeHolder2 = new JPanel();
        placeHolder2.setOpaque(false);
        panel.add(placeHolder2);

        return panel;
    }

    private JPanel createAbcCard() {
        JPanel panel = new JPanel(new GridLayout(3, 10, 4, 4));
        panel.setOpaque(false);

        String[] row1 = {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p"};
        String[] row2 = {"a", "s", "d", "f", "g", "h", "j", "k", "l", "x"};
        String[] row3 = {"z", "c", "v", "b", "n", "m", "(", ")", "pi", "e"};

        for (String key : row1) panel.add(createKey(key, key, btnBgFunc, textColorDark));
        for (String key : row2) panel.add(createKey(key, key, btnBgFunc, textColorDark));
        for (String key : row3) panel.add(createKey(key, key, btnBgFunc, textColorDark));

        return panel;
    }

    private JPanel createFncCard() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 6, 6));
        panel.setOpaque(false);

        String[][] functions = {
            {"ln(a)", "ln("}, {"log(a)", "log("}, {"exp(a)", "exp("}, {"sqrt(a)", "sqrt("},
            {"asin(a)", "asin("}, {"acos(a)", "acos("}, {"atan(a)", "atan("}, {"abs(a)", "abs("},
            {"sinh(a)", "sinh("}, {"cosh(a)", "cosh("}, {"tanh(a)", "tanh("}, {"x", "x"}
        };

        for (String[] pair : functions) {
            panel.add(createKey(pair[0], pair[1], btnBgFunc, textColorDark));
        }

        return panel;
    }

    private void insertText(String text) {
        if (targetField == null) return;
        int caretPosition = targetField.getCaretPosition();
        String currentText = targetField.getText();
        String before = currentText.substring(0, caretPosition);
        String after = currentText.substring(caretPosition);
        targetField.setText(before + text + after);
        targetField.setCaretPosition(caretPosition + text.length());
        targetField.requestFocusInWindow();
    }

    private void backspace() {
        if (targetField == null) return;
        int caretPosition = targetField.getCaretPosition();
        if (caretPosition == 0) return;
        String currentText = targetField.getText();
        String before = currentText.substring(0, caretPosition - 1);
        String after = currentText.substring(caretPosition);
        targetField.setText(before + after);
        targetField.setCaretPosition(caretPosition - 1);
        targetField.requestFocusInWindow();
    }

    private void moveCaret(boolean forward) {
        if (targetField == null) return;
        int caretPosition = targetField.getCaretPosition();
        if (forward && caretPosition < targetField.getText().length()) {
            targetField.setCaretPosition(caretPosition + 1);
        } else if (!forward && caretPosition > 0) {
            targetField.setCaretPosition(caretPosition - 1);
        }
        targetField.requestFocusInWindow();
    }
}
