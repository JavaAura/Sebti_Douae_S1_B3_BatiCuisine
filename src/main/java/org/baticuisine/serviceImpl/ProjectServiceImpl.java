package org.baticuisine.serviceImpl;

import org.baticuisine.entities.Component;
import org.baticuisine.entities.Labor;
import org.baticuisine.entities.Material;
import org.baticuisine.entities.Project;
import org.baticuisine.repository.ProjectRepository;
import org.baticuisine.repositoryImpl.LaborRepositoryImpl;
import org.baticuisine.repositoryImpl.MaterialRepositoryImpl;
import org.baticuisine.repositoryImpl.ProjectRepositoryImpl;
import org.baticuisine.service.ProjectService;

import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;
    private MaterialRepositoryImpl materialRepository;
    private LaborRepositoryImpl laborRepository;

    public ProjectServiceImpl() {
        this.projectRepository = new ProjectRepositoryImpl();
        this.materialRepository = new MaterialRepositoryImpl();
        this.laborRepository =  new LaborRepositoryImpl();
    }

    @Override
    public void addProject(Project project) {
        projectRepository.addProject(project);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.getAllProjects();
    }

    @Override
    public Project getProjectById(int projectId) {
        return projectRepository.getProjectById(projectId);
    }

@Override
    public void applyTaxAndProfitMargin(Project project, double taxRate, double profitMargin) {
        for (Component component : project.getComponents()) {
            if (component instanceof Material) {
                Material material = (Material) component;
                material.setTaxRate(taxRate);
                materialRepository.updateComponentTaxRate(material.getId(), taxRate);
            } else if (component instanceof Labor) {
                Labor labor = (Labor) component;
                labor.setTaxRate(taxRate);
                laborRepository.updateComponentTaxRate(labor.getId(), taxRate);
            }
        }
        project.setProfitMargin(profitMargin);

        double totalCostBeforeMargin = calculateTotalCostBeforeMargin(project);
        double marginAmount = totalCostBeforeMargin * (profitMargin / 100);
        double totalCostAfterMargin = totalCostBeforeMargin + marginAmount;
        project.setTotalCost(totalCostAfterMargin);
        projectRepository.updateProjectProfitMarginAndTotalCost(project.getId(), profitMargin, totalCostAfterMargin);
    }

    @Override
    public double calculateTotalCostBeforeMargin(Project project) {
        double totalMaterialCost = 0;
        double totalLaborCost = 0;

        for (Component component : project.getComponents()) {
            if (component instanceof Material) {
                Material material = (Material) component;
                totalMaterialCost += material.getUnitCost() * material.getQuantity();
            } else if (component instanceof Labor) {
                Labor labor = (Labor) component;
                totalLaborCost += labor.getHourlyRate() * labor.getWorkHours();
            }
        }

        return totalMaterialCost + totalLaborCost;
    }

}
