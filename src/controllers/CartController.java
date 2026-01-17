package controllers;

import components.UserSession;
import db.OrderDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.CartItem;

import java.io.IOException;
import java.util.List;

public class CartController {

    @FXML private VBox cartItemsContainer;
    @FXML private Label totalLabel;

    private OrderDAO orderDAO = new OrderDAO();

    @FXML
    public void initialize() {
        renderCart();
    }

    private void renderCart() {
        List<CartItem> items = UserSession.getInstance().getCart();
        double total = UserSession.getInstance().getCartTotal();

        if (items.isEmpty()) {
            Label emptyLbl = new Label("Your cart is empty.");
            emptyLbl.setStyle("-fx-font-size: 18px; -fx-text-fill: #999;");
            cartItemsContainer.getChildren().add(emptyLbl);
        } else {
            for (CartItem item : items) {
                HBox row = createCartRow(item);
                cartItemsContainer.getChildren().add(row);
            }
        }
        updateTotal();
    }

    private void updateTotal() {
        double total = UserSession.getInstance().getCartTotal();
        totalLabel.setText(String.format("$%.2f", total));
    }

    private HBox createCartRow(CartItem item) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 1);");

        ImageView imgView = new ImageView();
        imgView.setFitWidth(60);
        imgView.setFitHeight(60);
        imgView.setPreserveRatio(true);
        if (item.getImageUrl() != null) {
            try {
                imgView.setImage(new Image(item.getImageUrl(), true));
            } catch (Exception e) { /* ignore */ }
        }

        Label nameLbl = new Label(item.getName());
        nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label priceLbl = new Label(String.format("$%.2f", item.getPrice()));
        priceLbl.setStyle("-fx-text-fill: green;");

        VBox info = new VBox(5, nameLbl, priceLbl);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button removeBtn = new Button("Remove");
        removeBtn.setStyle("-fx-text-fill: red; -fx-background-color: transparent; -fx-cursor: hand;");
        removeBtn.setOnAction(e -> {
            UserSession.getInstance().removeFromCart(item);
            renderCart();
        });

        row.getChildren().addAll(imgView, info, spacer, removeBtn);
        return row;
    }

    @FXML
    public void handleCheckout(javafx.event.ActionEvent event) {
        components.User user = UserSession.getInstance().getCurrentUser();
        List<CartItem> cart = UserSession.getInstance().getCart();

        if (cart.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cart is Empty", "Add items before checking out!");
            return;
        }

        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Not Logged In", "You must log in to checkout.");
            return;
        }

        boolean success = orderDAO.checkout(user, cart);

        if (success) {
            UserSession.getInstance().clearCart();
            showAlert(Alert.AlertType.INFORMATION, "Success!", "Order placed successfully.");
            renderCart();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to place order. Database connection error.");
        }
    }

    @FXML
    public void handleClearCart() {
        UserSession.getInstance().clearCart();
        renderCart();
    }

    @FXML
    public void handleBack(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}