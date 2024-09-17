package org.baticuisine.repository;

import org.baticuisine.entities.Project;

import java.util.List;

public interface ProjectRepository {

    void addProject(Project project);

    List<Project> getAllProjects();
//    void displayProject(int projectId);
//    Project getProjectById(int projectId);

}
