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

        // Apply Boole's Composite Rule
        double sumOdd = 0;      // weight 32 (indices: 1, 3, 5, 7...)
        double sumEvenNot4 = 0; // weight 12 (indices: 2, 6, 10...)
        double sumMult4 = 0;    // weight 14 (indices: 4, 8, 12...)

        for (int i = 1; i < n; i++) {
            if (i % 4 == 0) {
                sumMult4 += yValues[i];
            } else if (i % 2 == 0) {
                sumEvenNot4 += yValues[i];
            } else {
                sumOdd += yValues[i];
            }
        }

        double result = (2.0 * h / 45.0) * (7.0 * yValues[0] + 32.0 * sumOdd + 12.0 * sumEvenNot4 + 14.0 * sumMult4 + 7.0 * yValues[n]);

        // Generate steps
        List<String> steps = new ArrayList<>();
        steps.add("--- MÉTODO DE BOOLE COMPUESTO ---");
        steps.add(String.format(Locale.US, "1. Calcular el tamaño de paso (h):\n   h = (b - a) / n = (%.6f - %.6f) / %d = %.8f", b, a, n, h));
        
        steps.add("\n2. Tabulación de puntos evaluados:");
        for (int i = 0; i <= n; i++) {
            String role;
            if (i == 0) role = "Límite inferior (a) [peso 7]";
            else if (i == n) role = "Límite superior (b) [peso 7]";
            else if (i % 4 == 0) role = "Múltiplo de 4 [peso 14]";
            else if (i % 2 == 0) role = "Par no múltiplo de 4 [peso 12]";
            else role = "Punto impar [peso 32]";
            
            steps.add(String.format(Locale.US, "   x_%d = %.6f   ->   f(x_%d) = %.8f   [%s]", i, xValues[i], i, yValues[i], role));
        }

        steps.add("\n3. Aplicar la fórmula:");
        steps.add("   Integral ≈ (2h / 45) * [ 7*f(x_0) + 32*Σ(f(x_impares)) + 12*Σ(f(x_pares_no_mult_4)) + 14*Σ(f(x_mult_4)) + 7*f(x_n) ]");
        steps.add(String.format(Locale.US, "   Suma de puntos impares (peso 32) = %.8f", sumOdd));
        steps.add(String.format(Locale.US, "   Suma de pares no mult 4 (peso 12) = %.8f", sumEvenNot4));
        steps.add(String.format(Locale.US, "   Suma de múltiplos de 4 (peso 14) = %.8f", sumMult4));
        steps.add(String.format(Locale.US, "   Integral ≈ (2 * %.8f / 45) * [ 7*(%.8f) + 32*(%.8f) + 12*(%.8f) + 14*(%.8f) + 7*(%.8f) ]", 
                h, yValues[0], sumOdd, sumEvenNot4, sumMult4, yValues[n]));
        
        steps.add(String.format(Locale.US, "\n4. Resultado final:\n   Integral ≈ %.10f", result));

        return new IntegrationResult(result, h, xValues, yValues, steps);
    }
}
