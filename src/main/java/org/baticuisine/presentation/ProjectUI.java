package org.baticuisine.presentation;

import org.baticuisine.entities.*;
import org.baticuisine.serviceImpl.ProjectServiceImpl;
import org.baticuisine.serviceImpl.QuoteServiceImpl;
import org.baticuisine.util.InputValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProjectUI {

    private final ProjectServiceImpl projectService;
    private final QuoteServiceImpl quoteService;
    private final Scanner scanner;

    public ProjectUI() {
        this.projectService = new ProjectServiceImpl();
        this.quoteService = new QuoteServiceImpl();
        this.scanner = new Scanner(System.in);
    }

    public Project creerNouveauProjet(Client client) {
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
        System.out.println("--- Calcul du coût total ---");

        boolean applyTax = InputValidator.getValidYesNo("Souhaitez-vous appliquer une TVA au projet ?");
        double taxRate = applyTax ? InputValidator.getValidDouble("Entrez le pourcentage de TVA (%) : ") : 0;

        boolean applyMargin = InputValidator.getValidYesNo("Souhaitez-vous appliquer une marge bénéficiaire au projet ?");
        double profitMargin = applyMargin ? InputValidator.getValidDouble("Entrez le pourcentage de marge bénéficiaire (%) : ") : 0;

        projectService.applyTaxAndProfitMargin(project, taxRate, profitMargin);
        System.out.println("TVA et marge bénéficiaire appliquées avec succès.");
        displayProjectCostDetails(project);
        saveQuote(project);
    }

    public void displayProjectCostDetails(Project project) {
        System.out.println("Calcul du coût en cours...\n");
        System.out.println("--- Résultat du Calcul ---");

        System.out.println("Nom du projet : " + project.getProjectName());
        Client client = project.getClient();
        System.out.println("Client : " + Optional.ofNullable(client).map(Client::getName).orElse("Inconnu"));
        System.out.println("Adresse du client : " + Optional.ofNullable(client).map(Client::getAddress).orElse("Inconnu"));

        double totalMaterialCostAfterTax = project.getComponents().stream()
                .filter(component -> component instanceof Material)
                .mapToDouble(component -> {
                    Material material = (Material) component;
                    double costBeforeTax = material.getUnitCost() * material.getQuantity() * material.getQualityCoefficient() + material.getTransportCost();
                    double costAfterTax = costBeforeTax + (costBeforeTax * (material.getTaxRate() / 100));
                    System.out.printf("- %s : %.2f € (avant TVA : %.2f €, quantité : %.2f, coût unitaire : %.2f €/unité, qualité : %.1f, transport : %.2f €)%n",
                            material.getName(), costAfterTax, costBeforeTax, material.getQuantity(), material.getUnitCost(), material.getQualityCoefficient(), material.getTransportCost());
                    return costAfterTax;
                }).sum();

        System.out.printf("Coût total des matériaux avec TVA : %.2f €%n", totalMaterialCostAfterTax);

        double totalLaborCostAfterTax = project.getComponents().stream()
                .filter(component -> component instanceof Labor)
                .mapToDouble(component -> {
                    Labor labor = (Labor) component;
                    double costBeforeTax = labor.getHourlyRate() * labor.getWorkHours() * labor.getWorkerProductivity();
                    double costAfterTax = costBeforeTax + (costBeforeTax * (labor.getTaxRate() / 100));
                    System.out.printf("- %s : %.2f € (avant TVA : %.2f €, taux horaire : %.2f €/h, heures travaillées : %.2f h, productivité : %.1f)%n",
                            labor.getName(), costAfterTax, costBeforeTax, labor.getHourlyRate(), labor.getWorkHours(), labor.getWorkerProductivity());
                    return costAfterTax;
                }).sum();

        System.out.printf("Coût total de la main-d'œuvre avec TVA : %.2f €%n", totalLaborCostAfterTax);

        double totalCostBeforeMargin = totalMaterialCostAfterTax + totalLaborCostAfterTax;
        System.out.printf("Coût total avant marge : %.2f €%n", totalCostBeforeMargin);

        double totalCostAfterMargin = totalCostBeforeMargin;
        double marginAmount = 0;
        if (project.getProfitMargin() > 0) {
            marginAmount = totalCostBeforeMargin * (project.getProfitMargin() / 100);
            totalCostAfterMargin += marginAmount;
            System.out.printf("Marge bénéficiaire (%.2f%%) : %.2f €%n", project.getProfitMargin(), marginAmount);
        }

        if (client != null && client.getProfessional()) {
            double discountPercentage = client.getDiscount();
            double discountAmount = totalCostAfterMargin * (discountPercentage / 100);
            System.out.printf("Coût total après marge : %.2f €%n", totalCostAfterMargin);
            System.out.printf("Remise appliquée pour le client professionnel (%.2f%%) : %.2f €%n", discountPercentage, discountAmount);
            double finalCostWithDiscount = totalCostAfterMargin - discountAmount;
            System.out.printf("Coût total final du projet après remise : %.2f €%n", finalCostWithDiscount);
        } else {
            System.out.printf("Coût total final du projet : %.2f €%n", totalCostAfterMargin);
        }
    }

    private void saveQuote(Project project) {
        System.out.println("--- Enregistrement du Devis ---");
        LocalDate issueDate = InputValidator.getValidDate("Entrez la date d'émission du devis (format : jj/mm/aaaa) : ");
        LocalDate validityDate = InputValidator.getValidDateWithCheck("Entrez la date de validité du devis (format : jj/mm/aaaa) : ", issueDate);

        double estimatedAmount = project.getTotalCost();
        boolean confirmSave = InputValidator.getValidYesNo("Souhaitez-vous enregistrer le devis ?");

        if (confirmSave) {
            Quote quote = new Quote(estimatedAmount, issueDate, validityDate);
            quote.setProject(project);
            quoteService.addQuote(quote);
            System.out.println("Devis enregistré avec succès !");
        } else {
            System.out.println("Enregistrement du devis annulé.");
        }
    }

    public void displayAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        if (projects.isEmpty()) {
            System.out.println("Aucun projet trouvé.");
        } else {
            projects.forEach(this::displayProjectDetails);
        }
    }

    private void displayProjectDetails(Project project) {
        System.out.println("--- Détails du Projet ---");
        System.out.println("Nom du projet : " + project.getProjectName());
        System.out.println("Statut du projet : " + project.getStatus());
        System.out.println("Marge de profit : " + project.getProfitMargin());

        Client client = project.getClient();
        System.out.println("Client : " + Optional.ofNullable(client).map(Client::getName).orElse("Inconnu"));
        System.out.println("Adresse du client : " + Optional.ofNullable(client).map(Client::getAddress).orElse("Inconnu"));
        System.out.println("Numéro de téléphone : " + Optional.ofNullable(client).map(Client::getPhoneNumber).orElse("Inconnu"));
        System.out.println("Professionnel : " + (Optional.ofNullable(client).map(Client::getProfessional).orElse(false) ? "Oui" : "Non"));

        List<Component> components = project.getComponents();
        if (components != null && !components.isEmpty()) {
            System.out.println(" -- Composants du projet : -- ");
            components.forEach(component -> {
                if (component instanceof Material) {
                    Material material = (Material) component;
                    System.out.println("   Type : Material ");
                    System.out.println("   Nom : " + material.getName());
                    System.out.println("   Coût Unitaire : " + material.getUnitCost());
                    System.out.println("   Quantité : " + material.getQuantity());
                    System.out.println("   Coût de transport : " + material.getTransportCost());
                    System.out.println("   Coefficient de qualité : " + material.getQualityCoefficient());
                } else if (component instanceof Labor) {
                    Labor labor = (Labor) component;
                    System.out.println("   Type : Labor ");
                    System.out.println("   Nom : " + labor.getName());
                    System.out.println("   Taux horaire : " + labor.getHourlyRate());
                    System.out.println("   Heures travaillées : " + labor.getWorkHours());
                    System.out.println("   Productivité du travailleur : " + labor.getWorkerProductivity());
                }
            });
        } else {
            System.out.println("Aucun composant pour ce projet.");
        }

        System.out.println("Coût total estimé : " + project.getTotalCost() + " €");
        System.out.println("-------------------------------");
    }

    public void projectTotalCost(String name) {
        Optional<Project> projectOpt = Optional.ofNullable(projectService.getProjectById(name));
        if (projectOpt.isPresent()) {
            displayProjectCostDetails(projectOpt.get());
        } else {
            System.out.println("Projet n'existe pas");
        }
    }
}
