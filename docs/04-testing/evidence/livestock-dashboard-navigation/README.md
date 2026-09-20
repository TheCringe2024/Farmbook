\# FR07 Livestock Dashboard Navigation Evidence



\## Objective



Verify that the Livestock feature is correctly integrated with the FarmBook dashboard and that users can navigate between the dashboard, livestock list, and add livestock screens.



\## Navigation Flow



Dashboard

→ Livestock List

→ Add Livestock

→ Livestock List

→ Dashboard



\## Changes



\- Corrected the Livestock List window title.

\- Standardised FXML resource loading through the relevant controllers.

\- Improved navigation from the Livestock List back to the dashboard.

\- Restored the FarmBook window title when returning to the dashboard.

\- Centred and resized the application window consistently during navigation.



\## Manual Verification



The following workflow was manually verified:



1\. Login to FarmBook.

2\. Open Livestock from the dashboard.

3\. Confirm the Livestock List screen opens.

4\. Open the Add Livestock screen.

5\. Return to the Livestock List.

6\. Return to the dashboard.

7\. Confirm existing Inventory navigation still works.

8\. Confirm Logout still returns the user to the login screen.



\## Regression Testing



Maven regression test suite:



\- Tests run: 19

\- Failures: 0

\- Errors: 0

\- Skipped: 0

\- Result: BUILD SUCCESS

