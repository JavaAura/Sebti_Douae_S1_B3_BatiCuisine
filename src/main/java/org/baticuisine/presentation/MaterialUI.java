package org.baticuisine.presentation;

import org.baticuisine.entities.Material;
import org.baticuisine.entities.Project;
import org.baticuisine.serviceImpl.MaterialServiceImpl;
import org.baticuisine.util.InputValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MaterialUI {

    private MaterialServiceImpl materialService;

    public MaterialUI() {
        this.materialService = new MaterialServiceImpl();
    }

    public List<Material> ajouterMateriaux(Project project) {
        Scanner scanner = new Scanner(System.in);
        List<Material> materiaux = new ArrayList<>();
        boolean ajouterPlus = true;

        if (project == null || project.getId() <= 0) {
            System.out.println("Le projet associé n'est valide. Veuillez créer un projet valide avant d'ajouter de la main-d'œuvre.");
            return materiaux;
        }

        System.out.println("--- Ajout des matériaux pour le projet : " + project.getProjectName() + " ---");

        while (ajouterPlus) {
            System.out.print("Entrez le nom du matériau : ");
            String nom = scanner.nextLine();
            double quantite = InputValidator.getValidDouble("Entrez la quantité de ce matériau (en m²) :");
            double coutUnitaire = InputValidator.getValidDouble("Entrez le coût unitaire de ce matériau (€/m²) : ");
            double coutTransport = InputValidator.getValidDouble("Entrez le coût de transport de ce matériau (€) : ");
            double coefficientQualite = Double.parseDouble(InputValidator.getValidCoeff("Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) :"));

            Material materiau = new Material();
            materiau.setName(nom);
            materiau.setQuantity(quantite);
            materiau.setUnitCost(coutUnitaire);
            materiau.setTransportCost(coutTransport);
            materiau.setQualityCoefficient(coefficientQualite);

            materiau.setProject(project);

            materiaux.add(materiau);
            materialService.addComponent(materiau);

            System.out.println("Matériau ajouté avec succès !");
            System.out.print("Voulez-vous ajouter un autre matériau ? (y/n) : ");
            String continuer = scanner.nextLine();

            ajouterPlus = continuer.equalsIgnoreCase("y");
        }

        return materiaux;
    }
}
