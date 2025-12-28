package db.products;

import db.DatabaseManager;
import components.CPU;
import db.ResultSetMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CPUDAO {

    private Connection getConnection() throws SQLException {
        return DatabaseManager.getInstance().getConnection();
    }

    public List<CPU> getAllCpus() {
        List<CPU> cpus = new ArrayList<>();

        String sql = "SELECT p.*, s.*, m.name AS manufacturer_name, c.category AS category_name " +
                "FROM products p " +
                "JOIN cpu_specs s ON p.id = s.product_id " +
                "JOIN manufacturers m ON p.manufacturer = m.id " +
                "JOIN product_categories c ON p.category = c.id";

        ResultSetMapper<CPU> mapper = new ResultSetMapper<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return mapper.mapResultSet(rs, CPU.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cpus;
    }

}