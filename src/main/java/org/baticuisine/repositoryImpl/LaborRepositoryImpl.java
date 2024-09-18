package org.baticuisine.repositoryImpl;

import org.baticuisine.database.DatabaseConnection;
import org.baticuisine.entities.Labor;
import org.baticuisine.entities.Material;
import org.baticuisine.repository.ComponentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LaborRepositoryImpl implements ComponentRepository<Labor> {
    private Connection conn;

    public LaborRepositoryImpl(){
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void addComponent(Labor labor) {
        String sql = "INSERT INTO labor (name, tax_rate, project_id, hourly_rate, work_hours, worker_productivity)" +
                " VALUES (?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, labor.getName());
            pstmt.setDouble(2, labor.getTaxRate());
            pstmt.setInt(3, labor.getProject().getId());
            pstmt.setDouble(4, labor.getHourlyRate());
            pstmt.setDouble(5, labor.getWorkHours());
            pstmt.setDouble(6, labor.getWorkerProductivity());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding labor: " + e.getMessage());
        }
    }
}
