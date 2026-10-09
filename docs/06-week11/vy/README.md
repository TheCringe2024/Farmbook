# Vy - Week 11 Evidence

My parts: Crops, Inventory and Stock, Livestock.

## Checklist

| What the rubric wants | My evidence |
|---|---|
| Better prototype | New features added, 60 tests pass |
| Build script | `pom.xml` (Maven) |
| Build server | GitHub Actions, run #19 passed |
| Refactoring | Commit `e7149cc` in ItemDAO |
| OO design patterns | DAO, MVC, singleton, interfaces |

## 1. Build server

Every time someone pushes, GitHub Actions builds the project and runs all the tests. Run #19 was my refactor commit and every step passed.


## 2. Refactoring

Commit `e7149cc` - ItemDAO.

**Before:** the same code was copied into `save`, `addStock` and `removeStock` (open connection, run the SQL, catch the error). `removeStock` was about 30 lines.

**After:** I moved the copied code into three small helpers:
- `executeUpdate` runs any insert or update
- `hasEnoughStock` checks there is enough stock
- `mapRow` turns a database row into an Item

`removeStock` is now about 7 lines.

**Nothing broke.** 60 tests passed before and the same 60 passed after.


## 3. My tests

- `CropDAOTest` - save and find a crop, delete a crop
- `ItemDAOTest` - save and find an item, stock can't go below zero, negative stock is rejected

For the delete and negative-stock tests I wrote the test first, watched it fail, then wrote the code to pass it.

## 4. OO design patterns

- **DAO:** `ItemDAO`, `CropDAO` and `LivestockDAO` hold all the database code, so the screens never touch SQL.
- **MVC:** models (`Item`, `Crop`, `Livestock`), FXML screens, and controllers.
- **Singleton:** `SqliteConnection` gives the whole app one shared database connection.
- **Interfaces:** `ILivestockDAO` separates what the code does from how it does it.
