# US02 – Logout

## Source Requirement

FR01 – User Access

## Original User Story – V1

As a farmer, I want to be able to logout so that I can leave my computer without exposing my data.

## Refined User Story – V2

As a logged-in farmer, I want to log out of FarmBook so that other people using the same computer cannot access my farm information.

## Priority

Must

## Sprint

Sprint 1

## Acceptance Criteria

### AC1 – Successful logout

Given the farmer is currently logged in,
when the farmer selects Logout,
then the current authenticated session is ended.

### AC2 – Return to Login

Given the farmer has successfully logged out,
when the logout process is completed,
then FarmBook displays the Login screen.

### AC3 – Access protection

Given the farmer has logged out,
when they attempt to access protected farm information,
then they must log in again before the information can be accessed.

## Expected UI Flow

Login
→ Homepage
→ Settings
→ Logout
→ Login

## Related UI

The current Figma design places the Logout action on the Settings screen.

Expected behaviour:
1. The authenticated farmer opens Settings.
2. The farmer selects Logout.
3. FarmBook ends the authenticated state.
4. FarmBook returns to the Login screen.
5. Farm information cannot be accessed until the farmer logs in again.

## Dependencies

- Login functionality must establish an authenticated user state.
- Homepage/Settings must provide access to the Logout action.
- After logout, navigation must return to the Login screen.