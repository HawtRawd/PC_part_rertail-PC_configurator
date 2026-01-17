package controllers;

import components.UserSession;
import db.UserDAO;
import components.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField address;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button actionButton;
    @FXML private Label statusLabel;
    @FXML private Label usText;
    @FXML private Label conpText;
    @FXML private Label adText;

    private boolean isLoginMode = true;
    private UserDAO userDAO = new UserDAO();
    @FXML
    protected void toggleMode() {
        isLoginMode = !isLoginMode;
        statusLabel.setText("");

        if (isLoginMode) {
            actionButton.setText("Log In");
            setVisibility(usernameField, false);
            setVisibility(usText, false);
            setVisibility(conpText, false);
            setVisibility(confirmPasswordField, false);
            setVisibility(address, false);
            setVisibility(adText, false);
        } else {
            actionButton.setText("Sign Up");
            setVisibility(usernameField, true);
            setVisibility(usText, true);
            setVisibility(conpText, true);
            setVisibility(confirmPasswordField, true);
            setVisibility(address, true);
            setVisibility(adText, true);
        }
    }

    private void setVisibility(Control control, boolean isVisible) {
        control.setVisible(isVisible);
        control.setManaged(isVisible);
    }

    @FXML
    protected void onActionButtonClick(ActionEvent event) {
        String email = emailField.getText();
        String pass = passwordField.getText();

        if (isLoginMode) {
            User loggedInUser = userDAO.login(email, pass);

            if (loggedInUser != null) {
                System.out.println("Login Success");
                UserSession.getInstance().setCurrentUser(loggedInUser);
                openDashboard(event, loggedInUser);
            } else {
                statusLabel.setText("Invalid email or password.");
            }

        } else {
            String user = usernameField.getText();
            String confirm = confirmPasswordField.getText();
            String add = address.getText();

            if (!pass.equals(confirm)) {
                statusLabel.setText("Passwords do not match!");
                return;
            }
            if (email.isEmpty()) {
                statusLabel.setText("Email is required.");
                return;
            }

            boolean success = userDAO.registerUser(user, email, pass, add);

            if (success) {
                statusLabel.setText("Account created! Please Log In.");
                toggleMode();
            } else {
                statusLabel.setText("Registration failed. Username/Email may be taken.");
            }
        }
    }

    private void openDashboard(ActionEvent event, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/dashboard.fxml"));
            Scene dashboardScene = new Scene(loader.load());

            DashboardController controller = loader.getController();
            controller.setUser(user);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dashboardScene);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}