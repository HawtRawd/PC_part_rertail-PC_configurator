package controllers;

import components.User;
import components.UserSession;
import db.OrderDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Order;

import java.io.IOException;
import java.util.List;

public class OrderHistoryController {

    @FXML private VBox ordersContainer;
    private OrderDAO orderDAO = new OrderDAO();

    @FXML
    public void initialize() {
        loadOrderHistory();
    }

    private void loadOrderHistory() {
        User user = UserSession.getInstance().getCurrentUser();
        if (user == null) return;

        List<Order> myOrders = orderDAO.getOrdersByUserId(user.getId());

        ordersContainer.getChildren().clear();

        if (myOrders.isEmpty()) {
            ordersContainer.getChildren().add(new Label("No past orders found."));
        } else {
            for (Order order : myOrders) {
                VBox card = createOrderCard(order);
                ordersContainer.getChildren().add(card);
            }
        }
    }

    private VBox createOrderCard(Order order) {
        VBox card = new VBox(5);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label header = new Label("Order #" + order.getId() + "  |  " + order.getOrderDate());
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label price = new Label(String.format("Total: $%.2f", order.getTotalPrice()));
        price.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

        Label status = new Label("Status: " + order.getStatus());
        status.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");

        card.getChildren().addAll(header, status, price);
        return card;
    }

    @FXML
    public void handleBack(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }
}