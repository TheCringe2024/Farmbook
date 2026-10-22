# Week 13 — Kevin evidence index and video/ZIP map

**Required destination in final archive:** `Farmbook/docs/08-submission2/kevin/` (keep identical folder names in walkthrough).

| Rubric | What marker should see | Exact source / documentary path | Git proof | Video cue |
|---|---|---|---|---|
| OOD (40 pts) | Service Layer, DAO interface, polymorphism, constructor injection | `src/main/java/com/example/farmbook/service/LivestockService.java`; `src/main/java/com/example/farmbook/dao/ILivestockDAO.java`; `src/main/java/com/example/farmbook/dao/LivestockDAO.java`; `docs/08-submission2/kevin/01-ood-refactoring.md` | Week 11 PR #31 and Week 13 PR #37 | Show interface, constructor, service, DAO, then architecture document |
| CI (20 pts) | Maven/Java 21 GitHub Actions, test execution, Javadoc generated | `.github/workflows/maven-ci.yml`; `docs/08-submission2/kevin/03-ci-and-javadoc.md`; `docs/07-submission2/generated-javadoc/index.html` **must be added for final archive** | PR #37 run `39735297593`; main branch run needs final-check | Open Actions logs & offline `generated-javadoc/index.html` |
| Feedback (40 pts) | Usability pain points → targeted new record workflows; genuine development feedback evidence and limitations | `src/main/resources/com/example/farmbook/livestock-list-view.fxml`; `src/main/java/com/example/farmbook/LivestockListController.java`; `docs/08-submission2/kevin/04-feedback-and-ux.md`; screenshots in `docs/08-submission2/kevin/evidence/` | #37 commits for Search/Edit/Delete | Show before/after UI and inputs; don't invent user-study figures |
| TDD under development process | Real failing→passing tests and expanded persistence checks | `src/test/java/com/example/farmbook/LivestockServiceTest.java`; `src/test/java/com/example/farmbook/LivestockPersistenceTest.java`; `docs/08-submission2/kevin/02-tdd-and-tests.md` | Commits `26e63e6`, `86bfe77`, `286d2e5`, `7e1ef15` | Show selected Red/Green screenshots, verify files |
| FR01 check | Logout clears authenticated state and username (source review; manual navigation verification pending) | `src/main/java/com/example/farmbook/SessionState.java`; `src/main/java/com/example/farmbook/HomePageController.java`; `docs/08-submission2/kevin/05-fr01-logout-review.md` | Week 9/11 historical work, current source | Only claim check after manual tests |

## Local screenshot file index (included in this prepared package)
- `evidence/search-red.png` — `LivestockServiceTest` Search Red; actual failure.
- `evidence/search-green.png` — 7 service tests Green.
- `evidence/search-ui.png` — Search field and filtered row.
- `evidence/delete-red.png`, `evidence/delete-green.png` — Delete service TDD.
- `evidence/delete-confirm-dialog.png`, `evidence/delete-ui-success.png` — Delete confirmation and success.
- `evidence/update-red.png`, `evidence/update-green.png` — Update service TDD.
- `evidence/edit-dialog.png` — JavaFX Edit form with prefilled fields.
- `evidence/pr37-ci-passed.png` — actual PR screenshot with checks green.

**Video–ZIP consistency:** the video narrator must name this exact path on screen and the team must copy this folder into the FINAL repository ZIP, not merely link to GitHub.
