package db;

import components.User;
import models.CartItem;
import models.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public boolean checkout(User user, List<CartItem> cartItems) {
        String insertOrder = "INSERT INTO orders (user_id, total_price, status) VALUES (?, ?, ?) RETURNING id";
        String insertItem = "INSERT INTO order_items (order_id, product_id, build_id, quantity, price_at_purchase) VALUES (?, ?, ?, ?, ?)";

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

                try (PreparedStatement stmt = conn.prepareStatement(insertItem)) {
                    for (CartItem item : cartItems) {
                        stmt.setInt(1, orderId);

                        if (item.getProduct() != null) {
                            stmt.setInt(2, item.getProduct().getId());
                            stmt.setObject(3, null);
                            stmt.setInt(4, 1);
                        } else if (item.getBuild() != null) {
                            stmt.setObject(2, null);
                            stmt.setInt(3, item.getBuild().getId());
                            stmt.setInt(4, 1);
                        }
                        stmt.setDouble(5, item.getPrice());
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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