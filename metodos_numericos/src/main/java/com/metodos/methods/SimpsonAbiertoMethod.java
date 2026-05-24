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

            steps.add(String.format(Locale.US, "   Bloque %d [%.4f a %.4f]:", j + 1, blockA, blockB));
            steps.add(String.format(Locale.US, "     x_1 = %.6f  ->  f(x_1) = %.8f (peso +2)", x1, y1));
            steps.add(String.format(Locale.US, "     x_2 = %.6f  ->  f(x_2) = %.8f (peso -1)", x2, y2));
            steps.add(String.format(Locale.US, "     x_3 = %.6f  ->  f(x_3) = %.8f (peso +2)", x3, y3));
            steps.add(String.format(Locale.US, "     Integral del bloque = (4*h/3) * [ 2*f(x1) - f(x2) + 2*f(x3) ] = %.8f", blockIntegral));
        }

        steps.add(String.format(Locale.US, "\n3. Suma total de integrales de los bloques:\n   Integral ≈ %.10f", totalIntegral));

        return new IntegrationResult(totalIntegral, h, xValues, yValues, steps);
    }
}
