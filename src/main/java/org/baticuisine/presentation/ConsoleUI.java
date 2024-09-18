package org.baticuisine.presentation;

import org.baticuisine.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private ClientUI clientUI = new ClientUI();
    private ProjectUI projectUI = new ProjectUI();
    private MaterialUI materialUI = new MaterialUI();
    private LaborUI laborUI = new LaborUI();

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Bienvenue dans l'application de gestion des projets de rénovation de cuisines ===");

        while (running) {
            System.out.println("=== Menu Principal ===");
            System.out.println("1. Créer un nouveau projet");
            System.out.println("2. Afficher les projets existants");
            System.out.println("3. Calculer le coût d'un projet");
            System.out.println("4. Quitter");
            System.out.print("Choisissez une option : ");
            int choix = scanner.nextInt();
            scanner.nextLine();
            switch (choix) {
                case 1:
                    creerNouveauProjet();
                    break;
                case 2:
                    afficherProjetsExistants();
                    break;
                case 3:
                    calculerCoutProjet();
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }

    private void creerNouveauProjet() {

        Client client = clientUI.rechercherOuAjouterClient();

        if (client != null) {
            Project projet = projectUI.creerNouveauProjet(client);
            System.out.println("Project: " + projet); // Debugging statement to check if project is null

            List<Material> materials = materialUI.ajouterMateriaux();
            List<Labor> labor = laborUI.ajouterMainDOeuvre();

            List<Component> allComponents = new ArrayList<>();
            allComponents.addAll(materials);
            allComponents.addAll(labor);

            projet.setComponents(allComponents);


            System.out.println("Projet créé avec succès !");
        } else {
            System.out.println("Échec de la création du projet.");
        }
    }


    private void afficherProjetsExistants() {
    }

    private void calculerCoutProjet() {
    }
}
