package org.baticuisine.repositoryImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.baticuisine.database.DatabaseConnection;
import org.baticuisine.entities.Client;
import org.baticuisine.repository.ClientRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ClientRepositoryImpl implements ClientRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRepositoryImpl.class);
    private final Connection conn;

    public ClientRepositoryImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void addClient(Client client) {
        String sql = "INSERT INTO client (name, address, phone_number, is_professional, discount) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getAddress());
            pstmt.setString(3, client.getPhoneNumber());
            pstmt.setBoolean(4, client.getProfessional());
            pstmt.setDouble(5, client.getDiscount());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        client.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error adding client: {}", e.getMessage(), e);
        }
    }

    @Override
    public Optional<Client> getClientById(int clientId) {
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
                client.setDiscount(rs.getDouble("discount"));
            }
        } catch (SQLException e) {
            LOGGER.error("Error retrieving client by ID: {}", e.getMessage(), e);
        }

        return Optional.ofNullable(client);
    }

    @Override
    public Optional<Client> searchClientByName(String name) {
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
                client.setDiscount(rs.getDouble("discount"));
            }
        } catch (SQLException e) {
            LOGGER.error("Error searching client by name: {}", e.getMessage(), e);
        }

        return Optional.ofNullable(client);
    }

    @Override
    public boolean isClientNameUnique(String name) {
        String sql = "SELECT COUNT(*) FROM client WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            LOGGER.error("Error checking client name uniqueness: {}", e.getMessage(), e);
        }
        return false;
    }
}
