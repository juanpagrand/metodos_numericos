package com.metodos.methods;

import com.metodos.parser.MathParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BooleMethod implements IntegrationMethod {
    @Override
    public String getName() {
        return "Método de Boole";
    }

    @Override
    public String getDescription() {
        return "Aproxima la integral usando polinomios de cuarto grado. Requiere que n sea múltiplo de 4 (n >= 4).";
    }

    @Override
    public IntegrationResult calculate(MathParser parser, double a, double b, int n) throws IllegalArgumentException, ArithmeticException {
        if (n < 4) {
            throw new IllegalArgumentException("El número de subdivisiones (n) debe ser mayor o igual a 4 para el Método de Boole.");
        }
        if (n % 4 != 0) {
            throw new IllegalArgumentException("El número de subdivisiones (n) debe ser un MÚLTIPLO DE 4 para el Método de Boole.");
        }

        double h = (b - a) / n;
        double[] xValues = new double[n + 1];
        double[] yValues = new double[n + 1];

        // Evaluate at all nodes
        for (int i = 0; i <= n; i++) {
            xValues[i] = a + i * h;
            if (i == n) {
                xValues[i] = b;
            }
            yValues[i] = parser.evaluate(xValues[i]);
        }

        // Apply Boole's Composite Rule and gather terms
        double sumTerms = 0;
        double[] terms = new double[n + 1];
        int[] weights = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            int w;
            if (i == 0 || i == n) {
                w = 7;
            } else if (i % 4 == 0) {
                w = 14;
            } else if (i % 2 == 0) {
                w = 12;
            } else {
                w = 32;
            }
            weights[i] = w;
            terms[i] = w * yValues[i];
            sumTerms += terms[i];
        }

        double result = (2.0 * h / 45.0) * sumTerms;

        // Generate steps
        List<String> steps = new ArrayList<>();
        steps.add("--- MÉTODO DE BOOLE COMPUESTO ---");
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
        steps.add("   Integral ≈ (2h / 45) * Suma");
        steps.add(String.format(Locale.US, "   Integral ≈ (2 * %.8f / 45) * %.8f", h, sumTerms));
        steps.add(String.format(Locale.US, "   Integral ≈ %.8f * %.8f", (2.0 * h / 45.0), sumTerms));
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
