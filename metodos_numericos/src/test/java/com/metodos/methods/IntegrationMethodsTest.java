package com.metodos.methods;

import com.metodos.parser.MathParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationMethodsTest {

    private MathParser sinX;
    private MathParser xSquared;
    private MathParser oneOverX;

    @BeforeEach
    public void setUp() {
        sinX = new MathParser("sin(x)");
        xSquared = new MathParser("x^2");
        oneOverX = new MathParser("1/x");
    }

    @Test
    public void testTrapezoidalMethod() {
        IntegrationMethod method = new TrapezoidalMethod();
        
        // Int(sin(x), 0, pi) -> approx 2.0
        IntegrationResult res1 = method.calculate(sinX, 0, Math.PI, 100);
        assertEquals(2.0, res1.getResult(), 0.001);

        // Int(x^2, 0, 1) -> approx 1/3 = 0.33333
        IntegrationResult res2 = method.calculate(xSquared, 0, 1, 100);
        assertEquals(1.0 / 3.0, res2.getResult(), 0.001);

        // Int(1/x, 1, 2) -> approx ln(2) = 0.69314
        IntegrationResult res3 = method.calculate(oneOverX, 1, 2, 100);
        assertEquals(Math.log(2), res3.getResult(), 0.001);

        // Check invalid divisions
        assertThrows(IllegalArgumentException.class, () -> method.calculate(sinX, 0, 1, 0));
    }

    @Test
    public void testSimpson13Method() {
        IntegrationMethod method = new Simpson13Method();

        // Int(sin(x), 0, pi) -> 2.0
        IntegrationResult res1 = method.calculate(sinX, 0, Math.PI, 20);
        assertEquals(2.0, res1.getResult(), 0.0001);

        // Int(x^2, 0, 1) -> should be exact for Simpson 1/3 (since it's a 2nd degree polynomial)
        IntegrationResult res2 = method.calculate(xSquared, 0, 1, 2);
        assertEquals(1.0 / 3.0, res2.getResult(), 1e-9);

        // Check invalid divisions (odd n)
        assertThrows(IllegalArgumentException.class, () -> method.calculate(sinX, 0, 1, 3));
    }

    @Test
    public void testSimpson38Method() {
        IntegrationMethod method = new Simpson38Method();

        // Int(sin(x), 0, pi) -> 2.0
        IntegrationResult res1 = method.calculate(sinX, 0, Math.PI, 30);
        assertEquals(2.0, res1.getResult(), 0.0001);

        // Int(x^2, 0, 1) -> exact for degree <= 3
        IntegrationResult res2 = method.calculate(xSquared, 0, 1, 3);
        assertEquals(1.0 / 3.0, res2.getResult(), 1e-9);

        // Check invalid divisions (not multiple of 3)
        assertThrows(IllegalArgumentException.class, () -> method.calculate(sinX, 0, 1, 4));
    }

    @Test
    public void testBooleMethod() {
        IntegrationMethod method = new BooleMethod();

        // Int(sin(x), 0, pi) -> 2.0
        IntegrationResult res1 = method.calculate(sinX, 0, Math.PI, 40);
        assertEquals(2.0, res1.getResult(), 0.0001);

        // Int(x^2, 0, 1) -> exact
        IntegrationResult res2 = method.calculate(xSquared, 0, 1, 4);
        assertEquals(1.0 / 3.0, res2.getResult(), 1e-9);

        // Check invalid divisions (not multiple of 4)
        assertThrows(IllegalArgumentException.class, () -> method.calculate(sinX, 0, 1, 6));
    }

    @Test
    public void testSimpsonAbiertoMethod() {
        IntegrationMethod method = new SimpsonAbiertoMethod();

        // Int(sin(x), 0, pi) -> 2.0
        IntegrationResult res1 = method.calculate(sinX, 0, Math.PI, 10);
        assertEquals(2.0, res1.getResult(), 0.001);

        // Int(x^2, 0, 1) -> exact or close
        IntegrationResult res2 = method.calculate(xSquared, 0, 1, 10);
        assertEquals(1.0 / 3.0, res2.getResult(), 0.001);

        // Check invalid divisions (n < 1)
        assertThrows(IllegalArgumentException.class, () -> method.calculate(sinX, 0, 1, 0));
    }

    @Test
    public void testMathParserWithLn() {
        MathParser parser = new MathParser("ln(x)");
        assertEquals(Math.log(2.0), parser.evaluate(2.0), 1e-9);
        assertEquals(Math.log(10.0), parser.evaluate(10.0), 1e-9);
    }

    @Test
    public void testMathParserWithLog() {
        MathParser parser = new MathParser("log(x)");
        assertEquals(Math.log10(2.0), parser.evaluate(2.0), 1e-9);
        assertEquals(Math.log10(10.0), parser.evaluate(10.0), 1e-9);
    }
}
