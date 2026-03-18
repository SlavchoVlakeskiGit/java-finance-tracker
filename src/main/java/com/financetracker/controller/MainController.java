package com.financetracker.controller;

import com.financetracker.model.Transaction;
import com.financetracker.service.DatabaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label incomeLabel;

    @FXML
    private Label expenseLabel;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField amountField;

    @FXML
    private TextField dateField;

    @FXML
    private TextField descriptionField;

    @FXML
    private PieChart expensePieChart;

    @FXML
    private BarChart<String, Number> summaryBarChart;

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

        typeComboBox.setItems(FXCollections.observableArrayList("Income", "Expense"));
        typeComboBox.setValue("Expense");

        filterComboBox.setItems(FXCollections.observableArrayList("All", "Income", "Expense"));
        filterComboBox.setValue("All");
        filterComboBox.setOnAction(event -> refreshTransactions());

        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        refreshTransactions();
    }

    @FXML
    private void handleAddTransaction() {
        String type = typeComboBox.getValue();
        String category = categoryField.getText().trim();
        String amountText = amountField.getText().trim();
        String date = dateField.getText().trim();
        String description = descriptionField.getText().trim();

        if (category.isEmpty() || amountText.isEmpty() || date.isEmpty()) {
            showAlert("Validation Error", "Category, amount, and date are required.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Amount must be a valid number.");
            return;
        }

        databaseService.insertTransaction(type, category, amount, date, description);
        clearForm();
        refreshTransactions();
    }

    @FXML
    private void handleDeleteTransaction() {
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();

        if (selectedTransaction == null) {
            showAlert("Selection Required", "Please select a transaction to delete.");
            return;
        }

        databaseService.deleteTransaction(selectedTransaction.getId());
        refreshTransactions();
    }

    @FXML
    private void handleExportCsv() {
        databaseService.exportTransactionsToCsv(transactions, "data/transactions_export.csv");
        showInfo("Export Complete", "Transactions exported to data/transactions_export.csv");
    }

    private void refreshTransactions() {
        String filter = filterComboBox.getValue();
        List<Transaction> results;

        if (filter == null || "All".equalsIgnoreCase(filter)) {
            results = databaseService.getAllTransactions();
        } else {
            results = databaseService.getTransactionsByType(filter);
        }

        transactions.setAll(results);
        transactionTable.setItems(transactions);
        updateSummary();
        updateCharts();
    }

    private void updateSummary() {
        double income = 0;
        double expense = 0;

        for (Transaction transaction : transactions) {
            if ("Income".equalsIgnoreCase(transaction.getType())) {
                income += transaction.getAmount();
            } else {
                expense += transaction.getAmount();
            }
        }

        double balance = income - expense;

        balanceLabel.setText(String.format("€ %.2f", balance));
        incomeLabel.setText(String.format("€ %.2f", income));
        expenseLabel.setText(String.format("€ %.2f", expense));
    }

    private void updateCharts() {
        Map<String, Double> expenseByCategory = new HashMap<>();
        double income = 0;
        double expense = 0;

        for (Transaction transaction : transactions) {
            if ("Expense".equalsIgnoreCase(transaction.getType())) {
                expense += transaction.getAmount();
                expenseByCategory.merge(transaction.getCategory(), transaction.getAmount(), Double::sum);
            } else {
                income += transaction.getAmount();
            }
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (Map.Entry<String, Double> entry : expenseByCategory.entrySet()) {
            pieData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        expensePieChart.setData(pieData);

        summaryBarChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Overview");
        series.getData().add(new XYChart.Data<>("Income", income));
        series.getData().add(new XYChart.Data<>("Expense", expense));
        summaryBarChart.getData().add(series);
        summaryBarChart.setLegendVisible(false);
    }

    private void clearForm() {
        typeComboBox.setValue("Expense");
        categoryField.clear();
        amountField.clear();
        dateField.clear();
        descriptionField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}