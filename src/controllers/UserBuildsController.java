package controllers;

import components.Build;
import components.UserSession;
import db.BuildDAO;
import db.OrderDAO;
import components.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class UserBuildsController {

    @FXML private VBox buildsContainer;
    @FXML private Button createNewBtn;

    private BuildDAO buildDAO = new BuildDAO();

    @FXML
    public void initialize() {
        loadBuilds();
    }

    private HBox createBuildCard(Build build) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-padding: 15; -fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        card.setMaxWidth(800);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);

        String imageUrl = null;
        if (build.getPcCase() != null) {
            imageUrl = build.getPcCase().getImageUrl();
        }

        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                Image image = new Image(imageUrl, true);
                imageView.setImage(image);
                image.errorProperty().addListener((obs, oldErr, newErr) -> {
                    if (newErr) {
                        imageView.setImage(new Image("file:src/resources/images/placeholder.jpg"));
                    }
                });

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Image URL: " + imageUrl);
            }
        } else {
            imageView.setImage(new Image("file:src/resources/images/placeholder.jpg"));
        }
        Label nameLbl = new Label(build.getBuildName());
        nameLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label priceLbl = new Label(String.format("Total: $%.2f", build.calculateTotal()));
        priceLbl.setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 14px;");
        String info = (build.getCpu() != null ? build.getCpu().getName() : "No CPU") + " | " +
                (build.getMobo() != null ? build.getMobo().getName() : "No Mobo");
        Label detailsLbl = new Label(info);
        detailsLbl.setStyle("-fx-text-fill: #666;");

        VBox textContainer = new VBox(5, nameLbl, detailsLbl, priceLbl);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button loadBtn = new Button("Edit / View");
        loadBtn.setOnAction(e -> handleEditBuild(build));

        Button buyBtn = new Button("Add to Cart");
        buyBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        buyBtn.setOnAction(e -> {
            User currentUser = UserSession.getInstance().getCurrentUser();
            if (currentUser.getAddress() == null || currentUser.getAddress().isEmpty()) {
                System.out.println("Current address is: " + currentUser.getAddress() + ". Please update your profile with an address first!");
                return;
            }
            CartItem item = new CartItem(build);
            UserSession.getInstance().addToCart(item);
            System.out.println("Added build to cart!");
        });

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> handleDeleteBuild(build));

        VBox buttonsBox = new VBox(8);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);
        buttonsBox.getChildren().addAll(loadBtn, buyBtn, deleteBtn);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(imageView, textContainer, spacer, buttonsBox);
        return card;
    }

    private void loadBuilds() {
        int userId = UserSession.getInstance().getCurrentUser().getId();
        List<Build> myBuilds = buildDAO.getAllBuildsForUser(userId);
        buildsContainer.getChildren().clear();
        for (Build build : myBuilds) {
            HBox card = createBuildCard(build);
            buildsContainer.getChildren().add(card);
        }
        buildsContainer.getChildren().add(createNewBtn);
    }


    private void handleEditBuild(Build build) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/build-dashboard.fxml"));
            Parent root = loader.load();
            BuildController controller = loader.getController();
            controller.loadExistingBuild(build);
            Stage stage = (Stage) buildsContainer.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteBuild(Build build) {
        buildDAO.deleteBuild(build.getId());
        loadBuilds();
    }

    public void handleBack(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/dashboard.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    public void handleCreateNew(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/build-dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

}