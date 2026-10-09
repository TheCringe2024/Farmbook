# Livestock Architecture - After Refactoring

## Overview

After the Week 11 refactoring, the Livestock feature uses a layered structure that separates UI, business logic, and persistence responsibilities.

The JavaFX controllers no longer depend directly on the concrete `LivestockDAO` implementation.

---

## Refactored Structure

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

This structure separates the main responsibilities of the feature.

---

## Controller Responsibility

The JavaFX controllers now focus primarily on UI behaviour.

Their responsibilities include:

- reading values from JavaFX controls;
- calling the service layer;
- displaying success or error messages;
- navigating between views.

They no longer contain the main livestock business logic.

---

## Service Layer

`LivestockService` provides the business logic for the Livestock feature.

Its responsibilities include:

- validating required fields;
- validating acquisition date input;
- creating `Livestock` domain objects;
- coordinating save operations;
- retrieving livestock records;
- translating persistence outcomes into service-level results.

The controllers therefore interact with the service rather than directly with SQLite persistence.

---

## DAO Abstraction

`ILivestockDAO` defines the persistence operations required by the service.

```text
LivestockService
        |
        v
ILivestockDAO
```

The production implementation is:

```text
ILivestockDAO
        |
        v
LivestockDAO
        |
        v
SQLite
```

This allows the service to depend on an abstraction rather than a concrete database implementation.

---

## Test Architecture

The same abstraction can be replaced during testing.

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

This allows service behaviour to be tested without requiring a real SQLite database.

---

## Separation of Concerns

The refactored responsibilities are divided as follows.

### JavaFX Controllers

Responsible for:

- user interaction;
- UI feedback;
- navigation.

### LivestockService

Responsible for:

- business logic;
- validation;
- domain object creation;
- persistence coordination.

### ILivestockDAO

Responsible for:

- defining the persistence contract.

### LivestockDAO

Responsible for:

- SQLite queries;
- database persistence;
- database retrieval.

---

## Benefits of the Refactoring

The new architecture provides:

- lower coupling between UI and persistence;
- improved separation of concerns;
- improved testability;
- clearer responsibilities;
- easier replacement of persistence implementations;
- easier maintenance;
- safer future changes.

---

## Regression Verification

The architecture changed without breaking the existing application regression suite.

Final verification:

- 60 automated tests
- 0 failures
- 0 errors
- 0 skipped
- BUILD SUCCESS

The refactored code was also validated by the GitHub Actions CI workflow before PR #31 was merged into `main`.

---

## Before and After Comparison

### Before

```text
Controllers
    |
    v
LivestockDAO
    |
    v
SQLite
```

### After

```text
Controllers
    |
    v
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

The main improvement is that UI code no longer directly controls business logic and database persistence.