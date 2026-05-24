package com.metodos.methods;

import com.metodos.parser.MathParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Simpson38Method implements IntegrationMethod {
    @Override
    public String getName() {
        return "Método de Simpson 3/8";
    }

    @Override
    public String getDescription() {
        return "Aproxima la integral usando polinomios cúbicos. Requiere que n sea múltiplo de 3 (n >= 3).";
    }

    @Override
    public IntegrationResult calculate(MathParser parser, double a, double b, int n) throws IllegalArgumentException, ArithmeticException {
        if (n < 3) {
            throw new IllegalArgumentException("El número de subdivisiones (n) debe ser mayor o igual a 3 para el Método de Simpson 3/8.");
        }
        if (n % 3 != 0) {
            throw new IllegalArgumentException("El número de subdivisiones (n) debe ser un MÚLTIPLO DE 3 para el Método de Simpson 3/8.");
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

        // Apply Simpson's 3/8 Rule
        double sumMult3 = 0;
        double sumOthers = 0;

        for (int i = 1; i < n; i++) {
            if (i % 3 == 0) {
                sumMult3 += yValues[i];
            } else {
                sumOthers += yValues[i];
            }
        }

        double result = (3.0 * h / 8.0) * (yValues[0] + 3.0 * sumOthers + 2.0 * sumMult3 + yValues[n]);

        // Generate steps
        List<String> steps = new ArrayList<>();
        steps.add("--- MÉTODO DE SIMPSON 3/8 COMPUESTO ---");
        steps.add(String.format(Locale.US, "1. Calcular el tamaño de paso (h):\n   h = (b - a) / n = (%.6f - %.6f) / %d = %.8f", b, a, n, h));
        
        steps.add("\n2. Tabulación de puntos evaluados:");
        for (int i = 0; i <= n; i++) {
            String role;
            if (i == 0) role = "Límite inferior (a)";
            else if (i == n) role = "Límite superior (b)";
            else if (i % 3 == 0) role = "Múltiplo de 3 (peso 2)";
            else role = "Otros puntos (peso 3)";
            
            steps.add(String.format(Locale.US, "   x_%d = %.6f   ->   f(x_%d) = %.8f   [%s]", i, xValues[i], i, yValues[i], role));
        }

        steps.add("\n3. Aplicar la fórmula:");
        steps.add("   Integral ≈ (3h / 8) * [ f(x_0) + 3 * Σ(f(x_no_multiplos_de_3)) + 2 * Σ(f(x_multiplos_de_3)) + f(x_n) ]");
        steps.add(String.format(Locale.US, "   Suma de puntos intermedios (peso 3) = %.8f", sumOthers));
        steps.add(String.format(Locale.US, "   Suma de múltiplos de 3 (peso 2) = %.8f", sumMult3));
        steps.add(String.format(Locale.US, "   Integral ≈ (3 * %.8f / 8) * [ %.8f + 3 * (%.8f) + 2 * (%.8f) + %.8f ]", 
                h, yValues[0], sumOthers, sumMult3, yValues[n]));
        
        steps.add(String.format(Locale.US, "\n4. Resultado final:\n   Integral ≈ %.10f", result));

        return new IntegrationResult(result, h, xValues, yValues, steps);
    }
}
