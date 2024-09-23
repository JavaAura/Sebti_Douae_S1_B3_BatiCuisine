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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
    private ProjectRepository projectRepository;
    private MaterialRepositoryImpl materialRepository;
    private LaborRepositoryImpl laborRepository;

    public ProjectServiceImpl() {
        this.projectRepository = new ProjectRepositoryImpl();
        this.materialRepository = new MaterialRepositoryImpl();
        this.laborRepository = new LaborRepositoryImpl();
    }

    @Override
    public void addProject(Project project) {
        projectRepository.addProject(project);
        logger.info("Project added: {}", project.getProjectName());
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.getAllProjects();
    }

    @Override
    public Project getProjectById(String projectName) {
        return projectRepository.getProjectById(projectName);
    }

    @Override
    public void applyTaxAndProfitMargin(Project project, double taxRate, double profitMargin) {
        logger.info("Applying tax and profit margin for project: {}", project.getProjectName());

        double totalMaterialCostAfterTax = project.getComponents().stream()
                .filter(component -> component instanceof Material)
                .map(component -> (Material) component)
                .mapToDouble(material -> {
                    material.setTaxRate(taxRate);
                    double costBeforeTax = material.getUnitCost() * material.getQuantity() * material.getQualityCoefficient() + material.getTransportCost();
                    double costAfterTax = costBeforeTax + (costBeforeTax * (taxRate / 100));
                    materialRepository.updateComponentTaxRate(material.getId(), taxRate);
                    return costAfterTax;
                })
                .sum();

        double totalLaborCostAfterTax = project.getComponents().stream()
                .filter(component -> component instanceof Labor)
                .map(component -> (Labor) component)
                .mapToDouble(labor -> {
                    labor.setTaxRate(taxRate);
                    double costBeforeTax = labor.getHourlyRate() * labor.getWorkHours() * labor.getWorkerProductivity();
                    double costAfterTax = costBeforeTax + (costBeforeTax * (taxRate / 100));
                    laborRepository.updateComponentTaxRate(labor.getId(), taxRate);
                    return costAfterTax;
                })
                .sum();

        double totalCostBeforeMargin = totalMaterialCostAfterTax + totalLaborCostAfterTax;
        double marginAmount = totalCostBeforeMargin * (profitMargin / 100);
        double totalCostAfterMargin = totalCostBeforeMargin + marginAmount;

        project.setProfitMargin(profitMargin);
        project.setTotalCost(totalCostAfterMargin);

        projectRepository.updateProjectProfitMarginAndTotalCost(project.getId(), profitMargin, totalCostAfterMargin);
        logger.info("Updated project with ID {}: Profit Margin = {}, Total Cost = {}", project.getId(), profitMargin, totalCostAfterMargin);
    }
}
