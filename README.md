# Java Finance Tracker

A desktop finance tracking application built with JavaFX, SQLite, and Maven.

I built this because I wanted one Java project that felt a bit more complete than a typical console app.

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

## Why I built it

A lot of smaller Java projects stop at console output. I wanted this one to feel like something a person could actually open and use.

One detail I liked here was the local database setup, because it made the app feel more grounded than a UI that only works with in-memory sample data.

Getting the database and UI to stay in sync cleanly took a bit more effort than I expected.

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

## Notes

I kept the scope relatively small on purpose. The goal was not to build a full personal finance suite, just a clean desktop app with a solid core workflow.

## Possible next improvements

- monthly budget targets
- recurring transactions
- better filtering by date and category
- CSV import
