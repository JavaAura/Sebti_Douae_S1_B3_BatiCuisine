package org.baticuisine.repositoryImpl;

import org.baticuisine.database.DatabaseConnection;
import org.baticuisine.entities.Labor;
import org.baticuisine.repository.ComponentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LaborRepositoryImpl implements ComponentRepository<Labor> {
    private static final Logger logger = LoggerFactory.getLogger(LaborRepositoryImpl.class);
    private Connection conn;

    public LaborRepositoryImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void addComponent(Labor labor) {
        String sql = "INSERT INTO labor (name, tax_rate, project_id, hourly_rate, work_hours, worker_productivity) VALUES (?,?,?,?,?,?) RETURNING id";
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

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    labor.setId(generatedId);
                    logger.info("Inserted Labor with ID: {}", generatedId);
                }
            }
        } catch (SQLException e) {
            logger.error("Error adding labor: {}", e.getMessage());
        }
    }

    @Override
    public void updateComponentTaxRate(int laborId, double taxRate) {
        String sql = "UPDATE labor SET tax_rate = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, taxRate);
            stmt.setInt(2, laborId);
            stmt.executeUpdate();
            logger.info("Updated tax rate for Labor ID: {}", laborId);
        } catch (SQLException e) {
            logger.error("Error updating tax rate for Labor ID {}: {}", laborId, e.getMessage());
        }
    }
}
