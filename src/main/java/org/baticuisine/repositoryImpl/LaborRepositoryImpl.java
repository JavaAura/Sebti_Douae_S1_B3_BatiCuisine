package org.baticuisine.repositoryImpl;

import org.baticuisine.database.DatabaseConnection;
import org.baticuisine.entities.Labor;
import org.baticuisine.entities.Material;
import org.baticuisine.repository.ComponentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LaborRepositoryImpl implements ComponentRepository<Labor> {
    private Connection conn;

    public LaborRepositoryImpl(){
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void addComponent(Labor labor) {
        String sql = "INSERT INTO labor (name, tax_rate, project_id, hourly_rate, work_hours, worker_productivity)" +
                " VALUES (?,?,?,?,?,?) RETURNING id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (labor.getProject() == null || labor.getProject().getId() <= 0) {
                throw new IllegalArgumentException("Labor must be associated with a valid project.");
            }

            pstmt.setString(1, labor.getName());
            pstmt.setDouble(2, labor.getTaxRate());
            pstmt.setInt(3, labor.getProject().getId());
            pstmt.setDouble(4, labor.getHourlyRate());
            pstmt.setDouble(5, labor.getWorkHours());
            pstmt.setDouble(6, labor.getWorkerProductivity());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int generatedId = rs.getInt(1);
                labor.setId(generatedId);
                System.out.println("Inserted Material with ID: " + generatedId);
            }
        } catch (SQLException e) {
            System.err.println("Error adding labor: " + e.getMessage());
        }
    }

    @Override
    public void updateComponentTaxRate(int laborId, double taxRate){
        String sql = "UPDATE labor SET tax_rate = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, taxRate);
            stmt.setInt(2, laborId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
