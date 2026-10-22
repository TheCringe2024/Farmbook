# FR01 — Logout and session regression review (source-only verification)

Current `main` code reviewed on 22 Oct 2026:

- `SessionState.logout()` sets `authenticated = false` and `currentUsername = null`.
- `HomePageController.onExit()` invokes `SessionState.logout()` and `HelloApplication.showLogin()`.
- `HelloApplication.showHome()` and `showSettings()` check authentication and return to Login if not authenticated.
- This is evidence of intended logout control flow, **not a complete security review**. `SessionState` remains static in-memory state, and account-specific persistence cannot be inferred from this.

## Manual smoke test checklist (results NOT supplied)

- [ ] Login with account A; verify Dashboard loads.
- [ ] Choose Logout; confirm Login page and cleared session.
- [ ] Attempt to reopen Settings/Home without logging in; confirm redirected to Login.
- [ ] Login with account B; verify correct account identity and no stale account A display.
- [ ] Check no JavaFX exceptions on repeated logout/login.

Do not mark these steps PASS until they have been run. If they fail, notify Auth/Integration owner and attach terminal log rather than silently changing shared code during final code freeze.

Relevant story: `docs/03-planning/user-stories/US02-logout.md` (FR01 source requirements). There may also be separate unit tests in the test tree; do not infer their names only from CLI test output.
