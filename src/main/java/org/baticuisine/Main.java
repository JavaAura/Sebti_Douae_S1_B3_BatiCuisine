package org.baticuisine;


import org.baticuisine.presentation.ClientUI;
import org.baticuisine.presentation.MaterialUI;

public class Main {
    public static void main(String[] args)  {

        System.out.println("Hello world!");
      //  Connection conn =   DatabaseConnection.getInstance().getConnection();
        MaterialUI materialUI = new MaterialUI();

        materialUI.ajouterMateriaux();

    }
}