package org.baticuisine.repositoryImpl;

import org.baticuisine.Status;
import org.baticuisine.database.DatabaseConnection;
import org.baticuisine.entities.Client;
import org.baticuisine.entities.Project;
import org.baticuisine.repository.ProjectRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepositoryImpl implements ProjectRepository {
    private Connection conn;

    public ProjectRepositoryImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void addProject(Project project) {
        String sql = "INSERT INTO project (project_name, profit_margin, total_cost, client_id) VALUES (?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, project.getProjectName());
            pstmt.setDouble(2, project.getProfitMargin());
            pstmt.setDouble(3, project.getTotalCost());
            pstmt.setInt(4, project.getClient().getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        project.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public List<Project> getAllProjects() {
        String sql = "SELECT p.id, p.project_name, p.profit_margin, p.total_cost, p.status, p.client_id, c.name AS client_name " +
                "FROM project p " +
                "JOIN client c ON p.client_id = c.id";
        List<Project> projects = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("id"));
                project.setProjectName(rs.getString("project_name"));
                project.setProfitMargin(rs.getDouble("profit_margin"));
                project.setTotalCost(rs.getDouble("total_cost"));
                project.setStatus(Status.valueOf(rs.getString("status")));

                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                client.setName(rs.getString("client_name"));
                project.setClient(client);

                projects.add(project);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching projects: " + e.getMessage());
        }
        return projects;
    }

    @Override
    public Project getProjectById(int projectId) {
        String sql = "SELECT p.id, p.project_name, p.profit_margin, p.total_cost, p.status, p.client_id, c.name AS client_name " +
                "FROM project p " +
                "JOIN client c ON p.client_id = c.id " +
                "WHERE p.id = ?";
        Project project = null;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                project = new Project();
                project.setId(rs.getInt("id"));
                project.setProjectName(rs.getString("project_name"));
                project.setProfitMargin(rs.getDouble("profit_margin"));
                project.setTotalCost(rs.getDouble("total_cost"));
                project.setStatus(Status.valueOf(rs.getString("status")));

                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                client.setName(rs.getString("client_name"));
                project.setClient(client);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching project: " + e.getMessage());
        }
        System.out.println(project.getProjectName());
        return project;
    }

    @Override
    public void updateProjectProfitMarginAndTotalCost(int projectId, double profitMargin, double totalCost) {
        String sql = "UPDATE Project SET profit_margin = ?, total_cost = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, profitMargin);
            stmt.setDouble(2, totalCost);
            stmt.setInt(3, projectId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}



