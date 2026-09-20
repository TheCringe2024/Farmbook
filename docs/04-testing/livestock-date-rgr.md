# Livestock Acquisition Date — Red–Green–Refactor Evidence

## Feature

Livestock acquisition dates must:

- use the `YYYY-MM-DD` format;
- represent a real calendar date;
- reject blank and null values;
- be validated before a livestock record is saved.

## Relevant files

- `src/main/java/com/example/farmbook/LivestockValidator.java`
- `src/main/java/com/example/farmbook/AddLivestockController.java`
- `src/test/java/com/example/farmbook/LivestockValidatorTest.java`

## Test cases

| Test case | Expected result |
|---|---|
| `2026-09-20` | Accepted |
| `20/09/2026` | Rejected |
| `2026-02-30` | Rejected |
| Blank value | Rejected |
| Null value | Rejected |

## 1. Red

The tests were written before the validation behaviour was implemented.
The initial validator returned `true`, causing invalid, impossible, blank and
null date test cases to fail.

## 2. Green

The minimum validation behaviour was implemented using `LocalDate.parse`.
The controller was updated to reject invalid dates before calling the DAO.
The targeted tests and full test suite then passed.

## 3. Refactor

The validator was converted into a utility class, a named ISO date formatter
was introduced, and the broad exception handling was replaced with
`DateTimeParseException`. The external behaviour did not change.

## Result

The feature accepts correctly formatted real dates and prevents invalid
livestock acquisition dates from reaching the persistence layer.
