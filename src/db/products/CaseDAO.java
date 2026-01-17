package db.products;

import components.CPU;
import components.Case;
import db.DatabaseManager;
import db.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CaseDAO {
    private Connection getConnection() throws SQLException{
        return DatabaseManager.getInstance().getConnection();
    }

    public Case getCaseById(int id){
        List<Case> pcCases = getAllCases();
        return pcCases.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public List<Case> getAllCases() {
        List<Case> cases = new ArrayList<>();

        String sql = "SELECT p.*, s.*, m.name AS manufacturer_name, c.category AS category_name " +
                "FROM products p " +
                "JOIN case_specs s ON p.id = s.product_id " +
                "JOIN manufacturers m ON p.manufacturer_id = m.id " +
                "JOIN product_categories c ON p.category_id = c.id";

        ResultSetMapper<Case> mapper = new ResultSetMapper<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return mapper.mapResultSet(rs, Case.class);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}