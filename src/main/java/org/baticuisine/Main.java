package org.baticuisine;


import org.baticuisine.presentation.ClientUI;
import org.baticuisine.presentation.ConsoleUI;

public class Main {
    public static void main(String[] args)  {

        System.out.println("Hello world!");
      //  Connection conn =   DatabaseConnection.getInstance().getConnection();
        ConsoleUI consoleUI = new ConsoleUI();

        consoleUI.start();

    }
}