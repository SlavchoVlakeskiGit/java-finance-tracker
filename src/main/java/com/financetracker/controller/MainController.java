package com.financetracker.controller;

import com.financetracker.model.Transaction;
import com.financetracker.service.DatabaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainController {

    @FXML
    private Label titleLabel;

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, String> typeColumn;

    @FXML
    private TableColumn<Transaction, String> categoryColumn;

    @FXML
    private TableColumn<Transaction, Number> amountColumn;

    @FXML
    private TableColumn<Transaction, String> dateColumn;

    @FXML
    private TableColumn<Transaction, String> descriptionColumn;

    private final DatabaseService databaseService = new DatabaseService();
    private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        titleLabel.setText("Finance Dashboard");

        databaseService.initializeDatabase();
        databaseService.insertSampleTransactionsIfEmpty();

        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        transactions.setAll(databaseService.getAllTransactions());
        transactionTable.setItems(transactions);
    }
}