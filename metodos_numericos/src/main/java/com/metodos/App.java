package com.metodos;

import com.formdev.flatlaf.FlatDarkLaf;
import com.metodos.gui.MainFrame;
import com.metodos.methods.BooleMethod;
import com.metodos.methods.IntegrationResult;
import com.metodos.parser.MathParser;
import java.awt.Color;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {
    private static final String DEFAULT_FUNCTION = "x^2";

    public static void main(String[] args) {
        if (args.length > 0 && "cli".equalsIgnoreCase(args[0])) {
            runConsoleBoole();
            return;
        }
        launchGui();
    }

    private static void launchGui() {
        SwingUtilities.invokeLater(() -> {
            try {
                FlatDarkLaf.setup();

                UIManager.put("Button.arc", 12);
                UIManager.put("Component.arc", 12);
                UIManager.put("TextComponent.arc", 12);
                UIManager.put("ScrollBar.showButtons", true);
                UIManager.put("ScrollBar.thumbArc", 999);

                UIManager.put("Panel.background", new Color(11, 37, 46));
                UIManager.put("OptionPane.background", new Color(11, 37, 46));
                UIManager.put("Button.background", new Color(19, 56, 68));
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("Label.foreground", Color.WHITE);

                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void runConsoleBoole() {
        Locale.setDefault(Locale.US);
        try (Scanner scanner = new Scanner(System.in).useLocale(Locale.US)) {
            while (true) {
                System.out.println("\n=== Metodo de Boole (Regla de Boole) ===");
                System.out.println("1) Calcular integral con f(x) = " + DEFAULT_FUNCTION);
                System.out.println("2) Salir");

                int option = readInt(scanner, "Seleccione una opcion: ");
                if (option == 2) {
                    System.out.println("Saliendo...");
                    break;
                }
                if (option != 1) {
                    System.out.println("Opcion invalida. Intente de nuevo.");
                    continue;
                }

                double a = readDouble(scanner, "Ingrese el limite inferior a: ");
                double b = readDouble(scanner, "Ingrese el limite superior b: ");
                int n = readInt(scanner, "Ingrese el numero de subdivisiones n (multiplo de 4): ");

                int adjustedN = adjustToMultipleOfFour(n);
                if (adjustedN != n) {
                    System.out.println("Aviso: n debe ser multiplo de 4. Se ajusta a n = " + adjustedN + ".");
                    n = adjustedN;
                }

                MathParser parser = new MathParser(DEFAULT_FUNCTION);
                BooleMethod method = new BooleMethod();
                IntegrationResult result = method.calculate(parser, a, b, n);

                double exact = exactIntegralOfXSquared(a, b);
                double approx = result.getResult();
                double error = Math.abs(exact - approx);

                System.out.println("\n--- Resultado ---");
                System.out.printf(Locale.US, "Aproximacion (Boole): %.10f%n", approx);
                System.out.printf(Locale.US, "Valor exacto:          %.10f%n", exact);
                System.out.printf(Locale.US, "Error absoluto:        %.10f%n", error);
            }
        } catch (IllegalArgumentException | ArithmeticException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    private static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException ex) {
                System.out.println("Entrada invalida. Debe ser un numero real.");
            }
        }
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value <= 0) {
                    System.out.println("Entrada invalida. Debe ser un entero positivo.");
                    continue;
                }
                return value;
            } catch (NumberFormatException ex) {
                System.out.println("Entrada invalida. Debe ser un entero positivo.");
            }
        }
    }

    private static int adjustToMultipleOfFour(int n) {
        if (n < 4) {
            return 4;
        }
        int remainder = n % 4;
        if (remainder == 0) {
            return n;
        }
        return n + (4 - remainder);
    }

    private static double exactIntegralOfXSquared(double a, double b) {
        return (Math.pow(b, 3) - Math.pow(a, 3)) / 3.0;
    }
}
