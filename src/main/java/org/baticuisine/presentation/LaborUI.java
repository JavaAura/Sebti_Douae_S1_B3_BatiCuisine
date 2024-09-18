package org.baticuisine.presentation;

import org.baticuisine.entities.Labor;
import org.baticuisine.entities.Project;
import org.baticuisine.serviceImpl.LaborServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LaborUI {

    private LaborServiceImpl laborService;
    public LaborUI(){
        this.laborService = new LaborServiceImpl();
    }
    public List<Labor> ajouterMainDOeuvre(Project project) {
        Scanner scanner = new Scanner(System.in);
        List<Labor> mainDOeuvre = new ArrayList<>();
        boolean ajouterPlus = true;
        if (project == null || project.getId() <= 0) {
            System.out.println("Le projet associé n'est valide. Veuillez créer un projet valide avant d'ajouter de la main-d'œuvre.");
            return mainDOeuvre;
        }
        System.out.println("--- Ajout de la main-d'œuvre pour le projet : " + project.getProjectName() + " ---");

        while (ajouterPlus) {
            System.out.print("Entrez le type de main-d'œuvre (e.g., Ouvrier de base, Spécialiste) : ");
            String type = scanner.nextLine();
            System.out.print("Entrez le taux horaire de cette main-d'œuvre (€/h) : ");
            double tauxHoraire = scanner.nextDouble();
            System.out.print("Entrez le nombre d'heures travaillées : ");
            double heuresTravaillees = scanner.nextDouble();
            System.out.print("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : ");
            double productivite = scanner.nextDouble();
            scanner.nextLine();

            Labor labor = new Labor();
            labor.setName(type);
            labor.setHourlyRate(tauxHoraire);
            labor.setWorkHours(heuresTravaillees);
            labor.setWorkerProductivity(productivite);
            labor.setProject(project);

            mainDOeuvre.add(labor);
            laborService.addComponent(labor);

            System.out.println("Main-d'œuvre ajoutée avec succès !");
            System.out.print("Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n) : ");
            String continuer = scanner.nextLine();

            ajouterPlus = continuer.equalsIgnoreCase("y");
        }

        return mainDOeuvre;
    }
}
