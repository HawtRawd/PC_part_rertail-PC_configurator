package db;

import components.Build;
import components.Product;
import components.User;
import models.CartItem;
import models.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public boolean checkout(User user, List<CartItem> cartItems) {
        String insertOrder = "INSERT INTO orders (user_id, total_price, status) VALUES (?, ?, ?) RETURNING id";
        String insertItem = "INSERT INTO order_items (order_id, product_id, build_id, price_at_purchase) VALUES (?, ?, ?, ?)";
        String updateStock = "UPDATE products SET stock = stock - 1 WHERE id = ? AND stock > 0";

        double total = cartItems.stream().mapToDouble(CartItem::getPrice).sum();

        try (Connection conn = DatabaseManager.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            try {
                int orderId = -1;
                try (PreparedStatement stmt = conn.prepareStatement(insertOrder)) {
                    stmt.setInt(1, user.getId());
                    stmt.setDouble(2, total);
                    stmt.setString(3, "PROCESSING");

                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) orderId = rs.getInt(1);
                }

                try (PreparedStatement itemStmt = conn.prepareStatement(insertItem);
                     PreparedStatement stockStmt = conn.prepareStatement(updateStock)) {

                    for (CartItem item : cartItems) {
                        if (item.getProduct() != null) {
                            itemStmt.setInt(1, orderId);
                            itemStmt.setInt(2, item.getProduct().getId());
                            itemStmt.setObject(3, null);
                            itemStmt.setDouble(4, item.getPrice());
                            itemStmt.addBatch();

                            if (!reduceStock(stockStmt, item.getProduct().getId(), item.getProduct().getName())) {
                                conn.rollback();
                                return false;
                            }
                        }
                        else if (item.getBuild() != null) {
                            itemStmt.setInt(1, orderId);
                            itemStmt.setObject(2, null);
                            itemStmt.setInt(3, item.getBuild().getId());
                            itemStmt.setDouble(4, item.getPrice());
                            itemStmt.addBatch();

                            if (!reduceBuildStock(stockStmt, item.getBuild())) {
                                conn.rollback();
                                return false;
                            }
                        }
                    }
                    itemStmt.executeBatch();
                }

                conn.commit();
                return true;

            } catch (SQLException | OutOfStockException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean reduceStock(PreparedStatement stmt, int productId, String productName) throws SQLException, OutOfStockException {
        stmt.setInt(1, productId);
        int rowsUpdated = stmt.executeUpdate();

        if (rowsUpdated == 0) {
            throw new OutOfStockException("Item out of stock: " + productName);
        }
        return true;
    }

    private boolean reduceBuildStock(PreparedStatement stmt, Build build) throws SQLException, OutOfStockException {
        if (build.getCpu() != null) reduceStock(stmt, build.getCpu().getId(), build.getCpu().getName());
        if (build.getMobo() != null) reduceStock(stmt, build.getMobo().getId(), build.getMobo().getName());
        if (build.getPsu() != null) reduceStock(stmt, build.getPsu().getId(), build.getPsu().getName());
        if (build.getPcCase() != null) reduceStock(stmt, build.getPcCase().getId(), build.getPcCase().getName());
        if (build.getCooler() != null) reduceStock(stmt, build.getCooler().getId(), build.getCooler().getName());

        if (build.getGpus() != null) {
            for (Product p : build.getGpus()) reduceStock(stmt, p.getId(), p.getName());
        }
        if (build.getRams() != null) {
            for (Product p : build.getRams()) reduceStock(stmt, p.getId(), p.getName());
        }
        if (build.getStorages() != null) {
            for (Product p : build.getStorages()) reduceStock(stmt, p.getId(), p.getName());
        }
        if (build.getAccessories() != null) {
            for (Product p : build.getAccessories()) reduceStock(stmt, p.getId(), p.getName());
        }

        return true;
    }

    private static class OutOfStockException extends Exception {
        public OutOfStockException(String message) {
            super(message);
        }
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setOrderDate(rs.getTimestamp("created_at"));
                order.setStatus(rs.getString("status"));
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}