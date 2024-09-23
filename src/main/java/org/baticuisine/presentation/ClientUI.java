package org.baticuisine.presentation;

import org.baticuisine.entities.Client;
import org.baticuisine.serviceImpl.ClientServiceImpl;
import org.baticuisine.util.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.Optional;

public class ClientUI {

    private static final Logger logger = LoggerFactory.getLogger(ClientUI.class);
    private final ClientServiceImpl clientService;

    public ClientUI() {
        this.clientService = new ClientServiceImpl();
    }

    public Client rechercherOuAjouterClient() {
        System.out.println("--- Recherche de client ---");

        int choix = InputValidator.getValidInt("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?\n1. Chercher un client existant\n2. Ajouter un nouveau client\nChoisissez une option :");

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

        Optional<Client> clientOpt = clientService.searchClientByName(nom);

        return clientOpt.map(client -> {
            System.out.println(ConsoleColors.GREEN + "Client trouvé !" + ConsoleColors.RESET);
            System.out.println("Nom : " + client.getName());
            System.out.println("Adresse : " + client.getAddress());
            System.out.println("Numéro de téléphone : " + client.getPhoneNumber());

            boolean continuer = InputValidator.getValidYesNo("Souhaitez-vous continuer avec ce client ?");
            return continuer ? client : null;
        }).orElseGet(() -> {
            System.out.println(ConsoleColors.RED + "Client non trouvé." + ConsoleColors.RESET);
            logger.warn("Client non trouvé: {}", nom);
            return rechercherOuAjouterClient();
        });
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

        return continuer ? client : null;
    }
}
