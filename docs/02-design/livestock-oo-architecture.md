# Livestock Object-Oriented Architecture

## Requirement Context

The current FR07 prototype supports the individual livestock-recording workflow.

The persistence layer also contributes to FR13 by storing livestock records in SQLite so they remain available across application sessions.

## Architecture

JavaFX FXML Views
        |
        v
JavaFX Controllers
- HomePageController
- LivestockListController
- AddLivestockController
        |
        +----------------------+
        |                      |
        v                      v
LivestockValidator       Livestock Model
        |                      |
        +-----------+----------+
                    |
                    v
               LivestockDAO
                    |
                    v
            DatabaseConnection
                    |
                    v
              SQLite Database

## Design Rationale

- `Livestock` encapsulates the livestock record data.
- `LivestockValidator` keeps validation rules separate from the JavaFX UI.
- Controllers coordinate user workflows rather than containing SQL.
- `LivestockDAO` isolates persistence operations using the DAO pattern.
- `DatabaseConnection` centralises SQLite connectivity and table creation.
