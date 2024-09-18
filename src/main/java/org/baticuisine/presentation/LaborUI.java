package org.baticuisine.presentation;

import org.baticuisine.entities.Labor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LaborUI {

    public List<Labor> ajouterMainDOeuvre() {
        Scanner scanner = new Scanner(System.in);
        List<Labor> mainDOeuvre = new ArrayList<>();
        boolean ajouterPlus = true;

        System.out.println("--- Ajout de la main-d'œuvre ---");

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
            mainDOeuvre.add(labor);

            System.out.println("Main-d'œuvre ajoutée avec succès !");
            System.out.print("Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n) : ");
            String continuer = scanner.nextLine();

            ajouterPlus = continuer.equalsIgnoreCase("y");
        }

        return mainDOeuvre;
    }
}
