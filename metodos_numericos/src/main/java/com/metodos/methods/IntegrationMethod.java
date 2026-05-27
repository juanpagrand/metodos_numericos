package com.metodos.methods;

import com.metodos.parser.MathParser;

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
        int rows = 12;
        int cols = 60;
        char[][] grid = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = ' ';
            }
        }

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

        double diffX = maxX - minX;
        double diffY = maxY - minY;

        if (diffX == 0) diffX = 1.0;
        if (diffY == 0) diffY = 1.0;

        // Draw axes if they fall within bounds
        int zeroRow = -1;
        if (minY <= 0 && maxY >= 0) {
            zeroRow = (int) Math.round((maxY - 0) / diffY * (rows - 1));
            if (zeroRow >= 0 && zeroRow < rows) {
                for (int c = 0; c < cols; c++) {
                    grid[zeroRow][c] = '-';
                }
            }
        }

        int zeroCol = -1;
        if (minX <= 0 && maxX >= 0) {
            zeroCol = (int) Math.round((0 - minX) / diffX * (cols - 1));
            if (zeroCol >= 0 && zeroCol < cols) {
                for (int r = 0; r < rows; r++) {
                    grid[r][zeroCol] = '|';
                }
            }
        }

        if (zeroRow != -1 && zeroCol != -1) {
            grid[zeroRow][zeroCol] = '+';
        }

        // Draw vertical drop lines from each point down to the bottom of the grid
        for (int i = 0; i < x.length; i++) {
            int c = (int) Math.round((x[i] - minX) / diffX * (cols - 1));
            int r = (int) Math.round((maxY - y[i]) / diffY * (rows - 1));

            if (c < 0) c = 0;
            if (c >= cols) c = cols - 1;
            if (r < 0) r = 0;
            if (r >= rows) r = rows - 1;

            for (int row = r + 1; row < rows; row++) {
                grid[row][c] = '|';
            }
        }

        // Plot points and coordinates
        for (int i = 0; i < x.length; i++) {
            int c = (int) Math.round((x[i] - minX) / diffX * (cols - 1));
            int r = (int) Math.round((maxY - y[i]) / diffY * (rows - 1));

            if (c < 0) c = 0;
            if (c >= cols) c = cols - 1;
            if (r < 0) r = 0;
            if (r >= rows) r = rows - 1;

            // Generate label
            String label = String.format(java.util.Locale.US, "(%s, %.2f)", formatDoubleForPlot(x[i]), y[i]);

            // Check if label fits to the left of the *
            if (c >= label.length() + 1) {
                String labelWithStar = label + " *";
                int startCol = c - labelWithStar.length() + 1;
                for (int k = 0; k < labelWithStar.length(); k++) {
                    int targetCol = startCol + k;
                    if (targetCol >= 0 && targetCol < cols) {
                        grid[r][targetCol] = labelWithStar.charAt(k);
                    }
                }
            } else {
                String starWithLabel = "* " + label;
                for (int k = 0; k < starWithLabel.length(); k++) {
                    int targetCol = c + k;
                    if (targetCol >= 0 && targetCol < cols) {
                        grid[r][targetCol] = starWithLabel.charAt(k);
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(java.util.Locale.US, "   y-máx: %.4f\n", maxY));
        
        // Top border
        sb.append("     +");
        for (int c = 0; c < cols; c++) {
            sb.append("-");
        }
        sb.append("+\n");

        // Rows
        for (int r = 0; r < rows; r++) {
            sb.append("     | ");
            for (int c = 0; c < cols; c++) {
                sb.append(grid[r][c]);
            }
            sb.append(" |\n");
        }

        // Bottom border
        sb.append("     +");
        for (int c = 0; c < cols; c++) {
            sb.append("-");
        }
        sb.append("+\n");

        sb.append(String.format(java.util.Locale.US, "   y-mín: %.4f\n", minY));
        
        // Print x-axis limits aligned to columns
        sb.append("       ");
        String minXStr = String.format(java.util.Locale.US, "%.2f", minX);
        String maxXStr = String.format(java.util.Locale.US, "%.2f", maxX);
        sb.append(minXStr);
        int spaces = cols - minXStr.length() - maxXStr.length() - 2;
        if (spaces > 0) {
            for (int i = 0; i < spaces; i++) {
                sb.append(" ");
            }
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
