package com.metodos.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HelpPanel extends JPanel {

    public HelpPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initComponents();
    }

    private void initComponents() {
        // Scrollable content area
        JTextPane helpText = new JTextPane();
        helpText.setContentType("text/html");
        helpText.setEditable(false);
        helpText.setBackground(new Color(15, 45, 54)); // slate teal background
        helpText.setForeground(Color.WHITE);
        helpText.setMargin(new Insets(15, 15, 15, 15));

        // Format HTML content
        String htmlContent = "<html>" +
                "<style>" +
                "  body { font-family: 'Segoe UI', sans-serif; color: #e5e7eb; background-color: #0f2d36; margin: 10px; }" +
                "  h1 { color: #2daac3; font-size: 20px; border-bottom: 1px solid #1a5160; padding-bottom: 5px; margin-top: 20px; }" +
                "  h2 { color: #4ade80; font-size: 16px; margin-top: 15px; }" +
                "  p { font-size: 13px; line-height: 1.5; margin-bottom: 10px; }" +
                "  ul { font-size: 13px; margin-left: 20px; }" +
                "  li { margin-bottom: 5px; }" +
                "  .formula { font-family: 'Consolas', monospace; background-color: #0b252e; color: #facc15; padding: 10px; border-radius: 6px; margin: 10px 0; border: 1px solid #133c4a; }" +
                "  .note { background-color: #1e3a8a; border-left: 4px solid #3b82f6; padding: 10px; font-size: 12px; border-radius: 4px; color: #bfdbfe; margin-top: 10px; }" +
                "  .warning { background-color: #7f1d1d; border-left: 4px solid #f87171; padding: 10px; font-size: 12px; border-radius: 4px; color: #fecaca; margin-top: 10px; }" +
                "</style>" +
                "<body>" +
                "  <h1 style='margin-top:0;'>Guía de Métodos de Integración Numérica</h1>" +
                "  <p>Esta calculadora permite aproximar el valor de una integral definida usando diferentes fórmulas de Newton-Cotes. A continuación se presentan los detalles de cada método:</p>" +

                "  <h1>1. Método Trapezoidal</h1>" +
                "  <p>Aproxima el área bajo la curva dividiendo el intervalo [a, b] en <i>n</i> trapecios. Es el método más sencillo pero requiere un mayor número de intervalos para lograr alta precisión.</p>" +
                "  <h2>Fórmula Compuesta:</h2>" +
                "  <div class='formula'>Integral ≈ (h / 2) * [ f(a) + 2 * Σ(f(x<sub>i</sub>)) + f(b) ]</div>" +
                "  <div class='note'><strong>Restricción:</strong> Permite cualquier número de intervalos (n ≥ 1). Paso h = (b - a) / n.</div>" +

                "  <h1>2. Método de Simpson 1/3</h1>" +
                "  <p>Aproxima la función en cada par de intervalos usando un polinomio de segundo grado (parábola). Ofrece una excelente precisión en comparación con el método trapezoidal.</p>" +
                "  <h2>Fórmula Compuesta:</h2>" +
                "  <div class='formula'>Integral ≈ (h / 3) * [ f(a) + 4 * Σ(f(x<sub>impares</sub>)) + 2 * Σ(f(x<sub>pares</sub>)) + f(b) ]</div>" +
                "  <div class='warning'><strong>Restricción obligatoria:</strong> El número de intervalos (n) debe ser <b>PAR</b> (n ≥ 2). Paso h = (b - a) / n.</div>" +

                "  <h1>3. Método de Simpson 3/8</h1>" +
                "  <p>Aproxima la función en cada trío de intervalos mediante un polinomio de tercer grado (cúbico). Es especialmente útil en intervalos impares múltiplos de 3.</p>" +
                "  <h2>Fórmula Compuesta:</h2>" +
                "  <div class='formula'>Integral ≈ (3h / 8) * [ f(a) + 3 * Σ(f(x<sub>no múltiplos de 3</sub>)) + 2 * Σ(f(x<sub>múltiplos de 3</sub>)) + f(b) ]</div>" +
                "  <div class='warning'><strong>Restricción obligatoria:</strong> El número de intervalos (n) debe ser <b>MÚLTIPLO DE 3</b> (n ≥ 3). Paso h = (b - a) / n.</div>" +

                "  <h1>4. Método de Boole</h1>" +
                "  <p>Llamado en honor a George Boole. Utiliza polinomios de cuarto grado interpolados sobre 5 puntos equiespaciados para aproximar el área. Posee un orden de convergencia muy alto.</p>" +
                "  <h2>Fórmula Compuesta:</h2>" +
                "  <div class='formula'>Integral ≈ (2h / 45) * [ 7*f(a) + 32*Σ(f(x<sub>impares</sub>)) + 12*Σ(f(x<sub>pares no mult 4</sub>)) + 14*Σ(f(x<sub>mult 4</sub>)) + 7*f(b) ]</div>" +
                "  <div class='warning'><strong>Restricción obligatoria:</strong> El número de intervalos (n) debe ser <b>MÚLTIPLO DE 4</b> (n ≥ 4). Paso h = (b - a) / n.</div>" +

                "  <h1>5. Método de Simpson Abierto (Fórmula Abierta de 3 Puntos)</h1>" +
                "  <p>Es una fórmula de Newton-Cotes abierta de 3 puntos. <b>No evalúa la función en los límites de integración (a y b)</b>. Esto es extremadamente útil para integrales donde el integrando está indeterminado en los extremos (por ejemplo, f(x) = 1/&radic;x con límites [0, 1]).</p>" +
                "  <h2>Fórmula de Bloque:</h2>" +
                "  <p>Para cada bloque [x<sub>j</sub>, x<sub>j+1</sub>] de ancho H = (b - a) / N, definimos h = H / 4 y calculamos:</p>" +
                "  <div class='formula'>Integral_Bloque ≈ (4h / 3) * [ 2*f(x<sub>j</sub>+h) - f(x<sub>j</sub>+2h) + 2*f(x<sub>j</sub>+3h) ]</div>" +
                "  <div class='note'><strong>Restricción:</strong> Permite cualquier número de bloques (N ≥ 1). No evalúa los extremos.</div>" +

                "</body></html>";

        helpText.setText(htmlContent);
        helpText.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(helpText);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(25, 68, 82), 1, true));
        add(scrollPane, BorderLayout.CENTER);
    }
}
