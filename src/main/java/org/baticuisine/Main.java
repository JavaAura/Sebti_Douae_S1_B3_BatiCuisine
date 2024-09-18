package org.baticuisine;

import org.baticuisine.database.DatabaseConnection;
import org.baticuisine.entities.Client;
import org.baticuisine.entities.Labor;
import org.baticuisine.entities.Material;
import org.baticuisine.entities.Project;
import org.baticuisine.repositoryImpl.ClientRepositoryImpl;
import org.baticuisine.repositoryImpl.LaborRepositoryImpl;
import org.baticuisine.repositoryImpl.MaterialRepositoryImpl;
import org.baticuisine.repositoryImpl.ProjectRepositoryImpl;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args)  {

        System.out.println("Hello world!");
      //  Connection conn =   DatabaseConnection.getInstance().getConnection();
        MaterialRepositoryImpl materialrep =  new MaterialRepositoryImpl();
        Material material = new Material();

        Project project = new Project();
        project.setId(1);
        material.setName("Cement");
        material.setUnitCost(50.0);
        material.setQuantity(200);
        material.setTaxRate(0.12);
        material.setTransportCost(15.0);
        material.setQualityCoefficient(1.05);
        material.setProject(project);
        materialrep.addComponent(material);

        LaborRepositoryImpl laborrep =  new LaborRepositoryImpl();
        Labor labor = new Labor();

        labor.setName("labor1");
        labor.setTaxRate(0.20);
        labor.setHourlyRate(12.0);
        labor.setWorkHours(10);
        labor.setWorkerProductivity(20);
        labor.setProject(project);
        laborrep.addComponent(labor);
    }
}