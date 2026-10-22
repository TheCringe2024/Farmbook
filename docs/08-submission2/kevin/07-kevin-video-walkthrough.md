# Submission 2 — Kevin's video contribution and capture map

**Separate from Assessment 3.** Team video walkthrough maximum **10 minutes**. Reserve approximately 2.5–3 minutes for Kevin's OOD/TDD/CI section within the team's overall timeline; final allocation must be confirmed by the video editor. Narrated file paths below must also exist in the submitted ZIP.

## Suggested Kevin section, ~2:30–2:50

**0:00–0:40 — OO evolution** (open `docs/07-submission2/kevin/01-ood-refactoring.md`, then `src/main/java/com/example/farmbook/service/LivestockService.java`)

> I focused on extending our Livestock feature using the architecture we introduced earlier. The JavaFX controller handles user interaction, while `LivestockService` owns the validation and business operations. The service depends on `ILivestockDAO` through constructor injection, and `LivestockDAO` implements SQLite persistence. The interface also makes it possible to use a fake DAO in tests, reducing coupling.

**0:40–1:20 — Before/after FR14** (show `LivestockListController.java`, `livestock-list-view.fxml`, `dao/LivestockDAO.java`; optionally show a recorded GUI)

> Earlier, the list displayed livestock as plain strings. We changed it to retain Livestock objects and their database IDs, allowing users to select a specific record. We added Search, Edit, and Delete with a confirmation before deletion. The DAO returns an operation result so the UI can show success or failure. The changes are traceable through five commits in PR #37.

**1:20–1:55 — TDD and persistence** (open `docs/07-submission2/kevin/02-tdd-and-tests.md`, `src/test/java/com/example/farmbook/LivestockServiceTest.java`, and `LivestockPersistenceTest.java`)

> We captured real failing and passing test runs for the new operations. Our persistence tests use SQLite to save records, then update or delete them, and read them again through a new DAO. The code is tested without claiming these service tests automate the complete JavaFX interface.

**1:55–2:40 — CI and Javadoc** (open `.github/workflows/maven-ci.yml`, Actions log for PR #37, then `docs/07-submission2/generated-javadoc/index.html` in unpacked submission)

> Every push and pull request triggers our GitHub Actions workflow with Java 21. The verified PR run completed all 80 tests successfully, generated Javadoc and published the documentation. The final submission archive also includes the generated HTML pages so the marker can inspect them without leaving the ZIP.

## Editing checklist

- Show the file tree and precise path in each code shot; zoom enough to read.
- Prefer one clean browser tab for GitHub run, one IntelliJ window for code, one local HTML Javadoc page.
- Avoid presenting old screenshots as current final-sprint results.
- Confirm microphone clarity; Week 9 tutor noted some video presentation deficiencies.
- End before team video exceeds 10:00; rehearse speaker handoffs.
- If `generated-javadoc/index.html` has not actually been copied into the final archive, **do not claim it is included**.
