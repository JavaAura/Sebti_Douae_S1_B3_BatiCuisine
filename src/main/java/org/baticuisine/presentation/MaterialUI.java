package org.baticuisine.presentation;

import org.baticuisine.entities.Material;
import org.baticuisine.entities.Project;
import org.baticuisine.serviceImpl.MaterialServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MaterialUI {

    private MaterialServiceImpl materialService;
    public MaterialUI(){
        this.materialService = new MaterialServiceImpl();
    }
    public List<Material> ajouterMateriaux(Project project) {
        Scanner scanner = new Scanner(System.in);
        List<Material> materiaux = new ArrayList<>();
        boolean ajouterPlus = true;

        System.out.println("--- Ajout des matériaux pour le projet : " + project.getProjectName() + " ---");

        while (ajouterPlus) {
            System.out.print("Entrez le nom du matériau : ");
            String nom = scanner.nextLine();
            System.out.print("Entrez la quantité de ce matériau (en m²) : ");
            double quantite = scanner.nextDouble();
            System.out.print("Entrez le coût unitaire de ce matériau (€/m²) : ");
            double coutUnitaire = scanner.nextDouble();
            System.out.print("Entrez le coût de transport de ce matériau (€) : ");
            double coutTransport = scanner.nextDouble();
            System.out.print("Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) : ");
            double coefficientQualite = scanner.nextDouble();
            scanner.nextLine();

            // Create a new material and set its properties
            Material materiau = new Material();
            materiau.setName(nom);
            materiau.setQuantity(quantite);
            materiau.setUnitCost(coutUnitaire);
            materiau.setTransportCost(coutTransport);
            materiau.setQualityCoefficient(coefficientQualite);

            // Associate the material with the project
            materiau.setProject(project);  // Assuming Material has a setProject method

            // Add the material to the list and persist it
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
