package org.baticuisine.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public static boolean getValidYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                System.out.println("Veuillez entrer 'y' pour oui ou 'n' pour non.");
            }
        }
    }

    public static LocalDate getValidDate(String prompt) {
        LocalDate date = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (date == null) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            try {
                date = LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Date invalide. Veuillez entrer la date au format jj/mm/aaaa.");
            } catch (Exception e) {
                System.out.println("Entrée invalide. Veuillez réessayer.");
            }
        }
        return date;
    }

    public static LocalDate getValidDateWithCheck(String prompt, LocalDate compareDate) {
        LocalDate date = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (date == null) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            try {
                date = LocalDate.parse(input, formatter);
                if (date.isBefore(compareDate)) {
                    System.out.println("La date de validité doit être supérieure à la date d'émission.");
                    date = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Date invalide. Veuillez entrer la date au format jj/mm/aaaa.");
            } catch (Exception e) {
                System.out.println("Entrée invalide. Veuillez réessayer.");
            }
        }
        return date;
    }


}
