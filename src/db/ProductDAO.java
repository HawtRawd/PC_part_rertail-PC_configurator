package db;

import components.*;
import db.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private Connection getConnection() throws SQLException {
        return DatabaseManager.getInstance().getConnection();
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT p.id, p.name, p.price, p.stock, p.image_url, " +
                "p.manufacturer AS manufacturer_id, m.name AS manufacturer_name, " +
                "p.category AS category_id, c.category AS category_name " +
                "FROM products p " +
                "JOIN manufacturers m ON p.manufacturer = m.id " +
                "JOIN product_categories c ON p.category = c.id";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = mapRowToProduct(rs);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.price, p.stock, p.image_url, " +
                "p.manufacturer AS manufacturer_id, m.name AS manufacturer_name, " +
                "p.category AS category_id, c.category AS category_name " +
                "FROM products p " +
                "JOIN manufacturers m ON p.manufacturer = m.id " +
                "JOIN product_categories c ON p.category = c.id " +
                "WHERE p.name LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%"); // Adds wildcards for partial match

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRowToProduct(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getInt("stock"),
                rs.getString("image_url"),
                rs.getInt("manufacturer_id"),   // The raw foreign key
                rs.getString("manufacturer_name"), // The joined name
                rs.getInt("category_id"),       // The raw foreign key
                rs.getString("category_name")   // The joined name
        );
    }
}