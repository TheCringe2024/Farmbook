# Vy - Work Log

## What I built

- **Crops:** add a crop and see a list of crops
- **Stock in and out:** add stock, remove stock, and it won't let stock go below zero
- **Livestock:** add an animal, see the list, and go back between screens
- **Tests:** tests for crops, items and livestock. For the delete and negative-stock tests I wrote the test first, then the code.
- **Refactor:** I tidied up ItemDAO (commit `e7149cc`). The same code was copied in three places, so I put it in one helper. All 60 tests passed before and after.
- **Planning:** release plan, sprint plan and wireframes
- **Fixes:** sorted out merge conflicts and two bugs that broke the build

## My time

| When | What I did | Hours |
|---|---|---|
| 18-20 Sept | Built crops, stock and livestock | 12 |
| 18-20 Sept | Fixed merge conflicts and bugs | 3 |
| 18-20 Sept | Comments and tests | 4 |
| 18-20 Sept | Release plan, sprint plan, wireframes | 3 |
| 25 Sept - 9 Oct | Pulled team changes and checked the tests | 1 |
| 25 Sept - 9 Oct | Refactored ItemDAO and checked the build server | 2.5 |
| 25 Sept - 9 Oct | Wrote my evidence | 2 |

## What I learned

- Refactoring feels safe when the tests pass before and after.
- Pull before you push, because teammates push all the time.
- Don't paste commands while the app is running, because they go to the app, not the terminal.
