package db;

import components.Build;
import components.Product;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class OrderDAO {

    /**
     * Places an order that can contain both single products AND full builds.
     */
    public boolean placeMixedOrder(int userId, List<Product> singleProducts, List<Integer> buildIds) {

        // 1. Calculate Total Price
        double total = 0;
        for (Product p : singleProducts) total += p.getPrice();

        // (You would need a helper method to get price of builds, assuming 0 for now to keep code simple)
        // double buildTotal = getBuildPrice(buildIds);
        // total += buildTotal;

        String sqlOrder = "INSERT INTO orders (user_id, total_price) VALUES (?, ?) RETURNING id";
        String sqlItemProduct = "INSERT INTO order_items (order_id, product_id, price_at_purchase) VALUES (?, ?, ?)";
        String sqlItemBuild = "INSERT INTO order_items (order_id, build_id, price_at_purchase) VALUES (?, ?, ?)";

        String sqlDeductStock = "UPDATE products SET stock = stock - 1 WHERE id = ? AND stock > 0";

        try (Connection conn = DatabaseManager.getInstance().getConnection()) {
            conn.setAutoCommit(false); // Start Transaction

            try {
                // --- STEP A: CREATE ORDER RECORD ---
                int orderId = -1;
                try (PreparedStatement stmt = conn.prepareStatement(sqlOrder)) {
                    stmt.setInt(1, userId);
                    stmt.setDouble(2, total);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) orderId = rs.getInt("id");
                }

                // --- STEP B: PROCESS SINGLE PRODUCTS ---
                for (Product p : singleProducts) {
                    // 1. Deduct Stock
                    deductStock(conn, p.getId(), p.getName());

                    // 2. Add to Receipt
                    try (PreparedStatement stmt = conn.prepareStatement(sqlItemProduct)) {
                        stmt.setInt(1, orderId);
                        stmt.setInt(2, p.getId());
                        stmt.setDouble(3, p.getPrice());
                        stmt.executeUpdate();
                    }
                }

                // --- STEP C: PROCESS BUILDS ---
                for (Integer buildId : buildIds) {
                    // 1. "Explode" the build to find all part IDs inside it
                    Build build = BuildDAO.getBuildById(buildId);
                    List<Integer> allParts = build.getAllPartIds();

                    // 2. Deduct stock for EVERY part in the build
                    for (int partId : allParts) {
                        deductStock(conn, partId, "Component in Build #" + buildId);
                    }

                    // 3. Add the BUILD itself to the receipt (so user sees "My Gaming PC")
                    // (You'd ideally fetch the build's total price here)
                    try (PreparedStatement stmt = conn.prepareStatement(sqlItemBuild)) {
                        stmt.setInt(1, orderId);
                        stmt.setInt(2, buildId);
                        stmt.setDouble(3, 0.00); // Replace with actual build price calculation
                        stmt.executeUpdate();
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Order Failed: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper to deduct stock or throw error
    private void deductStock(Connection conn, int productId, String itemName) throws SQLException {
        String sql = "UPDATE products SET stock = stock - 1 WHERE id = ? AND stock > 0";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Out of Stock: Product ID " + productId + " (" + itemName + ")");
            }
        }
    }

    // Helper to find all parts inside a build (Queries your Join Tables)
    private List<Integer> getPartIdsForBuild(Connection conn, int buildId) throws SQLException {
        List<Integer> ids = new ArrayList<>();

        String sql = "SELECT cpu_id, motherboard_id, psu_id, case_id, cooler_id FROM pc_build WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, buildId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cpu_id") != 0) ids.add(rs.getInt("cpu_id"));
                if (rs.getInt("motherboard_id") != 0) ids.add(rs.getInt("motherboard_id"));
            }
        }
        return ids;
    }
}