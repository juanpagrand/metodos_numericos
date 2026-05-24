package com.metodos.methods;

import com.metodos.parser.MathParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Simpson13Method implements IntegrationMethod {
    @Override
    public String getName() {
        return "Método de Simpson 1/3";
    }

    @Override
    public String getDescription() {
        return "Aproxima la integral usando parábolas. Requiere un número par de subdivisiones (n >= 2, par).";
    }

    @Override
    public IntegrationResult calculate(MathParser parser, double a, double b, int n) throws IllegalArgumentException, ArithmeticException {
        if (n < 2) {
            throw new IllegalArgumentException("El número de subdivisiones (n) debe ser mayor o igual a 2 para el Método de Simpson 1/3.");
        }
        if (n % 2 != 0) {
            throw new IllegalArgumentException("El número de subdivisiones (n) debe ser un número PAR para el Método de Simpson 1/3.");
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

        // Apply Simpson's 1/3 Rule
        double sumOdd = 0;
        double sumEven = 0;

        for (int i = 1; i < n; i++) {
            if (i % 2 == 0) {
                sumEven += yValues[i];
            } else {
                sumOdd += yValues[i];
            }
        }

        double result = (h / 3.0) * (yValues[0] + 4.0 * sumOdd + 2.0 * sumEven + yValues[n]);

        // Generate steps
        List<String> steps = new ArrayList<>();
        steps.add("--- MÉTODO DE SIMPSON 1/3 COMPUESTO ---");
        steps.add(String.format(Locale.US, "1. Calcular el tamaño de paso (h):\n   h = (b - a) / n = (%.6f - %.6f) / %d = %.8f", b, a, n, h));
        
        steps.add("\n2. Tabulación de puntos evaluados:");
        for (int i = 0; i <= n; i++) {
            String role;
            if (i == 0) role = "Límite inferior (a)";
            else if (i == n) role = "Límite superior (b)";
            else if (i % 2 == 0) role = "Punto par (peso 2)";
            else role = "Punto impar (peso 4)";
            
            steps.add(String.format(Locale.US, "   x_%d = %.6f   ->   f(x_%d) = %.8f   [%s]", i, xValues[i], i, yValues[i], role));
        }

        steps.add("\n3. Aplicar la fórmula:");
        steps.add("   Integral ≈ (h / 3) * [ f(x_0) + 4 * Σ(f(x_impares)) + 2 * Σ(f(x_pares)) + f(x_n) ]");
        steps.add(String.format(Locale.US, "   Suma de puntos impares (peso 4) = %.8f", sumOdd));
        steps.add(String.format(Locale.US, "   Suma de puntos pares (peso 2) = %.8f", sumEven));
        steps.add(String.format(Locale.US, "   Integral ≈ (%.8f / 3) * [ %.8f + 4 * (%.8f) + 2 * (%.8f) + %.8f ]", 
                h, yValues[0], sumOdd, sumEven, yValues[n]));
        
        steps.add(String.format(Locale.US, "\n4. Resultado final:\n   Integral ≈ %.10f", result));

        return new IntegrationResult(result, h, xValues, yValues, steps);
    }
}
