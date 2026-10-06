# Continuous Integration Evidence

## Overview

For Week 11, I introduced an automated Continuous Integration workflow for the FarmBook project using GitHub Actions.

The goal was to ensure that every relevant code change is automatically built and tested before integration into `main`.

---

## Build System

FarmBook uses Maven as its build system.

The repository includes:

- `pom.xml`
- `mvnw`
- `mvnw.cmd`

The local build can be executed with:

```text
.\mvnw.cmd clean test
```

Before introducing CI, I verified the project locally.

Initial local result:

- 55 automated tests
- 0 failures
- 0 errors
- 0 skipped
- BUILD SUCCESS

---

## GitHub Actions Workflow

I added the workflow:

```text
.github/workflows/maven-ci.yml
```

The workflow automatically:

1. checks out the repository;
2. runs on an Ubuntu 24.04 GitHub-hosted runner;
3. configures Java 21;
4. restores Maven dependency caching;
5. builds the project;
6. executes the complete JUnit test suite.

---

## Workflow Triggers

The workflow runs automatically on:

- repository pushes;
- pull requests targeting `main`;
- manual workflow dispatch.

This means changes are tested both while being developed and before they are merged into the main branch.

---

## CI Flow

```text
Developer change
      |
      v
Git push / Pull Request
      |
      v
GitHub Actions
      |
      v
Set up Java 21
      |
      v
Maven clean test
      |
      v
Compile source code
      |
      v
Compile tests
      |
      v
Run JUnit test suite
      |
      v
PASS or FAIL
```

---

## Pull Request #30

PR #30 introduced the automated Maven Continuous Integration workflow.

Title:

`Week 11: Add automated Maven continuous integration`

The pull request included two main commits:

- `ci: add automated Maven build and test workflow`
- `ci: update GitHub Actions dependencies and pin runner`

Before PR #30 was merged, GitHub reported:

- FarmBook CI / Build and Test (push) - passed
- FarmBook CI / Build and Test (pull_request) - passed
- no conflicts with the base branch

---

## Automated Test Verification

The GitHub Actions build executed the same Maven regression suite used locally.

Initial CI result:

- Tests run: 55
- Failures: 0
- Errors: 0
- Skipped: 0
- BUILD SUCCESS

After the later Livestock refactoring work, the CI pipeline continued to validate the expanded test suite.

Final result:

- Tests run: 60
- Failures: 0
- Errors: 0
- Skipped: 0
- BUILD SUCCESS

---

## Why This Improves the Project

The CI workflow provides an automated safety net for the team.

It helps detect:

- compilation failures;
- failing unit tests;
- regressions introduced by new changes;
- integration problems before code is merged.

This was particularly useful during the Livestock architecture refactoring because each push and pull request could be automatically checked against the complete regression suite.

---

## Week 11 Criteria Addressed

This work provides evidence for:

- Build Script
- Automated Build Server
- Continuous Integration
- Automated Testing
- Version Control Workflow
- Regression Testing

The CI workflow remains part of the repository and continues to run automatically for future changes.