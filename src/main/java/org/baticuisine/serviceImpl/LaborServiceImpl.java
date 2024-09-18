package org.baticuisine.serviceImpl;

import org.baticuisine.entities.Labor;
import org.baticuisine.repository.ComponentRepository;
import org.baticuisine.repositoryImpl.LaborRepositoryImpl;
import org.baticuisine.service.ComponentService;

public class LaborServiceImpl implements ComponentService<Labor> {

    private ComponentRepository<Labor> laborRepository;

    public LaborServiceImpl() {
        this.laborRepository = new LaborRepositoryImpl();
    }

    @Override
    public void addComponent(Labor labor) {
        laborRepository.addComponent(labor);
    }
}
