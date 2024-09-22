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
            }else{
                System.out.println("Entrée invalide. Veuillez réessayer.");
            }
        }
        return input;
    }


}
