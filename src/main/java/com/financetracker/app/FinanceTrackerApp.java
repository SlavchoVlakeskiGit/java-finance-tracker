package com.financetracker.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FinanceTrackerApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/main-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 1100, 700);
        scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());

        stage.setTitle("Java Finance Tracker");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}