package org.baticuisine.presentation;

import org.baticuisine.entities.Client;
import org.baticuisine.serviceImpl.ClientServiceImpl;
import org.baticuisine.util.InputValidator;

import java.util.Scanner;

public class ClientUI {

    private ClientServiceImpl clientService;

    public ClientUI() {
        this.clientService = new ClientServiceImpl();
    }

    public Client rechercherOuAjouterClient() {

        System.out.println("--- Recherche de client ---");

        int choix;
        do {
            System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
            System.out.println("1. Chercher un client existant");
            System.out.println("2. Ajouter un nouveau client");

            choix = InputValidator.getValidInt("Choisissez une option :");

            if (choix != 1 && choix != 2) {
                System.out.println("Option invalide. Veuillez choisir 1 ou 2.");
            }
        } while (choix != 1 && choix != 2);

        if (choix == 1) {
            return rechercherClient();
        } else {
            return ajouterNouveauClient();
        }
    }

        private Client rechercherClient() {
            Scanner scanner = new Scanner(System.in);

            System.out.println(ConsoleColors.BOLD_CYAN + "--- Recherche de client existant ---" + ConsoleColors.RESET);
            System.out.print("Entrez le nom du client : ");
            String nom = scanner.nextLine();

            Client client = clientService.searchClientByName(nom);

            if (client != null) {
                System.out.println(ConsoleColors.GREEN + "Client trouvé !" + ConsoleColors.RESET);
                System.out.println("Nom : " + client.getName());
                System.out.println("Adresse : " + client.getAddress());
                System.out.println("Numéro de téléphone : " + client.getPhoneNumber());

                boolean continuer = InputValidator.getValidYesNo("Souhaitez-vous continuer avec ce client ?");

                if (continuer) {
                    return client;
                }

            } else {
                System.out.println(ConsoleColors.RED + "Client non trouvé." + ConsoleColors.RESET);
                System.out.println("-------------------------------");
                return rechercherOuAjouterClient();
            }
            return null;
        }

    private Client ajouterNouveauClient() {
        Scanner scanner = new Scanner(System.in);
        String nom;
        System.out.println("--- Ajouter un nouveau client ---");
        do {
            System.out.print("Entrez le nom du client : ");
             nom = scanner.nextLine();
            if (!clientService.isClientNameUnique(nom)) {
                System.out.println("Ce nom de client existe déjà. Veuillez en choisir un autre.");
            }
        } while (!clientService.isClientNameUnique(nom));

        System.out.print("Entrez l'adresse du client : ");
        String adresse = scanner.nextLine();
        System.out.print("Entrez le numéro de téléphone du client : ");
        String telephone = scanner.nextLine();

        boolean isProfessional = InputValidator.getValidYesNo("Le client est-il un professionnel ?");

        Client client = new Client(nom, adresse, telephone, isProfessional);

        clientService.addClient(client);

        System.out.println("Client ajouté avec succès !");
        boolean continuer = InputValidator.getValidYesNo("Souhaitez-vous continuer avec ce client ?");

        if (continuer) {
            return client;
        }
        return null;
    }


}
