package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.List;
import java.util.function.Consumer;
import components.Product;

public class SelectionController {

    @FXML private TableView<Product> selectionTable;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;
    @FXML private TextField searchField;

    private Consumer<Product> onItemSelected;

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        selectionTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && selectionTable.getSelectionModel().getSelectedItem() != null) {
                confirmSelection();
            }
        });
    }

    public void setHeader(String title) {
    }

    public void initData(List<? extends Product> items, Consumer<Product> callback) {
        this.selectionTable.setItems(FXCollections.observableArrayList((List<Product>) items));
        this.onItemSelected = callback;
    }

    @FXML
    private void confirmSelection() {
        Product selected = selectionTable.getSelectionModel().getSelectedItem();

        if (selected != null && onItemSelected != null) {
            onItemSelected.accept(selected);
            closeWindow();
        }
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) selectionTable.getScene().getWindow();
        stage.close();
    }
}
