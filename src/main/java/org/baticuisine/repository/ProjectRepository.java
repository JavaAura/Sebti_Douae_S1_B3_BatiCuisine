package org.baticuisine.repository;

import org.baticuisine.entities.Project;

import java.util.List;

public interface ProjectRepository {

    void addProject(Project project);

    List<Project> getAllProjects();

    Project getProjectByName(String projectName);

    void updateProjectProfitMarginAndTotalCost(int projectId, double profitMargin, double totalCost);
}
