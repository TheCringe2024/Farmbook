# Test-Driven Development Evidence

## Overview

For Week 11, I applied Test-Driven Development to the Livestock service using the Red-Green-Refactor cycle.

The goal was to define expected behaviour through tests before completing the service implementation, then refactor the architecture while keeping the regression suite passing.

---

## Red

I first created tests for the expected behaviour of `LivestockService`.

The service was intentionally incomplete and returned:

```text
PERSISTENCE_ERROR
```

for every save request.

The test suite then produced:

- Tests run: 4
- Failures: 3
- Errors: 0
- Skipped: 0
- BUILD FAILURE

The failing behaviours included:

- expected `MISSING_FIELDS`, but received `PERSISTENCE_ERROR`;
- expected `INVALID_DATE`, but received `PERSISTENCE_ERROR`;
- expected `SUCCESS`, but received `PERSISTENCE_ERROR`.

This confirmed that the tests were checking behaviour that had not yet been implemented.

---

## Green

I then implemented the minimum required behaviour in `LivestockService`.

The service was updated to:

- detect missing required fields;
- validate the acquisition date;
- create a `Livestock` domain object;
- delegate persistence through `ILivestockDAO`;
- report successful or unsuccessful persistence.

After this implementation, the new service tests passed.

The full project regression suite increased from:

```text
55 tests
```

to:

```text
59 tests
```

Result:

- Tests run: 59
- Failures: 0
- Errors: 0
- Skipped: 0
- BUILD SUCCESS

---

## Refactor

After the service behaviour was working, I refactored the Livestock controllers.

Before refactoring:

```text
AddLivestockController
        |
        v
LivestockDAO
```

and:

```text
LivestockListController
        |
        v
LivestockDAO
```

After refactoring:

```text
AddLivestockController --------\
                                \
                                 > LivestockService
                                /
LivestockListController -------/
                                 |
                                 v
                           ILivestockDAO
                                 |
                                 v
                           LivestockDAO
```

The internal structure changed, but the observable application behaviour was preserved.

---

## Additional Retrieval Test

A further service test was added for retrieving livestock records through the service layer.

This increased the full regression suite to:

```text
60 automated tests
```

Final result:

- Tests run: 60
- Failures: 0
- Errors: 0
- Skipped: 0
- BUILD SUCCESS

---

## Test Isolation

The service tests use a fake implementation of `ILivestockDAO`.

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

This allows the business logic to be tested without requiring a real SQLite database.

The production application uses:

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

Both implementations follow the same DAO interface.

---

## Why This Demonstrates TDD

The process followed the TDD cycle:

```text
RED
Tests define expected behaviour and fail
        |
        v
GREEN
Implement the minimum behaviour required to pass
        |
        v
REFACTOR
Improve the design while keeping all tests passing
```

The final regression result shows that the architecture could be improved without breaking existing functionality.

---

## Final Evidence

The TDD process produced:

- a new `LivestockService`;
- a fake DAO for isolated service testing;
- new service tests;
- increased automated test coverage from 55 to 60 tests;
- zero final test failures;
- successful CI verification after the refactoring.

This provides direct evidence of Red-Green-Refactor development rather than only describing TDD theoretically.