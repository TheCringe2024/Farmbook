# CI, Javadoc and branch-workflow evidence

## CI implementation

- Workflow source: `.github/workflows/maven-ci.yml` (`FarmBook CI`).
- Ubuntu 24.04 runner; Java 21 Temurin; `mvn --batch-mode clean test`; `mvn javadoc:javadoc`; deploy Javadoc via GitHub Pages action.
- Trigger: push, pull request targeting `main`, and manual workflow_dispatch.
- **PR #37 final test log**: `80 tests run, 0 failures, 0 errors, 0 skipped; BUILD SUCCESS`.
- Verified workflow run `39735297593`, job `119679458298` completed successfully; steps `Build and run tests`, `Generate Javadoc`, `Publish Javadoc` all concluded successfully.
- GitHub also showed two green checks on PR #37 (pull_request and push). For full-mark rubric, show *multiple* distinct final-sprint runs, not a single green screenshot.
- `gh-pages/index.html` was checked on 22 Oct; its header identifies Javadoc 21 generated 22 Oct 2026.

## Genuine sustained adoption to demonstrate

1. **PR #30 (Week 11)** — CI first established; earlier evidence under `docs/06-week11/kevin/ci/README.md`.
2. **PR #37 (Week 13)** — five focused commits (Search, UI Search, Delete, Edit, persistence tests) triggered ongoing CI activity and an observed successful PR run.
3. **Final `main`** — verify the newest push run after the team integrates final changes; evidence needs screenshot or downloaded log of its status, not just PR checks.

## Generated HTML in the submission archive

**Critical:** repository `.gitignore` ignores `/docs/javadoc/` and `target/`; GitHub Pages being live does NOT satisfy the requirement to include HTML pages in the **Canvas ZIP**.

On the Windows machine after pulling the **final** `main`:

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd javadoc:javadoc
Test-Path .	arget\docs\javadoc\index.html  # must return True
```

Copy all of `target/docs/javadoc` (including assets, CSS, JS, nested class pages) into the final **ZIP staging tree** at `Farmbook/docs/07-submission2/generated-javadoc/`. After that, check the packaged `index.html` opens locally with styles. Do not confuse it with `docs/06-week11` markdown notes or only include a link.

## Key URLs (also show exact ZIP paths)

- PR and chronological commits: https://github.com/TheCringe2024/Farmbook/pull/37
- Verified Actions run: https://github.com/TheCringe2024/Farmbook/actions/runs/39735297593
- Generated source version: https://github.com/TheCringe2024/Farmbook/blob/gh-pages/index.html

## CI limits

Checks verify build, automated tests and documentation generation, **not** complete JavaFX GUI usability, per-account data isolation or absence of operational bugs. Do not claim complete test coverage without a coverage tool report.
