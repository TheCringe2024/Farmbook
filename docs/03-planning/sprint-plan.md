# FarmBook - Sprint Plan

## Sprint 1 - Foundation

**Goal:** A farmer can sign up, log in, log out, and reach a working home screen.

| Task | What It Covers |
|---|---|
| Database setup | User table, connection |
| Login logic | Check credentials, start session |
| Sign-Up logic | Register new user, reject bad input |
| Login/Sign-Up screens | Build the actual JavaFX screens |
| Home Page | Navigation to Crops, Inventory, Livestock |
| Git setup | Branching, first tests, .gitignore |

**Done when:** a farmer can register, log in, see the home screen, and log out.

## Sprint 2 - Crops

**Goal:** A farmer can add and view crops.

| Task | What It Covers |
|---|---|
| Crop model + database | Save and load crops |
| Add Crop screen | Type, field, quantity, date |
| Crop List screen | View all crops |
| Tests | Confirm saving/loading works |

**Done when:** a farmer adds a crop and sees it in the list.

## Sprint 3 - Inventory & Stock

**Goal:** A farmer can add items and track stock in/out.

| Task | What It Covers |
|---|---|
| Item model + database | Save and load items |
| Incoming Stock screen | Add stock to an item |
| Outgoing Stock screen | Remove stock, blocked if not enough |
| Tests | Confirm stock can't go negative |

**Done when:** a farmer can add, add-to, and remove stock correctly.

## Sprint 4 - Livestock

**Goal:** A farmer can add and view livestock, with valid dates only.

| Task | What It Covers |
|---|---|
| Livestock model + database | Save, load, delete animals |
| Add Livestock screen | Species, tag, date acquired |
| Livestock List screen | View all animals |
| Date validation | Reject bad/impossible dates |
| Tests | Confirm save/find/delete and date checks work |

**Done when:** a farmer adds an animal with a valid date and sees it in the list; bad dates are rejected.

## What Changed

| Change | Reason |
|---|---|
| Added Sign-Up and Home Page into Sprint 1 | Originally only planned Login and Logout, but the real app needs a full sign-up flow and somewhere to land |
