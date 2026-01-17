package db.products;

import components.*;
import db.DatabaseManager;
import db.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccessoryDAO {
    private Connection getConnection() throws SQLException{
        return DatabaseManager.getInstance().getConnection();
    }

    public Accessory getAccById(int id){
        List<Accessory> accs = getAllaccs();
        return accs.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public List<Accessory> getAllaccs() {
        List<Accessory> accs = new ArrayList<>();

        String sql = "SELECT p.*, s.*, m.name AS manufacturer_name, c.category AS category_name " +
                "FROM products p " +
                "JOIN accessories s ON p.id = s.id " +
                "JOIN manufacturers m ON p.manufacture_id = m.id " +
                "JOIN product_categories c ON p.category_id = c.id";

        ResultSetMapper<Accessory> mapper = new ResultSetMapper<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return mapper.mapResultSet(rs, Accessory.class);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}