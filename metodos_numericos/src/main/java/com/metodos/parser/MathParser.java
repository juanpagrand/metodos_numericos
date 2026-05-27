package com.metodos.parser;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Utility class to parse and evaluate mathematical expressions using exp4j.
 * The only variable allowed is 'x'.
 */
public class MathParser {
    private final String expressionString;
    private Expression expression;

    public MathParser(String expressionString) throws IllegalArgumentException {
        if (expressionString == null || expressionString.trim().isEmpty()) {
            throw new IllegalArgumentException("La expresión de la función no puede estar vacía.");
        }
        this.expressionString = sanitizeExpression(expressionString);
        validateAndBuild();
    }

    /**
     * Sanitizes the expression to make it more user friendly.
     * E.g., handling implicit multiplication for 'pi' and 'e', or case insensitivity.
     */
    private String sanitizeExpression(String expr) {
        String sanitized = expr.toLowerCase().trim();
        // Replace common spanish names/aliases if any (like sen -> sin)
        sanitized = sanitized.replaceAll("\\bsen\\b", "sin");
        // Replace 'log' with 'log10' (as base-10 log is standard for user inputting 'log')
        sanitized = sanitized.replaceAll("\\blog\\b", "log10");
        // Replace 'ln' with 'log' as exp4j uses 'log' for natural logarithm
        sanitized = sanitized.replaceAll("\\bln\\b", "log");
        return sanitized;
    }

    private void validateAndBuild() throws IllegalArgumentException {
        try {
            // Build the expression, declaring 'x' as the independent variable.
            this.expression = new ExpressionBuilder(this.expressionString)
                    .variable("x")
                    .build();
            
            // Perform a quick validation by building the expression tree
            this.expression.validate();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error de sintaxis en la función: " + e.getMessage(), e);
        }
    }

    /**
     * Evaluates the function at a given value of x.
     * @param x the value of x
     * @return the value of f(x)
     * @throws ArithmeticException if division by zero or other mathematical error occurs
     */
    public double evaluate(double x) throws ArithmeticException {
        try {
            expression.setVariable("x", x);
            double result = expression.evaluate();
            if (Double.isNaN(result)) {
                throw new ArithmeticException("Resultado indefinido (NaN) en x = " + x);
            }
            if (Double.isInfinite(result)) {
                throw new ArithmeticException("Resultado infinito en x = " + x);
            }
            return result;
        } catch (Exception e) {
            throw new ArithmeticException("Error al evaluar en x = " + x + ": " + e.getMessage());
        }
    }

    public String getExpressionString() {
        return expressionString;
    }
}
