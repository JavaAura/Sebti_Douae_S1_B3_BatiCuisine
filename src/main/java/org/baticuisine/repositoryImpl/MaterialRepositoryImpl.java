package org.baticuisine.repositoryImpl;

import org.baticuisine.database.DatabaseConnection;
import org.baticuisine.entities.Material;
import org.baticuisine.repository.ComponentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaterialRepositoryImpl implements ComponentRepository<Material> {

    private static final Logger logger = LoggerFactory.getLogger(MaterialRepositoryImpl.class);
    private Connection conn;

    public MaterialRepositoryImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void addComponent(Material material) {
        String sql = "INSERT INTO material (name, tax_rate, project_id, transport_cost, quality_coefficient, unit_cost, quantity) VALUES (?,?,?,?,?,?,?) RETURNING id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (material.getProject() == null || material.getProject().getId() <= 0) {
                throw new IllegalArgumentException("Material must be associated with a valid project.");
            }

            pstmt.setString(1, material.getName());
            pstmt.setDouble(2, 0);
            pstmt.setInt(3, material.getProject().getId());
            pstmt.setDouble(4, material.getTransportCost());
            pstmt.setDouble(5, material.getQualityCoefficient());
            pstmt.setDouble(6, material.getUnitCost());
            pstmt.setDouble(7, material.getQuantity());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    material.setId(generatedId);
                    logger.info("Inserted Material with ID: {}", generatedId);
                }
            }
        } catch (SQLException e) {
            logger.error("Error adding material: {}", e.getMessage());
        }
    }

    @Override
    public void updateComponentTaxRate(int id, double taxRate) {
        String sql = "UPDATE material SET tax_rate = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, taxRate);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            logger.info("Updated tax rate for Material ID: {}", id);
        } catch (SQLException e) {
            logger.error("Error updating tax rate for Material ID {}: {}", id, e.getMessage());
        }
    }
}
