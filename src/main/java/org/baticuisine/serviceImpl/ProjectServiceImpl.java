package org.baticuisine.serviceImpl;

import org.baticuisine.entities.Project;
import org.baticuisine.repository.ProjectRepository;
import org.baticuisine.repositoryImpl.ProjectRepositoryImpl;
import org.baticuisine.service.ProjectService;

import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;

    public ProjectServiceImpl() {
        this.projectRepository = new ProjectRepositoryImpl();
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
}
