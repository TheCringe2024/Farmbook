# Kevin Duy — CAB302 Submission 2 Evidence (22 Oct 2026)

**Scope:** Individual contribution evidence for FarmBook (FR07/FR14 Livestock; earlier FR01/CI work). **Canonical repository:** `TheCringe2024/Farmbook`; **merged PR:** [#37](https://github.com/TheCringe2024/Farmbook/pull/37) on 22 Oct 2026 16:45 AEST; **merge commit:** `83672931f828af209ce29d869b64e95a1ccac71e`.

## Start here
1. [Evidence index](evidence-index.md) — maps rubric claims to files, commits and the video.
2. [OOD and refactoring](01-ood-refactoring.md) — architecture before/after, DAO + Service Layer + constructor dependency injection; precise limitations.
3. [TDD and test evidence](02-tdd-and-tests.md) — genuine Red/Green evidence, SQLite persistence tests and test counts.
4. [CI and Javadoc](03-ci-and-javadoc.md) — GitHub Actions checks, sustained-workflow evidence and offline Javadoc packaging instructions.
5. [Feedback and UX validation](04-feedback-and-ux.md) — real tutor feedback, requirements and items still awaiting independent usability testing.
6. [FR01 regression review](05-fr01-logout-review.md) — inspected source and a pending manual verification checklist.
7. [Kevin's Assessment 3 presentation](06-kevin-five-minute-presentation.md) — his ~50–60-second section in the TEAM's **5-minute** live presentation.
8. [Submission 2 video guidance](07-kevin-video-walkthrough.md) — separate video walkthrough (**team maximum 10 minutes**), path/cursor/voice cues.
9. [Final handoff](08-final-submission-handoff.md) — team actions before Canvas upload.

## Verified (not forecasts)
- PR #37 merged; five feature commits; seven files changed.
- GitHub Actions PR run `39735297593`, job `119679458298`: **80 tests, 0 failures, 0 errors, 0 skipped**; Maven build, Javadoc generation and deployment steps succeeded.
- Generated `gh-pages/index.html` inspected; page header reports Javadoc 21 generated on 22 Oct 2026.
- Screenshots in `evidence/` are unaltered conversation-supplied screenshots documenting the user's local development runs. They do **not** establish formal usability metrics.

## Important qualifications
- PR #37 extends existing Week 11 service/DAO design. Do **not** imply all those patterns originated in PR #37; cite Week 11 PR #31 and corresponding docs.
- UI screenshot of Edit dialog verifies the dialog opens, not an automated GUI test.
- Current SQLite integration tests target the project's `farmbook.db`; a dedicated isolated test database has **not** been implemented.
- Do not claim account-scoped FR13 data isolation, independent user-study results, or a passing fresh `main` run without separate verification.
- Do not use this evidence folder to replace full team-wide git reports, PM export, source, `*.git`, Javadoc HTML or team walkthrough video in Canvas submission.
