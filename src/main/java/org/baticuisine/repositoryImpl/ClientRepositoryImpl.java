package org.baticuisine.repositoryImpl;

import org.baticuisine.database.DatabaseConnection;
import org.baticuisine.entities.Client;
import org.baticuisine.repository.ClientRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientRepositoryImpl implements ClientRepository {

    private Connection conn;

    public ClientRepositoryImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void addClient(Client client){
        String sql = "INSERT INTO client (name,address,phone_number,is_professional) VALUES (?,?,?,?)";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getAddress());
            pstmt.setString(3, client.getPhoneNumber());
            pstmt.setBoolean(4, client.getProfessional());
            pstmt.executeUpdate();
        }catch(SQLException e){
            System.out.println("e.getMessage()");
        }
    }

    @Override
    public Client getClientById(int clientId) {
        String sql = "SELECT * FROM client WHERE id = ?";
        Client client = null;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                client.setAddress(rs.getString("address"));
                client.setPhoneNumber(rs.getString("phone_number"));
                client.setProfessional(rs.getBoolean("is_professional"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return client;
    }

    @Override
    public Client searchClientByName(String name) {
        String sql = "SELECT * FROM client WHERE name = ?";
        Client client = null;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                client.setAddress(rs.getString("address"));
                client.setPhoneNumber(rs.getString("phone_number"));
                client.setProfessional(rs.getBoolean("is_professional"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return client;
    }
}
