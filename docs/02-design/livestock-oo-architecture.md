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