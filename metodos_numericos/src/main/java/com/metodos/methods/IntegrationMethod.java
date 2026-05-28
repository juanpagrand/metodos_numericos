package com.metodos.methods;

import com.metodos.parser.MathParser;
import java.util.function.DoubleUnaryOperator;

/**
 * Common interface for all numerical integration methods.
 */
public interface IntegrationMethod {
    /**
     * Gets the name of the integration method.
     */
    String getName();

    /**
     * Gets a short description of the method and its constraints (e.g. n must be even).
     */
    String getDescription();

    /**
     * Calculates the integral of a function defined by a MathParser between a and b using n subdivisions.
     * 
     * @param parser the mathematical function parser
     * @param a the lower limit of integration
     * @param b the upper limit of integration
     * @param n the number of subdivisions (or blocks, depending on the method)
     * @return the integration result containing calculated value and steps
     * @throws IllegalArgumentException if the parameters are invalid (e.g. n is odd for Simpson 1/3)
     * @throws ArithmeticException if an arithmetic error occurs (e.g. division by zero, NaN values)
     */
    IntegrationResult calculate(MathParser parser, double a, double b, int n) throws IllegalArgumentException, ArithmeticException;

    /**
     * Generates a text-based ASCII plot representing y values vs x values.
     */
    default String generateASCIIPlot(double[] x, double[] y) {
        return generateASCIIPlot(null, 0.0, 1.0, x, y, 60, 12);
    }

    /**
     * Generates a text-based ASCII plot with custom width/height.
     */
    default String generateASCIIPlot(DoubleUnaryOperator func, double a, double b, double[] x, double[] y) {
        return generateASCIIPlot(func, a, b, x, y, 60, 12);
    }

    /**
     * Generates a text-based ASCII plot with custom width/height and optional background curve.
     */
    default String generateASCIIPlot(DoubleUnaryOperator func, double a, double b, double[] x, double[] y, int cols, int rows) {
        if (x == null || y == null || x.length == 0 || y.length == 0 || x.length != y.length) {
            return "(sin datos para graficar)";
        }
        int innerCols = Math.max(cols, 10);
        int innerRows = Math.max(rows, 6);

        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        for (double val : x) {
            if (val < minX) minX = val;
            if (val > maxX) maxX = val;
        }
        for (double val : y) {
            if (val < minY) minY = val;
            if (val > maxY) maxY = val;
        }

        double[] curveSamples = null;
        if (func != null) {
            minX = a;
            maxX = b;
            int sampleCount = innerCols;
            curveSamples = new double[sampleCount];
            for (int i = 0; i < sampleCount; i++) {
                double t = (sampleCount == 1) ? 0.0 : (double) i / (sampleCount - 1);
                double xi = a + (b - a) * t;
                double yi;
                try {
                    yi = func.applyAsDouble(xi);
                } catch (Exception ex) {
                    yi = Double.NaN;
                }
                curveSamples[i] = yi;
            }
        }

        double diffX = maxX - minX;
        double diffY = maxY - minY;
        if (diffX == 0) diffX = 1.0;
        if (diffY == 0) diffY = 1.0;

        int totalCols = innerCols + 2;
        int totalRows = innerRows + 2;
        char[][] canvas = new char[totalRows][totalCols];
        for (int r = 0; r < totalRows; r++) {
            for (int c = 0; c < totalCols; c++) {
                canvas[r][c] = ' ';
            }
        }

        canvas[0][0] = '+';
        canvas[0][totalCols - 1] = '+';
        canvas[totalRows - 1][0] = '+';
        canvas[totalRows - 1][totalCols - 1] = '+';
        for (int c = 1; c < totalCols - 1; c++) {
            canvas[0][c] = '-';
            canvas[totalRows - 1][c] = '-';
        }
        for (int r = 1; r < totalRows - 1; r++) {
            canvas[r][0] = '|';
            canvas[r][totalCols - 1] = '|';
        }

        if (curveSamples != null) {
            for (int i = 0; i < curveSamples.length; i++) {
                double yi = curveSamples[i];
                if (Double.isNaN(yi) || Double.isInfinite(yi)) {
                    continue;
                }
                int col = i;
                int row = (int) Math.round(((maxY - yi) / diffY) * (innerRows - 1));

                if (row < 0 || row >= innerRows) {
                    continue;
                }
                if (col < 0 || col >= innerCols) {
                    continue;
                }

                int plotCol = col + 1;
                int plotRow = row + 1;
                if (canvas[plotRow][plotCol] == ' ') {
                    canvas[plotRow][plotCol] = '.';
                }
            }
        }

        for (int i = 0; i < x.length; i++) {
            if (Double.isNaN(x[i]) || Double.isInfinite(x[i]) || Double.isNaN(y[i]) || Double.isInfinite(y[i])) {
                continue;
            }
            int col = (int) Math.round(((x[i] - minX) / diffX) * (innerCols - 1));
            int row = (int) Math.round(((maxY - y[i]) / diffY) * (innerRows - 1));

            if (col < 0) col = 0;
            if (col >= innerCols) col = innerCols - 1;
            if (row < 0) row = 0;
            if (row >= innerRows) row = innerRows - 1;

            int plotCol = col + 1;
            int plotRow = row + 1;
            canvas[plotRow][plotCol] = '*';

            String label = String.format(java.util.Locale.US, "(%s, %.2f)", formatDoubleForPlot(x[i]), y[i]);
            int rightStart = plotCol + 1;
            int leftStart = plotCol - label.length() - 1;

            if (rightStart + label.length() <= totalCols - 1) {
                for (int k = 0; k < label.length(); k++) {
                    canvas[plotRow][rightStart + k] = label.charAt(k);
                }
            } else if (leftStart >= 1) {
                for (int k = 0; k < label.length(); k++) {
                    canvas[plotRow][leftStart + k] = label.charAt(k);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(java.util.Locale.US, "y-max: %.4f\n", maxY));
        for (int r = 0; r < totalRows; r++) {
            for (int c = 0; c < totalCols; c++) {
                sb.append(canvas[r][c]);
            }
            sb.append("\n");
        }
        sb.append(String.format(java.util.Locale.US, "y-min: %.4f\n", minY));

        String minXStr = String.format(java.util.Locale.US, "%.2f", minX);
        String maxXStr = String.format(java.util.Locale.US, "%.2f", maxX);
        sb.append(minXStr);
        int spaces = totalCols - minXStr.length() - maxXStr.length();
        for (int i = 0; i < Math.max(spaces, 1); i++) {
            sb.append(" ");
        }
        sb.append(maxXStr);
        sb.append("\n");

        return sb.toString();
    }

    private String formatDoubleForPlot(double val) {
        if (val == (long) val) {
            return String.format(java.util.Locale.US, "%d", (long) val);
        } else {
            String s = String.format(java.util.Locale.US, "%.2f", val);
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
