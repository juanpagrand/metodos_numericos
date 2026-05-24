package com.metodos.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class HistoryPanel extends JPanel {
    private final MainFrame mainFrame;
    private JPanel listPanel;
    private JScrollPane scrollPane;

    public HistoryPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initComponents();
    }

    private void initComponents() {
        // TOP SECTION: TITLE & CLEAR BUTTON
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel titleLbl = new JLabel("Historial de operaciones recientes");
        titleLbl.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
        titleLbl.setForeground(new Color(190, 210, 215));
        topRow.add(titleLbl, BorderLayout.WEST);

        JButton clearBtn = new JButton("Borrar Historial");
        clearBtn.setBackground(new Color(127, 29, 29)); // Dark red
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        clearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de que deseas borrar todo el historial?",
                    "Confirmar borrado",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                mainFrame.clearHistory();
            }
        });
        topRow.add(clearBtn, BorderLayout.EAST);

        add(topRow, BorderLayout.NORTH);

        // LIST CONTAINER
        listPanel = new JPanel();
        listPanel.setBackground(new Color(11, 37, 46));
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(25, 68, 82), 1, true));
        scrollPane.getViewport().setBackground(new Color(11, 37, 46));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        refreshHistory();
    }

    /**
     * Rebuilds the list entries from the history log inside MainFrame.
     */
    public void refreshHistory() {
        listPanel.removeAll();
        List<String> history = mainFrame.getCalculationHistory();

        if (history.isEmpty()) {
            listPanel.setLayout(new GridBagLayout());
            JLabel emptyLbl = new JLabel("No se han realizado cálculos todavía.");
            emptyLbl.setFont(new Font("Segoe UI Light", Font.ITALIC, 14));
            emptyLbl.setForeground(new Color(120, 140, 145));
            listPanel.add(emptyLbl);
        } else {
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
            listPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

            for (String entry : history) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(new Color(15, 45, 54));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(45, 170, 195)), // cyan left edge
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(25, 68, 82)),
                                new EmptyBorder(12, 15, 12, 15)
                        )
                ));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

                JTextArea text = new JTextArea(entry);
                text.setFont(new Font("Consolas", Font.PLAIN, 12));
                text.setForeground(new Color(210, 230, 235));
                text.setOpaque(false);
                text.setEditable(false);

                card.add(text, BorderLayout.CENTER);
                
                listPanel.add(card);
                listPanel.add(Box.createVerticalStrut(10)); // spacing between cards
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }
}
