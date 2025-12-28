import components.Product;
import db.products.User;
import db.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

    @FXML private Button buildButton;

    private User currentUser;
    private ProductDAO productDAO = new ProductDAO();

    @FXML
    public void initialize() {
        colManufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturerName"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

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

    @FXML
    private void showProducts() {
        loadData();
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    private void setToBuildScreen (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("build-dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }
}