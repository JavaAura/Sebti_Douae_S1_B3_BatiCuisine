package org.baticuisine.serviceImpl;

import org.baticuisine.entities.Material;
import org.baticuisine.repository.ComponentRepository;
import org.baticuisine.repositoryImpl.MaterialRepositoryImpl;
import org.baticuisine.service.ComponentService;

public class MaterialServiceImpl implements ComponentService<Material> {

    private ComponentRepository<Material> materialRepository;

    public MaterialServiceImpl(){
        this.materialRepository = new MaterialRepositoryImpl();
    }

    @Override
    public void addComponent(Material material){
        materialRepository.addComponent(material);
    }
}
