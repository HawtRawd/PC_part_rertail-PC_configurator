package models;

import java.sql.Timestamp;

public class Order {
    private int id;
    private int userId;
    private double totalPrice;
    private Timestamp createdAt;
    private String status;

    public Order() {}

    public Order(int id, int userId, double totalPrice, Timestamp orderDate, String status) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.createdAt = orderDate;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public Timestamp getOrderDate() { return createdAt; }
    public void setOrderDate(Timestamp orderDate) { this.createdAt = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}