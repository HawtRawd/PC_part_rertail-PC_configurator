package db.products;

import components.PSU;
import db.DatabaseManager;
import db.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PSUDAO {
    private Connection getConnection() throws SQLException{
        return DatabaseManager.getInstance().getConnection();
    }

    public List<PSU> getAllPsus() {
        List<PSU> psus = new ArrayList<>();

        String sql = "SELECT p.*, s.*, m.name AS manufacturer_name, c.category AS category_name " +
                "FROM products p " +
                "JOIN psu_specs s ON p.id = s.product_id " +
                "JOIN manufacturers m ON p.manufacturer = m.id " +
                "JOIN product_categories c ON p.category = c.id";

        ResultSetMapper<PSU> mapper = new ResultSetMapper<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return mapper.mapResultSet(rs, PSU.class);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}