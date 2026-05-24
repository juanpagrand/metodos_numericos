package com.metodos.methods;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the result of a numerical integration calculation,
 * along with step-by-step documentation, evaluated points, and metadata.
 */
public class IntegrationResult {
    private final double result;
    private final double h;
    private final double[] xValues;
    private final double[] yValues;
    private final List<String> steps;

    public IntegrationResult(double result, double h, double[] xValues, double[] yValues, List<String> steps) {
        this.result = result;
        this.h = h;
        this.xValues = xValues;
        this.yValues = yValues;
        this.steps = steps != null ? steps : new ArrayList<>();
    }

    public double getResult() {
        return result;
    }

    public double getH() {
        return h;
    }

    public double[] getXValues() {
        return xValues;
    }

    public double[] getYValues() {
        return yValues;
    }

    public List<String> getSteps() {
        return steps;
    }
}
