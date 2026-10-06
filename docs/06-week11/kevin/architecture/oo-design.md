# Object-Oriented Design Evidence

## Overview

The Week 11 Livestock refactoring improved the object-oriented structure of the feature by separating UI, business logic, and persistence responsibilities.

The refactored design demonstrates several object-oriented principles and design patterns.

---

## Abstraction

The main persistence abstraction is:

`ILivestockDAO`

This interface defines the operations required by the Livestock feature without exposing SQLite implementation details.

The service depends on:

```text
ILivestockDAO
```

instead of depending directly on:

```text
LivestockDAO
```

This reduces coupling between the business logic and the concrete persistence implementation.

---

## Polymorphism

Multiple classes can implement the same `ILivestockDAO` interface.

The production implementation is:

```text
LivestockDAO implements ILivestockDAO
```

The test implementation is:

```text
FakeLivestockDAO implements ILivestockDAO
```

Both implementations can be supplied to `LivestockService`.

This means the service can work with different persistence implementations without changing its business logic.

---

## Dependency Injection

`LivestockService` receives its DAO dependency through its constructor.

Conceptually:

```java
public LivestockService(ILivestockDAO livestockDAO) {
    this.livestockDAO = livestockDAO;
}
```

The service does not create its own DAO internally.

This allows different DAO implementations to be supplied when required.

For production:

```text
LivestockService
        |
        v
LivestockDAO
```

For testing:

```text
LivestockService
        |
        v
FakeLivestockDAO
```

This improves testability and reduces coupling.

---

## Separation of Concerns

The refactoring separates responsibilities across different classes.

### JavaFX Controllers

The controllers are responsible for:

- receiving user input;
- invoking service operations;
- updating UI components;
- displaying feedback;
- performing navigation.

### LivestockService

The service is responsible for:

- required-field validation;
- acquisition-date validation;
- creating livestock domain objects;
- coordinating persistence operations;
- retrieving livestock records.

### ILivestockDAO

The interface is responsible for:

- defining the persistence contract used by the service.

### LivestockDAO

The concrete DAO is responsible for:

- interacting with SQLite;
- executing SQL operations;
- saving livestock records;
- retrieving livestock records.

This reduces the number of responsibilities handled by each class.

---

## DAO Pattern

The Livestock feature uses the Data Access Object pattern.

The structure is:

```text
LivestockService
        |
        v
ILivestockDAO
        |
        v
LivestockDAO
        |
        v
SQLite
```

`LivestockDAO` encapsulates persistence and database-access behaviour.

This prevents SQL and SQLite-specific logic from being placed directly inside JavaFX controllers or business logic.

---

## Service Layer Pattern

`LivestockService` acts as a Service Layer between the JavaFX UI and persistence layer.

The structure is:

```text
JavaFX Controllers
        |
        v
LivestockService
        |
        v
Persistence Layer
```

The service provides a clear location for livestock business logic.

This allows the controllers to remain focused on UI behaviour.

---

## Reduced Coupling

Before refactoring:

```text
Controller
    |
    v
Concrete LivestockDAO
    |
    v
SQLite
```

After refactoring:

```text
Controller
    |
    v
LivestockService
    |
    v
ILivestockDAO
    |
    v
LivestockDAO
```

The controller no longer needs to know how livestock data is stored.

The business layer also depends on an interface rather than directly on SQLite-specific persistence.

---

## Improved Testability

Before refactoring, testing livestock business behaviour independently from persistence was difficult.

After introducing `ILivestockDAO`, tests can provide a fake DAO:

```text
LivestockServiceTest
        |
        v
LivestockService
        |
        v
ILivestockDAO
        |
        v
FakeLivestockDAO
```

This allows tests to verify business behaviour without opening a real SQLite database.

---

## Relationship to TDD

The new OO design also supports Test-Driven Development.

The DAO abstraction made it possible to test the service in isolation during the Red-Green-Refactor process.

This demonstrates how object-oriented design and automated testing support each other.

---

## Final Architecture

```text
AddLivestockController --------\
                                \
                                 > LivestockService
                                /         |
LivestockListController -------/          v
                                    ILivestockDAO
                                          |
                                          v
                                    LivestockDAO
                                          |
                                          v
                                        SQLite
```

Testing uses:

```text
LivestockServiceTest
        |
        v
LivestockService
        |
        v
ILivestockDAO
        |
        v
FakeLivestockDAO
```

---

## OO Improvements Demonstrated

The final design demonstrates:

- abstraction;
- polymorphism;
- dependency injection;
- separation of concerns;
- reduced coupling;
- improved testability;
- DAO Pattern;
- Service Layer Pattern.

These improvements were introduced through refactoring while keeping the full automated regression suite passing.