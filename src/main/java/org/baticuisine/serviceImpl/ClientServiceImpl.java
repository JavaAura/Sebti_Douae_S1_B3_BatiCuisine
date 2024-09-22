package org.baticuisine.serviceImpl;

import org.baticuisine.service.ClientService;
import org.baticuisine.entities.Client;
import org.baticuisine.repository.ClientRepository;
import org.baticuisine.repositoryImpl.ClientRepositoryImpl;


    public class ClientServiceImpl implements ClientService {

        private ClientRepository clientRepository;

        public ClientServiceImpl() {
            this.clientRepository = new ClientRepositoryImpl();
        }

        @Override
        public void addClient(Client client) {
            clientRepository.addClient(client);
        }

        @Override
        public Client getClientById(int clientId) {
            return clientRepository.getClientById(clientId);
        }

        @Override
        public Client searchClientByName(String name) {
            return clientRepository.searchClientByName(name);
        }

        @Override
        public boolean isClientNameUnique(String name) {
            return clientRepository.isClientNameUnique(name);
        }

    }

