package org.baticuisine.serviceImpl;

import org.baticuisine.entities.Client;
import org.baticuisine.repository.ClientRepository;
import org.baticuisine.repositoryImpl.ClientRepositoryImpl;
import org.baticuisine.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ClientServiceImpl implements ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
    private final ClientRepository clientRepository;

    public ClientServiceImpl() {
        this.clientRepository = new ClientRepositoryImpl();
    }

    @Override
    public void addClient(Client client) {
        try {
            clientRepository.addClient(client);
            logger.info("Client added successfully: {}", client.getName());
        } catch (Exception e) {
            logger.error("Error adding client: {}", e.getMessage());
        }
    }

    @Override
    public Optional<Client> getClientById(int clientId) {
        try {
            Optional<Client> client = clientRepository.getClientById(clientId);
            if (client.isPresent()) {
                logger.info("Client found with ID: {}", clientId);
            } else {
                logger.warn("No client found with ID: {}", clientId);
            }
            return client;
        } catch (Exception e) {
            logger.error("Error fetching client by ID: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Client> searchClientByName(String name) {
        try {
            Optional<Client> client = clientRepository.searchClientByName(name);
            if (!client.isPresent())
            {
                logger.warn("No client found with name: {}", name);
            }
            return client;
        } catch (Exception e) {
            logger.error("Error searching client by name: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean isClientNameUnique(String name) {
        try {
            boolean isUnique = clientRepository.isClientNameUnique(name);
            if (isUnique) {
                logger.info("Client name '{}' is unique", name);
            } else {
                logger.warn("Client name '{}' is already in use", name);
            }
            return isUnique;
        } catch (Exception e) {
            logger.error("Error checking if client name is unique: {}", e.getMessage());
            return false;
        }
    }
}
