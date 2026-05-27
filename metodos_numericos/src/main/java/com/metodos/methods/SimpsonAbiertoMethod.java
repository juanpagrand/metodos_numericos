package com.metodos.methods;

import com.metodos.parser.MathParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SimpsonAbiertoMethod implements IntegrationMethod {
    @Override
    public String getName() {
        return "Método de Simpson Abierto";
    }

    @Override
    public String getDescription() {
        return "Fórmula abierta de Newton-Cotes de 3 puntos. Útil cuando la función está indeterminada en los extremos (a o b). Requiere n >= 1 bloques.";
    }

    @Override
    public IntegrationResult calculate(MathParser parser, double a, double b, int n) throws IllegalArgumentException, ArithmeticException {
        if (n < 1) {
            throw new IllegalArgumentException("El número de bloques (n) debe ser mayor o igual a 1 para el Método de Simpson Abierto.");
        }

        // H is the width of each of the n blocks
        double H = (b - a) / n;
        // h is the sub-step size within a block (H / 4)
        double h = H / 4.0;

        int totalPoints = n * 3;
        double[] xValues = new double[totalPoints];
        double[] yValues = new double[totalPoints];

        double totalIntegral = 0;
        List<String> steps = new ArrayList<>();
        steps.add("--- MÉTODO DE SIMPSON ABIERTO COMPUESTO (3 PUNTOS) ---");
        steps.add(String.format(Locale.US, "1. Parámetros de división:\n   Número de bloques (N) = %d\n   Ancho de bloque (H) = (b - a) / N = %.6f\n   Paso de integración (h) = H / 4 = %.8f", n, H, h));
        
        steps.add("\n2. Evaluación de puntos internos por bloque (no se evalúan los extremos a ni b):");
        steps.add(String.format(Locale.US, "   %-7s | %-12s | %-12s | %-10s | %s", "Bloque", "x_i", "f(x_i)", "Peso (w)", "Término (w * f(x_i))"));
        steps.add("   --------------------------------------------------------------------------------------------------------");

        int pointIndex = 0;
        for (int j = 0; j < n; j++) {
            double blockA = a + j * H;
            double blockB = blockA + H;
            
            double x1 = blockA + h;
            double x2 = blockA + 2.0 * h;
            double x3 = blockA + 3.0 * h;

            double y1 = parser.evaluate(x1);
            double y2 = parser.evaluate(x2);
            double y3 = parser.evaluate(x3);

            xValues[pointIndex] = x1;
            yValues[pointIndex] = y1;
            pointIndex++;

            xValues[pointIndex] = x2;
            yValues[pointIndex] = y2;
            pointIndex++;

            xValues[pointIndex] = x3;
            yValues[pointIndex] = y3;
            pointIndex++;

            // Local block integral: (4h/3) * (2*f(x1) - f(x2) + 2*f(x3))
            double blockIntegral = (4.0 * h / 3.0) * (2.0 * y1 - y2 + 2.0 * y3);
            totalIntegral += blockIntegral;

            String exprX1 = parser.getExpressionString().replaceAll("\\bx\\b", formatDouble(x1));
            String exprX2 = parser.getExpressionString().replaceAll("\\bx\\b", formatDouble(x2));
            String exprX3 = parser.getExpressionString().replaceAll("\\bx\\b", formatDouble(x3));

            steps.add(String.format(Locale.US, "   %-7d | %-12s | %-12.8f | %-10d | 2 * %s = %.8f", j + 1, formatDouble(x1), y1, 2, exprX1, 2.0 * y1));
            steps.add(String.format(Locale.US, "   %-7s | %-12s | %-12.8f | %-10d | -1 * %s = %.8f", "", formatDouble(x2), y2, -1, exprX2, -1.0 * y2));
            steps.add(String.format(Locale.US, "   %-7s | %-12s | %-12.8f | %-10d | 2 * %s = %.8f", "", formatDouble(x3), y3, 2, exprX3, 2.0 * y3));
            steps.add(String.format(Locale.US, "   --> Integral del bloque %d = (4h/3) * [ 2*f(x1) - f(x2) + 2*f(x3) ] = %.8f", j + 1, blockIntegral));
            steps.add("   --------------------------------------------------------------------------------------------------------");
        }

        steps.add(String.format(Locale.US, "\n3. Suma total de integrales de los bloques:\n   Integral ≈ %.10f", totalIntegral));

        steps.add("\n5. Tabulación de puntos evaluados (sin pesos):");
        steps.add(String.format(Locale.US, "   %-9s | %-12s | %s", "Punto (i)", "x_i", "f(x_i)"));
        steps.add("   -------------------------------------------------");
        for (int i = 0; i < totalPoints; i++) {
            steps.add(String.format(Locale.US, "   i = %-5d | %-12s | %.8f", 
                    i + 1, formatDouble(xValues[i]), yValues[i]));
        }
        steps.add("   -------------------------------------------------");

        steps.add("\n6. Gráfica de los puntos evaluados (eje X = x_i, eje Y = f(x_i)):");
        steps.add(generateASCIIPlot(xValues, yValues));

        return new IntegrationResult(totalIntegral, h, xValues, yValues, steps);
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
