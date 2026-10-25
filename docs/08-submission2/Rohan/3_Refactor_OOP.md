# Pull Request / Commit Documentation: OOP Refactoring in Crop Module

**Commit / PR Number:** `4105a03`
**Module:** `Crop`

## Overview
This architectural refactoring significantly enhances the maintainability, scalability, and loose coupling of the `Crop` module by applying advanced Object-Oriented Design (OOD) patterns.

## Patterns Applied

### 1. Model-View-Controller (MVC) & Dependency Injection
- **What Changed:** The interaction between `CropController` and `CropDAO`. Instead of a hard-coded internal `new CropDAO()` the controller now supports **Dependency Injection (DI)** via `setCropDAO(ICropDAO dao)`.
- **Benefit:** Decouples the UI logic from the persistence layer. This makes the controller strictly responsible for mediating, and it allows unit tests to inject mock DAOs without pinging the actual SQLite database.

### 2. Observer Pattern
- **What Changed:** Replaced primitive data types (`String`, `int`) in the `Crop.java` model with JavaFX Properties (`StringProperty`, `IntegerProperty`).
- **Benefit:** Implements the true **Observer Pattern**. `TableView` instances observing these properties will automatically render changes in real-time when the model’s data changes, without requiring rigid programmatic UI refreshes.

### 3. Factory Pattern
- **What Changed:** Introduced a `CropFactory` utility class. Object instantiation (like `new Crop(...)`) was removed from the controller and DAO layers.
- **Benefit:** Adheres to the **Single Responsibility** and **Open/Closed Principles**. If the team decides to add subclasses of `Crop` (e.g., `OrganicCrop`, `GeneticallyModifiedCrop`), we can adapt the Factory to generate these without rewriting instantiation logic across the UI and Data Access layers.

### 4. Singleton Pattern
- **What Changed:** `CropDAO` was converted to a **Singleton**. It now features a private constructor and a thread-safe `getInstance()` method.
- **Benefit:** Prevents accidental instantiation of multiple DAO objects throughout the app. Centralising the DAO enforces a single active channel bridging the application to the `SqliteConnection`, optimising memory management.