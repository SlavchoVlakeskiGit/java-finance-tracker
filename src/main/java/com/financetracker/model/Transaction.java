package com.financetracker.model;

import javafx.beans.property.*;

public class Transaction {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final StringProperty category = new SimpleStringProperty();
    private final DoubleProperty amount = new SimpleDoubleProperty();
    private final StringProperty date = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();

    public Transaction() {
    }

    public Transaction(int id, String type, String category, double amount, String date, String description) {
        setId(id);
        setType(type);
        setCategory(category);
        setAmount(amount);
        setDate(date);
        setDescription(description);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public StringProperty typeProperty() {
        return type;
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public StringProperty dateProperty() {
        return date;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}