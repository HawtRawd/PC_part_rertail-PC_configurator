package db.products;

import components.CPU;
import components.RAM;
import db.DatabaseManager;
import db.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RAMDAO {
    private Connection getConnection() throws SQLException{
        return DatabaseManager.getInstance().getConnection();
    }

    public RAM getRamById(int id){
        List<RAM> rams = getAllRam();
        return rams.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public List<RAM> getAllRam() {
        List<RAM> ram = new ArrayList<>();

        String sql = "SELECT p.*, s.*, m.name AS manufacturer_name, c.category AS category_name " +
                "FROM products p " +
                "JOIN ram_specs s ON p.id = s.product_id " +
                "JOIN manufacturers m ON p.manufacturer_id = m.id " +
                "JOIN product_categories c ON p.category_id = c.id";

        ResultSetMapper<RAM> mapper = new ResultSetMapper<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return mapper.mapResultSet(rs, RAM.class);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}