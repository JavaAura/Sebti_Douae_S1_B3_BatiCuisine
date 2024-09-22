package org.baticuisine.presentation;

import org.baticuisine.entities.Labor;
import org.baticuisine.entities.Project;
import org.baticuisine.serviceImpl.LaborServiceImpl;
import org.baticuisine.util.InputValidator;

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

            double tauxHoraire = InputValidator.getValidDouble("Entrez le taux horaire de cette main-d'œuvre (€/h) : ");

            double heuresTravaillees = InputValidator.getValidDouble("Entrez le nombre d'heures travaillées : ");

            double productivite = Double.parseDouble(InputValidator.getValidCoeff("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : "));


            Labor labor = new Labor();
            labor.setName(type);
            labor.setHourlyRate(tauxHoraire);
            labor.setWorkHours(heuresTravaillees);
            labor.setWorkerProductivity(productivite);
            labor.setProject(project);

            mainDOeuvre.add(labor);
            laborService.addComponent(labor);

            System.out.println("Main-d'œuvre ajoutée avec succès !");

            ajouterPlus = InputValidator.getValidYesNo("Voulez-vous ajouter un autre type de main-d'œuvre ?");

        }

        return mainDOeuvre;
    }
}
