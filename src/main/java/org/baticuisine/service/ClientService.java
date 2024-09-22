package org.baticuisine.service;

import org.baticuisine.entities.Client;

public interface ClientService {
    void addClient(Client client);

    Client getClientById(int clientId);

    Client searchClientByName(String name);

    boolean isClientNameUnique(String name);
}
