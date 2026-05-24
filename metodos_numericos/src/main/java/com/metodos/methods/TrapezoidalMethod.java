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

        // Apply Trapezoidal Rule formula
        double sumInner = 0;
        for (int i = 1; i < n; i++) {
            sumInner += yValues[i];
        }

        double result = (h / 2.0) * (yValues[0] + 2.0 * sumInner + yValues[n]);

        // Generate Step-by-Step logs
        List<String> steps = new ArrayList<>();
        steps.add("--- MÉTODO TRAPEZOIDAL COMPUESTO ---");
        steps.add(String.format(Locale.US, "1. Calcular el tamaño de paso (h):\n   h = (b - a) / n = (%.6f - %.6f) / %d = %.8f", b, a, n, h));
        
        steps.add("\n2. Tabulación de puntos evaluados:");
        for (int i = 0; i <= n; i++) {
            String role = (i == 0) ? "Límite inferior (a)" : (i == n) ? "Límite superior (b)" : "Punto interno";
            steps.add(String.format(Locale.US, "   x_%d = %.6f   ->   f(x_%d) = %.8f   [%s]", i, xValues[i], i, yValues[i], role));
        }

        steps.add("\n3. Aplicar la fórmula:");
        steps.add("   Integral ≈ (h / 2) * [ f(x_0) + 2 * Σ(f(x_i) desde i=1 hasta n-1) + f(x_n) ]");
        
        if (n == 1) {
            steps.add(String.format(Locale.US, "   Integral ≈ (%.8f / 2) * [ %.8f + %.8f ]", h, yValues[0], yValues[1]));
        } else {
            steps.add(String.format(Locale.US, "   Suma de puntos internos = %.8f", sumInner));
            steps.add(String.format(Locale.US, "   Integral ≈ (%.8f / 2) * [ %.8f + 2 * (%.8f) + %.8f ]", h, yValues[0], sumInner, yValues[n]));
        }
        
        steps.add(String.format(Locale.US, "\n4. Resultado final:\n   Integral ≈ %.10f", result));

        return new IntegrationResult(result, h, xValues, yValues, steps);
    }
}
