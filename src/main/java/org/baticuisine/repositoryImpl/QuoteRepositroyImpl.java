package org.baticuisine.repositoryImpl;

import org.baticuisine.database.DatabaseConnection;
import org.baticuisine.entities.Quote;
import org.baticuisine.repository.QuoteRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuoteRepositroyImpl implements QuoteRepository {

    private Connection conn;

    public QuoteRepositroyImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void addQuote(Quote quote) {
        String sql = "INSERT INTO quote (estimated_amount, issue_date, validity_date, is_accepted, project_id) VALUES (?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, quote.getEstimatedAmount());
            pstmt.setDate(2, Date.valueOf(quote.getIssueDate()));
            pstmt.setDate(3, Date.valueOf(quote.getValidityDate()));
            pstmt.setBoolean(4, quote.isAccepted());
            pstmt.setInt(5, quote.getProject().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
