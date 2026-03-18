package com.financetracker.service;

import com.financetracker.model.Transaction;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private static final String DB_URL = "jdbc:sqlite:data/finance_tracker.db";

    public void initializeDatabase() {
        String sql = """
                CREATE TABLE IF NOT EXISTS transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    type TEXT NOT NULL,
                    category TEXT NOT NULL,
                    amount REAL NOT NULL,
                    date TEXT NOT NULL,
                    description TEXT
                )
                """;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT id, type, category, amount, date, description FROM transactions ORDER BY date DESC, id DESC";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getString("type"),
                        resultSet.getString("category"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("date"),
                        resultSet.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public List<Transaction> getTransactionsByType(String type) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT id, type, category, amount, date, description FROM transactions WHERE type = ? ORDER BY date DESC, id DESC";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, type);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(new Transaction(
                            resultSet.getInt("id"),
                            resultSet.getString("type"),
                            resultSet.getString("category"),
                            resultSet.getDouble("amount"),
                            resultSet.getString("date"),
                            resultSet.getString("description")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public void insertTransaction(String type, String category, double amount, String date, String description) {
        String sql = """
                INSERT INTO transactions (type, category, amount, date, description)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, category);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, date);
            preparedStatement.setString(5, description);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void exportTransactionsToCsv(List<Transaction> transactions, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("id,type,category,amount,date,description");

            for (Transaction transaction : transactions) {
                writer.printf(
                        "%d,%s,%s,%.2f,%s,%s%n",
                        transaction.getId(),
                        safeCsv(transaction.getType()),
                        safeCsv(transaction.getCategory()),
                        transaction.getAmount(),
                        safeCsv(transaction.getDate()),
                        safeCsv(transaction.getDescription())
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String safeCsv(String value) {
        if (value == null) {
            return "";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    public void insertSampleTransactionsIfEmpty() {
        String countSql = "SELECT COUNT(*) FROM transactions";
        String insertSql = """
                INSERT INTO transactions (type, category, amount, date, description)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement countStatement = connection.createStatement();
             ResultSet resultSet = countStatement.executeQuery(countSql)) {

            if (resultSet.next() && resultSet.getInt(1) == 0) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                    addSample(preparedStatement, "Income", "Salary", 2400.00, "2026-03-01", "Monthly salary");
                    addSample(preparedStatement, "Expense", "Rent", 850.00, "2026-03-03", "Apartment rent");
                    addSample(preparedStatement, "Expense", "Groceries", 145.50, "2026-03-05", "Weekly groceries");
                    addSample(preparedStatement, "Expense", "Transport", 60.00, "2026-03-06", "Fuel and parking");
                    addSample(preparedStatement, "Income", "Freelance", 320.00, "2026-03-10", "Small freelance task");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addSample(PreparedStatement preparedStatement, String type, String category,
                           double amount, String date, String description) throws SQLException {
        preparedStatement.setString(1, type);
        preparedStatement.setString(2, category);
        preparedStatement.setDouble(3, amount);
        preparedStatement.setString(4, date);
        preparedStatement.setString(5, description);
        preparedStatement.executeUpdate();
    }
}