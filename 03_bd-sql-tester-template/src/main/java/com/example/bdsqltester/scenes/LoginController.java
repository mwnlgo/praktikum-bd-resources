package com.example.bdsqltester.scenes;

import com.example.bdsqltester.HelloApplication;
import com.example.bdsqltester.datasources.MainDataSource;
import com.example.bdsqltester.scenes.admin.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.sql.*;

public class LoginController {

    @FXML
    private TextField passwordField;

    @FXML
    private ChoiceBox<String> selectRole;

    @FXML
    private TextField usernameField;

    @FXML
    private Label welcomeLabel;

    private int getUserIdByUsername(String username) throws SQLException {
        try (Connection c = MainDataSource.getConnection()) {
            PreparedStatement stmt = c.prepareStatement("SELECT id FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    private boolean verifyCredentials(String username, String password, String role) throws SQLException {
        try (Connection c = MainDataSource.getConnection()) {
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM users WHERE username = ? AND role = ?");
            stmt.setString(1, username);
            stmt.setString(2, role.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String dbPassword = rs.getString("password");
                if (dbPassword.equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    @FXML
    private void initialize() {
        selectRole.getItems().addAll("Admin", "User");
        selectRole.setValue("User");
    }

    @FXML
    private void onLoginClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = selectRole.getValue();
        try {
            if (verifyCredentials(username, password, role)) {
                HelloApplication app = HelloApplication.getApplicationInstance();
                if (role.equals("Admin")) {
                    app.getPrimaryStage().setTitle("Admin View");
                    app.getPrimaryStage().setOnCloseRequest(e -> {
                        try {
                            HelloApplication.getApplicationInstance().start(new Stage());
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                    });
                    FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("admin-view.fxml"));
                    Scene scene = new Scene(loader.load());
                    app.getPrimaryStage().setScene(scene);
                } else {
                    int userId = getUserIdByUsername(username);
                    if (userId == -1) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login Error");
                        alert.setHeaderText("User Not Found");
                        alert.setContentText("Cannot find user ID in database.");
                        alert.showAndWait();
                        return;
                    }
                    app.getPrimaryStage().setTitle("User View");
                    app.getPrimaryStage().setOnCloseRequest(e -> {
                        try {
                            HelloApplication.getApplicationInstance().start(new Stage());
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                    });
                    try {
                        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("user-view.fxml"));
                        Parent root = loader.load();
                        UserController userController = loader.getController();
                        userController.setUserId(userId);
                        Scene scene = new Scene(root);
                        app.getPrimaryStage().setScene(scene);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // Show an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText("Invalid Credentials");
                alert.setContentText("Please check your username and password.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Database Connection Failed");
            alert.setContentText("Could not connect to the database. Please try again later.");
            alert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reset() {
        welcomeLabel.setText("Login Page");
    }
}