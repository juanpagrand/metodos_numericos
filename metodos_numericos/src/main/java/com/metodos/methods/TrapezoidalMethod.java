package com.metodos.methods;

import com.metodos.parser.MathParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrapezoidalMethod implements IntegrationMethod {
    @Override
    public String getName() {
        return "Método Trapezoidal";
    }

    @Override
    public String getDescription() {
        return "Aproxima la integral dividiendo el área en trapecios. Permite cualquier número de subdivisiones (n >= 1).";
    }

    @Override
    public IntegrationResult calculate(MathParser parser, double a, double b, int n) throws IllegalArgumentException, ArithmeticException {
        if (n < 1) {
            throw new IllegalArgumentException("El número de subdivisiones (n) debe ser mayor o igual a 1 para el Método Trapezoidal.");
        }

        double h = (b - a) / n;
        double[] xValues = new double[n + 1];
        double[] yValues = new double[n + 1];

        // Evaluate at all nodes
        for (int i = 0; i <= n; i++) {
            xValues[i] = a + i * h;
            // Handle precision issue for the last element
            if (i == n) {
                xValues[i] = b;
            }
            yValues[i] = parser.evaluate(xValues[i]);
        }

        // Apply Trapezoidal Rule formula and gather terms
        double sumTerms = 0;
        double[] terms = new double[n + 1];
        int[] weights = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            int w = (i == 0 || i == n) ? 1 : 2;
            weights[i] = w;
            terms[i] = w * yValues[i];
            sumTerms += terms[i];
        }

        double result = (h / 2.0) * sumTerms;

        // Generate Step-by-Step logs
        List<String> steps = new ArrayList<>();
        steps.add("--- MÉTODO TRAPEZOIDAL COMPUESTO ---");
        steps.add(String.format(Locale.US, "1. Calcular el tamaño de paso (h):\n   h = (b - a) / n = (%.6f - %.6f) / %d = %.8f", b, a, n, h));
        
        steps.add("\n2. Tabulación de puntos evaluados y cálculo de términos:");
        steps.add(String.format(Locale.US, "   %-9s | %-12s | %-12s | %-10s | %s", "Punto (i)", "x_i", "f(x_i)", "Peso (w_i)", "Término (w_i * f(x_i))"));
        steps.add("   --------------------------------------------------------------------------------------------------------");
        
        for (int i = 0; i <= n; i++) {
            String exprWithVal = parser.getExpressionString().replaceAll("\\bx\\b", formatDouble(xValues[i]));
            String termExpression = String.format(Locale.US, "%d * %s = %.8f", weights[i], exprWithVal, terms[i]);
            steps.add(String.format(Locale.US, "   i = %-5d | %-12s | %-12.8f | %-10d | %s", 
                    i, formatDouble(xValues[i]), yValues[i], weights[i], termExpression));
        }
        steps.add("   --------------------------------------------------------------------------------------------------------");

        steps.add("\n3. Suma total de los términos (corchete):");
        StringBuilder sumExpression = new StringBuilder();
        for (int i = 0; i <= n; i++) {
            sumExpression.append(String.format(Locale.US, "%.8f", terms[i]));
            if (i < n) {
                sumExpression.append(" + ");
            }
        }
        steps.add(String.format(Locale.US, "   Suma = %s", sumExpression.toString()));
        steps.add(String.format(Locale.US, "   Suma = %.8f", sumTerms));

        steps.add("\n4. Aplicar la fórmula final:");
        steps.add("   Integral ≈ (h / 2) * Suma");
        steps.add(String.format(Locale.US, "   Integral ≈ (%.8f / 2) * %.8f", h, sumTerms));
        steps.add(String.format(Locale.US, "   Integral ≈ %.8f * %.8f", (h / 2.0), sumTerms));
        steps.add(String.format(Locale.US, "   Integral ≈ %.10f", result));

        steps.add("\n5. Tabulación de puntos evaluados (sin pesos):");
        steps.add(String.format(Locale.US, "   %-9s | %-12s | %s", "Punto (i)", "x_i", "f(x_i)"));
        steps.add("   -------------------------------------------------");
        for (int i = 0; i <= n; i++) {
            steps.add(String.format(Locale.US, "   i = %-5d | %-12s | %.8f", 
                    i, formatDouble(xValues[i]), yValues[i]));
        }
        steps.add("   -------------------------------------------------");

        steps.add("\n6. Gráfica de los puntos evaluados (eje X = x_i, eje Y = f(x_i)):");
        steps.add(generateASCIIPlot(xValues, yValues));

        return new IntegrationResult(result, h, xValues, yValues, steps);
    }

    private String formatDouble(double val) {
        if (val == (long) val) {
            return String.format(Locale.US, "%d", (long) val);
        } else {
            String s = String.format(Locale.US, "%.6f", val);
            while (s.endsWith("0") && s.contains(".")) {
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")) {
                s = s.substring(0, s.length() - 1);
            }
            return s;
        }
    }
}
