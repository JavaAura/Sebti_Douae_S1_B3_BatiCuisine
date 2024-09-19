package org.baticuisine.presentation;

import org.baticuisine.entities.*;
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

    public void calculerCoutTotalProjet(Project project) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Calcul du coût total ---");
        System.out.println("Souhaitez-vous appliquer une TVA au projet ? (y/n) : ");
        String applyTax = scanner.nextLine();
        double taxRate = 0;
        if (applyTax.equalsIgnoreCase("y")) {
            System.out.println("Entrez le pourcentage de TVA (%) : ");
            taxRate = scanner.nextDouble();
            scanner.nextLine();
        }

        System.out.println("Souhaitez-vous appliquer une marge bénéficiaire au projet ? (y/n) : ");
        String applyMargin = scanner.nextLine();
        double profitMargin = 0;
        if (applyMargin.equalsIgnoreCase("y")) {
            System.out.println("Entrez le pourcentage de marge bénéficiaire (%) : ");
            profitMargin = scanner.nextDouble();
            scanner.nextLine();
        }

        projectService.applyTaxAndProfitMargin(project, taxRate, profitMargin);
        System.out.println("TVA et marge bénéficiaire appliquées avec succès.");
        displayProjectCostDetails(project);
    }

    public void displayProjectCostDetails(Project project) {
        System.out.println("--- Résultat du Calcul ---");

        System.out.println("Nom du projet : " + project.getProjectName());
        Client client = project.getClient();
        System.out.println("Client : " + client.getName());
        System.out.println("Adresse du client : " + client.getAddress());

        System.out.println("--- Détail des Coûts ---");

        System.out.println("1. Matériaux :");
        double totalMaterialCostBeforeTax = 0;
        double totalMaterialCostAfterTax = 0;
        double materialTaxRate = 0;
        for (Component component : project.getComponents()) {
            if (component instanceof Material) {
                Material material = (Material) component;
                double costBeforeTax = material.getUnitCost() * material.getQuantity();
                double costAfterTax = costBeforeTax + (costBeforeTax * (material.getTaxRate() / 100));
                totalMaterialCostBeforeTax += costBeforeTax;
                totalMaterialCostAfterTax += costAfterTax;
                materialTaxRate = material.getTaxRate();
                System.out.printf("- %s : %.2f € (avant TVA : %.2f €, quantité : %.2f, coût unitaire : %.2f €/unité, transport : %.2f €)%n",
                        material.getName(), costAfterTax, costBeforeTax, material.getQuantity(), material.getUnitCost(), material.getTransportCost());
            }
        }
        System.out.printf("Coût total des matériaux avant TVA : %.2f €%n", totalMaterialCostBeforeTax);
        System.out.printf("Coût total des matériaux avec TVA (%.0f%%) : %.2f €%n", materialTaxRate, totalMaterialCostAfterTax);

        System.out.println("2. Main-d'œuvre :");
        double totalLaborCostBeforeTax = 0;
        double totalLaborCostAfterTax = 0;
        double laborTaxRate = 0;
        for (Component component : project.getComponents()) {
            if (component instanceof Labor) {
                Labor labor = (Labor) component;
                double costBeforeTax = labor.getHourlyRate() * labor.getWorkHours();
                double costAfterTax = costBeforeTax + (costBeforeTax * (labor.getTaxRate() / 100));
                totalLaborCostBeforeTax += costBeforeTax;
                totalLaborCostAfterTax += costAfterTax;
                laborTaxRate = labor.getTaxRate();
                System.out.printf("- %s : %.2f € (avant TVA : %.2f €, taux horaire : %.2f €/h, heures travaillées : %.2f h)%n",
                        labor.getName(), costAfterTax, costBeforeTax, labor.getHourlyRate(), labor.getWorkHours());
            }
        }
        System.out.printf("Coût total de la main-d'œuvre avant TVA : %.2f €%n", totalLaborCostBeforeTax);
        System.out.printf("Coût total de la main-d'œuvre avec TVA (%.0f%%) : %.2f €%n", laborTaxRate, totalLaborCostAfterTax);

        double totalCostBeforeMargin = totalMaterialCostAfterTax + totalLaborCostAfterTax;
        System.out.printf("Coût total avant marge : %.2f €%n", totalCostBeforeMargin);

        if (project.getProfitMargin() > 0) {
            double marginAmount = totalCostBeforeMargin * (project.getProfitMargin() / 100);
            double totalCostAfterMargin = totalCostBeforeMargin + marginAmount;
            System.out.printf("Marge bénéficiaire (%.2f%%) : %.2f €%n", project.getProfitMargin(), marginAmount);
            System.out.printf("Coût total final du projet : %.2f €%n", totalCostAfterMargin);
        } else {
            System.out.printf("Coût total final du projet : %.2f €%n", totalCostBeforeMargin);
        }
    }



}
