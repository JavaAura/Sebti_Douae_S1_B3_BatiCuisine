package org.baticuisine.repositoryImpl;

import org.baticuisine.database.DatabaseConnection;
import org.baticuisine.entities.Project;
import org.baticuisine.repository.ProjectRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjectRepositoryImpl implements ProjectRepository {
    private Connection conn;

    public ProjectRepositoryImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void addProject(Project project) {
        String sql = "INSERT INTO project (project_name, profit_margin, total_cost, client_id) VALUES (?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, project.getProjectName());
            pstmt.setDouble(2, project.getProfitMargin());
            pstmt.setDouble(3, project.getTotalCost());
            //pstmt.setObject(4, project.getStatus().name(), java.sql.Types.OTHER);
            pstmt.setInt(4, project.getClient().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



}
