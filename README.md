# Java Finance Tracker

A desktop finance tracking application built with JavaFX, SQLite, and Maven.

## Features

- add income and expense transactions
- store transactions in a local SQLite database
- view transactions in a table
- display summary cards for current balance, total income, and total expenses
- filter transactions by type
- delete selected transactions
- export transactions to CSV
- visualize expense breakdown by category and income vs expense summary

## Tech stack

- Java 17
- JavaFX
- Maven
- SQLite
- JDBC

## How it works

1. the application starts and initializes the database
2. sample transactions are inserted if the database is empty
3. transactions are loaded into the table view
4. the dashboard calculates totals
5. charts update automatically
6. users can add, filter, delete, and export transactions

## Running the application

### Option 1 — Using Maven

```bash
mvn javafx:run
```

### Option 2 — Using Maven Wrapper

Windows:

```bash
mvnw.cmd javafx:run
```

Linux / macOS:

```bash
./mvnw javafx:run
```

## Export output

The CSV export is saved to:

```text
data/transactions_export.csv
```

## Possible next improvements

- monthly budget targets
- recurring transactions
- better filtering by date and category
- CSV import
