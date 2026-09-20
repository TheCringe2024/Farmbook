> Historical iteration evidence. Test counts below reflect the repository state at the time this feature was completed. The current Week 9 regression suite contains 39 passing tests.
# FR07 Livestock Persistence Verification


## Objective



Verify that livestock records are persisted in SQLite and remain available after navigation and application restart.



## Persistence Flow



Add Livestock

→ LivestockDAO.save()

→ SQLite `farmbook.db`

→ Application closes

→ Application restarts

→ LivestockDAO.findAll()

→ Saved record remains available



\## Automated Verification



`LivestockPersistenceTest` verifies that:



1. A livestock record can be saved through `LivestockDAO`.

2. The original database connection is closed.

3. A new `LivestockDAO` instance reads the database.

4. The saved animal is found using `findAll()`.

5. Test data is removed after the test.



## Manual Verification



Manual persistence workflow:



1. Login to FarmBook.

2. Open Livestock.

3. Add a livestock record.

4. Confirm the record appears in the Livestock List.

5. Close FarmBook completely.

6. Restart FarmBook.

7. Login and reopen Livestock.

8. Confirm the record still exists.



This demonstrates persistence across application sessions.



## Regression Testing



Full Maven test suite:



- Tests run: 20

- Failures: 0

- Errors: 0

- Skipped: 0

- Result: BUILD SUCCESS

