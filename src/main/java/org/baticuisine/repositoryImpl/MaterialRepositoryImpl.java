package org.baticuisine.repositoryImpl;

import org.baticuisine.database.DatabaseConnection;
import org.baticuisine.entities.Component;
import org.baticuisine.entities.Material;
import org.baticuisine.repository.ComponentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MaterialRepositoryImpl implements ComponentRepository<Material> {

    private Connection conn;

    public MaterialRepositoryImpl(){
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void addComponent(Material material){
        String sql = "INSERT INTO material (name, unit_cost, quantity, tax_rate, project_id, transport_cost, quality_coefficient )" +
                " VALUES (?,?,?,?,?,?,?)";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, material.getName());
            pstmt.setDouble(2, material.getUnitCost());
            pstmt.setDouble(3, material.getQuantity());
            pstmt.setDouble(4, material.getTaxRate());
            pstmt.setInt(5, material.getProject().getId());
            pstmt.setDouble(6, material.getTransportCost());
            pstmt.setDouble(7, material.getQualityCoefficient());
            pstmt.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
