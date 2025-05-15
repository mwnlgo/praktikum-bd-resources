package com.example.bdsqltester;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static HelloApplication applicationInstance;
    private Stage primaryStage;
    public static HelloApplication getApplicationInstance () { return applicationInstance; }
    public Stage getPrimaryStage () { return primaryStage; }

    @Override
    public void start(Stage stage) throws IOException {
        HelloApplication.applicationInstance = this;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage = stage;
        stage.setTitle("Grading System");
        stage.setOnCloseRequest(event -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);
            Alert exitAlert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to exit the program?",
                    yes, no
            );
            exitAlert.setTitle("Confirm Exit");
            exitAlert.setHeaderText(null);
            exitAlert.showAndWait().ifPresent(response -> {
                if (response != yes) {
                    event.consume();
                }
            });
        });
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}