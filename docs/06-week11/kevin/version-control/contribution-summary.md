# Week 11 Version Control and Contribution Summary

## Overview

This document summarises my individual Week 11 contribution to the FarmBook project.

My work focused on two main areas:

1. Continuous Integration
2. Livestock architecture refactoring using TDD and object-oriented design

All major changes were completed using separate feature branches, commits, pull requests, automated CI checks, and merge validation.

---

## Continuous Integration Branch

Branch:

```text
ci/week11-automated-build
```

Purpose:

- introduce an automated build server;
- automatically build the Maven project;
- run the JUnit test suite on pushes;
- run the JUnit test suite on pull requests targeting `main`.

---

## Continuous Integration Commits

Key commits:

```text
ed9c5b3 ci: add automated Maven build and test workflow
b87f26a ci: update GitHub Actions dependencies and pin runner
```

These commits introduced and refined:

```text
.github/workflows/maven-ci.yml
```

The workflow uses:

- GitHub Actions;
- Ubuntu 24.04;
- Java 21;
- Maven;
- automated JUnit testing.

---

## Pull Request #30

Title:

```text
Week 11: Add automated Maven continuous integration
```

PR #30 introduced the Continuous Integration workflow.

Before integration into `main`, GitHub reported:

```text
FarmBook CI / Build and Test (push)         PASSED
FarmBook CI / Build and Test (pull_request) PASSED
```

There were no merge conflicts.

The workflow initially verified:

```text
Tests run: 55
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

PR #30 was then merged into `main`.

Merge commit:

```text
20683eb Merge pull request #30 from TheCringe2024/ci/week11-automated-build
```

---

## Livestock Refactoring Branch

Branch:

```text
refactor/week11-livestock-architecture
```

Purpose:

- improve the Livestock OO design;
- introduce a DAO abstraction;
- introduce a Service Layer;
- reduce controller responsibilities;
- improve testability;
- demonstrate Red-Green-Refactor TDD.

---

## Livestock Refactoring Commits

The refactoring was divided into several focused commits.

### DAO Abstraction

```text
c5aaad4 refactor: introduce livestock DAO abstraction
```

Changes included:

- introduced `ILivestockDAO`;
- updated `LivestockDAO` to implement the interface;
- created an abstraction between business logic and persistence.

---

### TDD Service Validation

```text
cb347d8 test: add TDD livestock service validation
```

Changes included:

- introduced `LivestockService`;
- introduced service-level tests;
- used a fake `ILivestockDAO` implementation for isolated testing;
- demonstrated the Red and Green stages of TDD.

---

### Add Livestock Controller Refactoring

```text
336cad7 refactor: delegate livestock save logic to service layer
```

Changes included:

- removed livestock business logic from `AddLivestockController`;
- delegated validation and persistence coordination to `LivestockService`;
- reduced controller responsibilities.

---

### Livestock List Controller Refactoring

```text
93f01b0 refactor: route livestock list through service layer
```

Changes included:

- routed livestock retrieval through `LivestockService`;
- removed direct DAO access from `LivestockListController`;
- added a retrieval test;
- completed the layered Livestock architecture.

---

## Pull Request #31

Title:

```text
Week 11: Refactor Livestock architecture using service layer and DAO abstraction
```

PR #31 contained the four focused refactoring commits.

Before merge, GitHub reported:

```text
FarmBook CI / Build and Test (push)         PASSED
FarmBook CI / Build and Test (pull_request) PASSED
```

There were no merge conflicts.

The final regression suite contained:

```text
Tests run: 60
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

PR #31 was then merged into `main`.

Merge commit:

```text
ce58a66 Merge pull request #31 from TheCringe2024/refactor/week11-livestock-architecture
```

---

## Test Progress

The automated test suite progressed during my Week 11 work as follows:

```text
Before Week 11 refactoring
55 tests passing
        |
        v
Livestock service tests introduced
59 tests passing
        |
        v
Livestock retrieval test introduced
60 tests passing
```

Final status:

```text
60 tests
0 failures
0 errors
0 skipped
BUILD SUCCESS
```

---

## Branch and Integration Workflow

The workflow used for my Week 11 contribution was:

```text
main
 |
 +--> ci/week11-automated-build
 |          |
 |          +--> commits
 |          |
 |          +--> push
 |          |
 |          +--> GitHub CI
 |          |
 |          +--> PR #30
 |          |
 |          +--> CI checks
 |          |
 |          +--> merge to main
 |
 +--> refactor/week11-livestock-architecture
            |
            +--> focused commits
            |
            +--> local regression testing
            |
            +--> push
            |
            +--> GitHub CI
            |
            +--> PR #31
            |
            +--> CI checks
            |
            +--> merge to main
```

This prevented direct development on `main` and provided a reviewable history of my individual contribution.

---

## Individual Contribution Summary

My Week 11 contribution included:

### Continuous Integration

- added GitHub Actions CI;
- configured Java 21;
- automated Maven builds;
- automated JUnit test execution;
- configured CI for pushes and pull requests;
- verified CI before merging changes.

### Test-Driven Development

- created Livestock service tests;
- demonstrated Red-Green-Refactor;
- increased the complete regression suite from 55 to 60 tests;
- maintained zero final failures and errors.

### Object-Oriented Refactoring

- introduced `ILivestockDAO`;
- introduced `LivestockService`;
- refactored `AddLivestockController`;
- refactored `LivestockListController`;
- applied DAO Pattern;
- applied Service Layer Pattern;
- improved abstraction and polymorphism;
- reduced coupling;
- improved separation of concerns and testability.

---

## Final Repository State

After PR #30 and PR #31 were merged:

```text
main
 |
 +--> Automated GitHub Actions CI
 |
 +--> Java 21 Maven build
 |
 +--> 60 automated tests
 |
 +--> Livestock Service Layer
 |
 +--> ILivestockDAO abstraction
 |
 +--> LivestockDAO persistence implementation
```

All final regression tests passed successfully.

This provides clear version-control evidence of my individual Week 11 progress.