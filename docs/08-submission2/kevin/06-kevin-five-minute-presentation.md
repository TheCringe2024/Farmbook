# Assessment 3 — Kevin's ~55-second speaking section

**Important:** This is the team's **five-minute live Assessment 3**, separate from Submission 2's **maximum 10-minute video**. The Discord 19 Oct allocation assigned Kevin: **“Your Development Approach and Key Decisions”** (slide/topic 3). Overall team order: Jeremy → Ronell → Kevin → Vy → Rohan.

## Speaker script (~115 words; rehearse to stay near 55–60 seconds)

> Now I'll explain a few key technical decisions behind FarmBook. We used JavaFX for the interface and SQLite to store farm records locally. For our Livestock feature, we applied a Service Layer and DAO interface to separate the screen from business rules and database operations. This also let us test the service with a fake DAO instead of always using SQLite. During our final sprint, I extended that structure to support searching, editing and deleting records. I used Red–Green TDD for these changes and added persistence tests to check the updates. Our GitHub Actions workflow then confirmed that all 80 automated tests passed. Overall, these choices made the feature easier to maintain and test. I'll pass over to Vy for quality across the system.

## Slide content (single slide, no dense paragraph)

**Title:** Development Approach & Key Decisions

- **JavaFX + SQLite** — working desktop UI and local persistent records.
- **Service Layer + DAO interface** — separation of UI, rules and storage; constructor injection enables fake DAO tests.
- **TDD + GitHub Actions** — FR14 Search/Edit/Delete, 80 tests passed on PR #37.

**Recommended visual:** the architecture diagram in `01-ood-refactoring.md`, with tiny footer `PR #31 → PR #37`.

## Mouse/pointing cues

- 'Service Layer and DAO' → point at `LivestockService` → `ILivestockDAO` → `LivestockDAO`.
- 'searching, editing and deleting' → briefly point at feature list or final UI screenshot.
- '80 automated tests' → point at green CI badge screenshot; don't open terminal during the 5-minute talk.

**If asked:** 'Why an interface?' → 'It decouples business logic from SQLite, so we can test with a fake DAO and change the storage layer more easily.'
