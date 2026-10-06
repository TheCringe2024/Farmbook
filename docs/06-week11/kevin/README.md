# Week 11 Checkpoint Evidence - Kevin Duy

## Overview

My Week 11 contribution focused on Continuous Integration, Test-Driven Development, refactoring, and object-oriented design improvements for the FarmBook project.

The main goals were to:

1. Introduce an automated Continuous Integration pipeline.
2. Automatically build and test the project on pushes and pull requests.
3. Refactor the Livestock feature to improve separation of concerns.
4. Introduce a service layer and DAO abstraction.
5. Demonstrate Red-Green-Refactor Test-Driven Development.
6. Preserve existing behaviour through regression testing.

---

## Continuous Integration

I introduced the FarmBook GitHub Actions workflow in PR #30.

The CI pipeline automatically:

- checks out the repository;
- configures Java 21;
- builds the Maven project;
- runs the complete JUnit test suite;
- executes on pushes;
- executes on pull requests targeting `main`.

Initial CI verification:

- 55 tests
- 0 failures
- 0 errors
- 0 skipped
- BUILD SUCCESS

PR #30:  
`Week 11: Add automated Maven continuous integration`

---

## Livestock Architecture Refactoring

I refactored the Livestock feature in PR #31.

### Before

JavaFX controllers directly depended on the concrete `LivestockDAO`.

This meant UI controllers were responsible for multiple concerns including:

- validation;
- model creation;
- persistence coordination;
- UI feedback.

### After

The architecture was changed to:

```text
JavaFX Controllers
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

This separates UI, business logic, and persistence responsibilities.

---

## Object-Oriented Design

The refactoring demonstrates:

- abstraction through `ILivestockDAO`;
- polymorphism through multiple `ILivestockDAO` implementations;
- separation of concerns;
- reduced coupling;
- dependency injection;
- DAO Pattern;
- Service Layer Pattern.

---

## Test-Driven Development

The Livestock service was developed using Red-Green-Refactor.

### Red

New service tests were written before the required implementation was complete.

Result:

- 4 tests executed
- 3 failures
- 0 errors
- BUILD FAILURE

### Green

The minimum service implementation was added.

The full project regression suite increased from 55 to 59 passing tests.

### Refactor

The JavaFX controllers were refactored to use the new service layer.

A further service test was added for livestock retrieval.

Final result:

- 60 automated tests
- 0 failures
- 0 errors
- 0 skipped
- BUILD SUCCESS

---

## Version Control Evidence

Key commits:

- `c5aaad4` - refactor: introduce livestock DAO abstraction
- `cb347d8` - test: add TDD livestock service validation
- `336cad7` - refactor: delegate livestock save logic to service layer
- `93f01b0` - refactor: route livestock list through service layer

Key pull requests:

- PR #30 - Automated Maven Continuous Integration
- PR #31 - Livestock architecture refactoring

Both pull requests passed the automated FarmBook CI checks before integration into `main`.

---

## Final Verification

After PR #31 was merged into `main`:

- 60 tests executed
- 0 failures
- 0 errors
- 0 skipped
- BUILD SUCCESS

This confirms that the architecture was refactored without breaking the existing regression suite.