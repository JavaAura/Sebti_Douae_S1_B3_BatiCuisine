package org.baticuisine.presentation;

import org.baticuisine.entities.Client;
import org.baticuisine.entities.Project;
import org.baticuisine.serviceImpl.ClientServiceImpl;
import org.baticuisine.serviceImpl.ProjectServiceImpl;

import java.util.Scanner;

public class ProjectUI {

    private ProjectServiceImpl projectService;

    public ProjectUI() {
        this.projectService = new ProjectServiceImpl();
    }

    public Project creerNouveauProjet(Client client) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Création d'un Nouveau Projet ---");
        System.out.print("Entrez le nom du projet : ");
        String nomProjet = scanner.nextLine();

        Project projet = new Project();
        projet.setProjectName(nomProjet);
        projet.setClient(client);

        projectService.addProject(projet);
        return projet;
    }
}
