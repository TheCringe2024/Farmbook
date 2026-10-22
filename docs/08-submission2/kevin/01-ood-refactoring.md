# FR07 / FR14 Object-Oriented Design — purposeful incremental refactoring

## Design development timeline

**Earlier Week 11 (PR #31):** Livestock logic was separated out of JavaFX controllers into `LivestockService`, and persistence was abstracted by `ILivestockDAO`. The service accepts an `ILivestockDAO` through constructor injection. See `docs/06-week11/kevin/architecture/before.md`, `after.md`, and `oo-design.md` for the earlier architectural evidence.

**Week 13 (PR #37, five commits):** the existing design was **extended**, not redesigned from scratch, to implement FR14 Search, Edit and Delete. Service validation and DAO changes were tested and wired to the JavaFX list. The key decision was to preserve the abstraction boundary as CRUD behavior expanded.

## Before/after, scoped to FR14

| Before #37 | After #37 | Why it matters |
|---|---|---|
| `LivestockListController` rendered `ListView<String>` from `animal.toString()` | `ListView<Livestock>` renders domain objects | Retains record IDs, allowing correct selection and update/delete instead of matching string text |
| Service exposed only save and getAll | Service adds case-insensitive search, `deleteLivestock(id)` and `updateLivestock(id,...)` with validation outcomes | Controls feature rules separately from UI widgets |
| DAO exposed `void delete(int id)` | `boolean delete(int id)` returns whether exactly one row was affected | UI can distinguish success, missing row, and persistence failure |
| No SQLite update method in `ILivestockDAO` | `boolean update(Livestock)` is defined in interface and implemented by prepared SQL `UPDATE ... WHERE id=?` | New behavior stays behind one persistence contract; preserves entity ID |
| No Edit/Delete dialogs or search input | Search field, Edit form with validations, Delete confirmation | Matches FR14 workflow and reduces risky accidental deletion |

## Patterns and OO principles (accurate attribution)

```text
livestock-list-view.fxml + add-livestock-view.fxml
            |
            v
LivestockListController / AddLivestockController
      |   JavaFX events and user feedback
      v
LivestockService
      |   business validation; SaveResult / UpdateResult
      v
ILivestockDAO     <-- test fake implements this interface
      | production implementation
      v
LivestockDAO -- prepared JDBC statements --> SQLite livestock table
```

- **DAO / Repository-like abstraction** — `ILivestockDAO` defines persistence operations; `LivestockDAO` owns SQLite SQL and connection lifetimes.
- **Service Layer** — `LivestockService` encapsulates business rules, input validation, and operation results.
- **Constructor dependency injection** — `LivestockService(ILivestockDAO)` receives the dependency rather than instantiating SQLite internals. **Qualify:** controllers still construct a concrete service + DAO; injection exists at the service boundary, not across the whole JavaFX app.
- **Abstraction and polymorphism** — production `LivestockDAO` and test `FakeLivestockDAO` are replaceable via the interface.
- **Encapsulation/separation of concerns** — JavaFX handles interaction, service handles rules, DAO owns storage. No claim of a new Factory/Observer/Builder pattern unless separately demonstrated in code.

## Why this is evidence of systematic improvement

- Commit `26e63e6` — search service and tests.
- Commit `a771900` — JavaFX search integration.
- Commit `86bfe77` — deletion service, DAO contract change, confirmation UI, tests.
- Commit `286d2e5` — editing and validation with tests.
- Commit `7e1ef15` — SQLite UPDATE and DELETE integration tests.
- PR #37: https://github.com/TheCringe2024/Farmbook/pull/37 (merged into `main` as `83672931`).

## Honest boundaries

- No full-app GUI automation; UI was manually exercised and captured.
- `searchLivestock` currently filters an in-memory result from `findAll()`; for much larger datasets a DAO-side query could be an improvement, but it is not required for the current prototype.
- `LivestockDAO` uses the common `farmbook.db`; user-level record partitioning (FR13) has not been established by this implementation.
- Further UI layout consistency and weather responsiveness are **team-wide** work, not automatically attributable to this PR.
