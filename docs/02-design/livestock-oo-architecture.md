\# Livestock Object-Oriented Architecture



\## Requirement Context



The Livestock feature supports recording and retrieving livestock information including species, identifier and acquisition date.



Persistent storage requirements require livestock records to remain available after the application is closed and reopened.



\## Architecture



```text

JavaFX FXML Views

&#x20;       |

&#x20;       v

JavaFX Controllers

\- HomePageController

\- LivestockListController

\- AddLivestockController

&#x20;       |

&#x20;       +--------------------+

&#x20;       |                    |

&#x20;       v                    v

LivestockValidator      Livestock Model

&#x20;       |                    |

&#x20;       +----------+---------+

&#x20;                  |

&#x20;                  v

&#x20;             LivestockDAO

&#x20;                  |

&#x20;                  v

&#x20;          DatabaseConnection

&#x20;                  |

&#x20;                  v

&#x20;            SQLite Database

```



\## Responsibilities



\### Livestock Model



Path:



`src/main/java/com/example/farmbook/model/Livestock.java`



Represents one livestock record and encapsulates livestock state including ID, species, identifier and acquisition date.



\### LivestockValidator



Path:



`src/main/java/com/example/farmbook/LivestockValidator.java`



Contains validation rules independently of the JavaFX user interface. This separation allows validation behaviour to be unit tested without launching the GUI.



\### AddLivestockController



Path:



`src/main/java/com/example/farmbook/AddLivestockController.java`



Coordinates the Add Livestock workflow. It receives JavaFX input, invokes validation, creates a Livestock model object, delegates persistence to the DAO and updates UI feedback.



\### LivestockListController



Path:



`src/main/java/com/example/farmbook/LivestockListController.java`



Retrieves persisted livestock records through the DAO and presents them through the JavaFX Livestock List screen.



\### LivestockDAO



Path:



`src/main/java/com/example/farmbook/dao/LivestockDAO.java`



Encapsulates SQLite persistence operations for Livestock records. SQL is kept out of the JavaFX controllers.



\### DatabaseConnection



Path:



`src/main/java/com/example/farmbook/dao/DatabaseConnection.java`



Centralises SQLite connections and database-table initialisation.



\## Object-Oriented Principles



\### Encapsulation



Livestock state is stored in private fields and exposed through controlled methods.



\### Separation of Concerns



UI interaction, validation, domain data and persistence are handled by separate classes rather than one large controller.



\### DAO Pattern



Database operations are isolated behind `LivestockDAO`, preventing UI controllers from directly depending on SQL statements.



\### MVC-Like Structure



FXML files act as views, controller classes coordinate interaction, and model classes represent application data. DAO classes provide a separate persistence layer.



\## Design Benefit



This structure improves maintainability and testability because validation and persistence behaviour can be tested independently from JavaFX screens. It also allows the user interface or persistence implementation to evolve without requiring all application logic to be rewritten.



