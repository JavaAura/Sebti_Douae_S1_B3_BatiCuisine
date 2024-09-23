package org.baticuisine.repository;

import org.baticuisine.entities.Client;

import java.util.Optional;

public interface ClientRepository {
    void addClient(Client client);
    Optional<Client> getClientById(int clientId);
    Optional<Client> searchClientByName(String name);
    boolean isClientNameUnique(String name);
}
