# FR14 — TDD and regression-test evidence

## Real Red → Green sequence (screen captures)

| Feature | Observed Red | Observed Green | Code covered |
|---|---|---|---|
| Search | `LivestockServiceTest.searchMatchesSpeciesIgnoringCase`: expected 1 record, received 2; 7 tests/1 failure | 7/7 passed | `searchLivestock` normalizes the input and filters species or identifier |
| Delete | `deletesLivestockById`: expected true, received false; 9 tests/1 failure | 9/9 passed | `deleteLivestock(id)` checks ID; DAO deletion returns affected-row success |
| Update | 11 tests/2 failures when stub returned persistence error | 11/11 passed | `updateLivestock` validates ID, required fields, date and persists changes |

Screenshots: `evidence/search-red.png`, `search-green.png`, `delete-red.png`, `delete-green.png`, `update-red.png`, `update-green.png`.

**Historical accuracy:** failing and passing **terminal runs** were captured in the actual development sequence; the feature commits themselves combined some test and production code. Do **not** claim a separate failing-test commit was pushed before implementation for each step.

## Verified automated suite

GitHub Actions pull-request run ID: `39735297593`; job ID: `119679458298`. The log includes:

```text
LivestockPersistenceTest: 4 tests, 0 failures, 0 errors, 0 skipped
LivestockServiceTest: 11 tests, 0 failures, 0 errors, 0 skipped
LivestockValidatorTest: 9 tests, 0 failures, 0 errors, 0 skipped
TOTAL: 80 tests, 0 failures, 0 errors, 0 skipped
BUILD SUCCESS
```

See also `src/test/java/com/example/farmbook/LivestockPersistenceTest.java` and `LivestockServiceTest.java`.

**Persistence tests:** `updatedLivestockPersistsInSQLite()` saves a record, updates species/date and reloads it through a new DAO, retaining the same ID. `deletedLivestockDoesNotReappear()` saves a record, deletes by ID and loads through a new DAO to assert the record is absent. Unique `UUID` identifiers are used; a cleanup helper removes test data.

## Test-quality caveat / next improvement

The test DAO uses `DatabaseConnection.connect()` pointing to `jdbc:sqlite:farmbook.db`; it is **not** an isolated SQLite database. Tests are written to remove their own test records but could affect a developer's database in abnormal failures. Keep a backup and consider a test-only connection/factory in future. No invented coverage percentage: Jacoco/line coverage has not been established.

## Final reproducible commands (PowerShell, Java 21)

```powershell
.\mvnw.cmd -Dtest=LivestockServiceTest test
.\mvnw.cmd -Dtest=LivestockPersistenceTest test
.\mvnw.cmd clean test
```

Capture screenshot of the results in IntelliJ and **reference the exact source files** in the walkthrough.
