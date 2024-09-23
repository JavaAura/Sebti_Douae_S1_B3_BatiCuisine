package org.baticuisine.service;

import org.baticuisine.entities.Client;

import java.util.Optional;
public interface ClientService {
    void addClient(Client client);

    Optional<Client> getClientById(int clientId);

    Optional<Client> searchClientByName(String name);

    boolean isClientNameUnique(String name);
}
