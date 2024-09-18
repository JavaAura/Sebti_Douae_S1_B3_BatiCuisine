package org.baticuisine.presentation;

import org.baticuisine.entities.Client;
import org.baticuisine.entities.Project;

import java.util.Scanner;

public class ProjectUI {

    public Project creerNouveauProjet(Client client) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Création d'un Nouveau Projet ---");
        System.out.print("Entrez le nom du projet : ");
        String nomProjet = scanner.nextLine();

        Project projet = new Project();
        projet.setProjectName(nomProjet);

        return projet;
    }
}
