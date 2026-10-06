# Livestock Architecture - Before Refactoring

## Overview

Before the Week 11 refactoring, the Livestock feature used a more tightly coupled structure.

Both JavaFX controllers depended directly on the concrete `LivestockDAO` implementation.

---

## Previous Structure

```text
AddLivestockController --------\
                                \
                                 > LivestockDAO
                                /       |
LivestockListController -------/        v
                                      SQLite
```

---

## Direct Dependency on the DAO

The controllers created and used the concrete DAO directly.

Conceptually, the dependency looked like:

```text
Controller
    |
    v
LivestockDAO
    |
    v
SQLite
```

This meant the UI layer was directly aware of the persistence implementation.

---

## Multiple Responsibilities in the Controller

Before the refactoring, `AddLivestockController` handled several responsibilities.

These included:

- reading values from JavaFX controls;
- checking required fields;
- validating the acquisition date;
- creating a `Livestock` object;
- calling the persistence layer;
- interpreting the persistence result;
- displaying success or error feedback.

The controller therefore contained both UI responsibilities and business-related behaviour.

---

## High Coupling

The controllers depended directly on `LivestockDAO`.

This created stronger coupling between:

```text
JavaFX UI
   |
   v
SQLite persistence
```

If the persistence implementation changed, the controllers could also require changes.

---

## Reduced Testability

Business behaviour was difficult to test independently because the controllers were connected directly to the concrete DAO.

A test could not easily replace `LivestockDAO` with a lightweight fake implementation.

This made it harder to isolate:

- validation behaviour;
- save behaviour;
- persistence outcomes;
- livestock retrieval behaviour.

---

## Refactoring Motivation

The Week 11 refactoring aimed to separate these concerns.

The main goals were to:

1. move livestock business logic out of the JavaFX controllers;
2. introduce an abstraction for persistence;
3. reduce direct dependency on the concrete DAO;
4. make the business logic easier to test;
5. preserve existing application behaviour.

The resulting architecture is documented in `after.md`.