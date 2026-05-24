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
}
