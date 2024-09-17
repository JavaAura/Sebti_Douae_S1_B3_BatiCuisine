package org.baticuisine;

import org.baticuisine.database.DatabaseConnection;
import org.baticuisine.entities.Client;
import org.baticuisine.entities.Project;
import org.baticuisine.repositoryImpl.ClientRepositoryImpl;
import org.baticuisine.repositoryImpl.ProjectRepositoryImpl;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args)  {

        System.out.println("Hello world!");
      //  Connection conn =   DatabaseConnection.getInstance().getConnection();
        ProjectRepositoryImpl projectrep = new ProjectRepositoryImpl();
        projectrep.getProjectById(2);
    }
}