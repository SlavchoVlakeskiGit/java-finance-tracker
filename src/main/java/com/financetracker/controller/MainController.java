package com.financetracker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label titleLabel;

    @FXML
    public void initialize() {
        titleLabel.setText("Finance Dashboard");
    }
}