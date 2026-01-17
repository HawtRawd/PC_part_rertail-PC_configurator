package controllers;

import components.Product;
import components.UserSession;
import components.User;
import db.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private TextField searchField;
    @FXML private TableView<Product> productTable;

    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, String> colManufacturer;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;

    @FXML private TableColumn<Product, String> colImage;
    @FXML private TableColumn<Product, Void> colAction;

    private User currentUser;
    private ProductDAO productDAO = new ProductDAO();

    @FXML
    public void initialize() {
        colManufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturerName"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        colImage.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));

        colImage.setCellFactory(column -> new TableCell<Product, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (empty || imageUrl == null || imageUrl.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        Image image = new Image(imageUrl, true);
                        imageView.setImage(image);
                        imageView.setFitHeight(50);
                        imageView.setFitWidth(50);
                        imageView.setPreserveRatio(true);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });

        colAction.setCellFactory(param -> new TableCell<Product, Void>() {
            private final Button btn = new Button("+");

            {
                btn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
                btn.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    handleAddCart(product);
                    btn.setText("✓");
                    btn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    btn.setText("+");
                    btn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
                    setGraphic(btn);
                }
            }
        });

        loadData();
    }

    public void setUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getUsername());
    }

    private void loadData() {
        ObservableList<Product> products = FXCollections.observableArrayList(productDAO.getAllProducts());
        productTable.setItems(products);
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText();
        ObservableList<Product> searchResults = FXCollections.observableArrayList(productDAO.searchProducts(keyword));
        productTable.setItems(searchResults);
    }

    public void handleAddCart(Product product){
        models.CartItem item = new models.CartItem(product);
        UserSession.getInstance().addToCart(item);
        System.out.println("Added to cart: " + product.getName());
    }

    @FXML private void showProducts() { loadData(); }

    @FXML private void logout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/hello-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML private void setToBuildsScreen (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/user-builds-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML private void setToCartScreen (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/cart-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML private void setToOrdersScreen (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/my-orders-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }
}