package org.baticuisine.util;

import java.util.Scanner;

public class InputValidator {

    private static Scanner scanner = new Scanner(System.in);

    public static int getValidInt(String prompt) {
        int input;
        while (true) {
            System.out.println(prompt);
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                scanner.nextLine();
                break;
            } else {
                System.out.println("Entrér invalide. Veuillez entrer un nombre.");
                scanner.next();
            }
        }
        return input;
    }

    @FunctionalInterface
    public interface InputValidatorFunction<T> {
        boolean isValid(T input);
    }

    public static String getValidInput(String prompt, InputValidatorFunction<String> validator){
        String input;

        while(true){
            System.out.println(prompt);
            input = scanner.nextLine();
            if(validator.isValid(input)){
                break;
            }
        }
        return input;
    }

    public static double getValidDouble(String prompt){
        double input;
        while(true){
            System.out.println(prompt);
            if(scanner.hasNextDouble()){
            input = scanner.nextDouble();
            scanner.nextLine();
            break;
            }else{
                System.out.println("Entrér invalide. Veuillez entrer un nombre.");
                scanner.next();
            }
        }
        return input;
    }

    public static boolean isValidCoeff(String input) {
        try {
            double coeff = Double.parseDouble(input);
            if (coeff >= 1.0 && coeff <= 5.0) {
                return true;
            } else {
                System.out.println("Veuillez entrer une valeur comprise entre 1.0 et 5.0");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrée invalide. Veuillez entrer un nombre valide.");
            return false;
        }
    }

    public static String getValidCoeff(String prompt){
        return getValidInput(prompt,InputValidator::isValidCoeff);
    }


}
