# Week 13 feedback-driven improvement — findings and boundaries

## Proven authentic feedback

**Week 9 marker:** “Prototype good has some good features. Consider having a logo on each page. Also there is a lack of uniformity with some screens. OO principles - some aspects shown - need more.” The tutor also commented “TDD only basics shown.” (Source: `CAB302 Assessment 2 – Submission 1` marked feedback, score 75.5/100.)

**Team discussion (Discord #week13, 19 October):** Jeremy reported that returning from Livestock to Dashboard changed window size / went fullscreen. The user later reported Livestock navigation worked normally in their latest build; no verified fix is attributed to PR #37. The team also discussed icon and window-sizing improvements. Those should be evaluated as separate team changes, not credited to Kevin's FR14 commits without direct evidence.

**Accepted FR14 functionality gap:** previous Livestock list could create and display animals, but had no Search/Edit/Delete controls. This was a requirements and internal review gap, not necessarily a measured usability-study finding.

## Issue → decision → change → test → Git traceability

| Finding / origin | Engineering response | Verified evidence | Limit |
|---|---|---|---|
| FR14 missing Search | Added live case-insensitive search by species/tag | `LivestockService.java`, `LivestockListController.java`, `livestock-list-view.fxml`; commits `26e63e6`, `a771900`; `evidence/search-ui.png` | No timing or participant success-rate measurements |
| FR14 missing Edit | Edit Selected dialog with prefilled values and validation feedback; DAO SQL UPDATE | commit `286d2e5`; UI screenshot `evidence/edit-dialog.png`; `LivestockServiceTest` and `LivestockPersistenceTest` | Screenshot proves dialog appearance, not an independent usability experiment |
| Potential accidental Delete | Added confirmation and cancel path, DAO success feedback | commit `86bfe77`; `evidence/delete-confirm-dialog.png`, `delete-ui-success.png` | No destructive changes claimed without manual verification |
| Week 9 OO/TDD feedback | Explain pre/post architecture and maintain genuine Red/Green screenshots; add Update/Delete persistence tests | `01-ood-refactoring.md`, `02-tdd-and-tests.md`, PR #37 | Original architecture refactor preceded PR #37 (Week 11 PR #31) |
| Week 9 logo/UI consistency | Team should assess consistency across *all* application screens | Week 9 feedback and team UI work | Not marked completed by this evidence |

## Small, honest usability test sheet (complete with REAL observations only)

Participants: **not yet independently recorded**. Preferred: one teammate unfamiliar with the latest Livestock UI or a user matching the farmer persona. Ask them to attempt: (1) locate a sheep by tag, (2) edit its acquisition date, (3) cancel a deletion, (4) delete an intentionally-created test record. Log task completion, navigation confusion, errors, assistive prompts and timing only if actually measured.

| Task | Participant/date | Completed unaided? | Observed confusion or error | Follow-up decision |
|---|---|---|---|---|
| Search by tag | NOT RECORDED | — | — | — |
| Edit saved record | NOT RECORDED | — | — | — |
| Cancel delete | NOT RECORDED | — | — | — |
| Delete test record | NOT RECORDED | — | — | — |

## Next actions for the team

- Verify presentation of title/button size at laptop resolution; avoid making untested accessibility claims.
- Add tutor/peer feedback excerpts and actual results if received before code freeze.
- Team should capture a short retrospective naming what went well, blockers and what changed after Week 9.
- Update the table with actual evidence, not retroactively invented user data.
