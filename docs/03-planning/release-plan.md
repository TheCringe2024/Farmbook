# FarmBook - Release Plan

FarmBook is built in two releases.

## Release 1 - The Basics

**Goal:** A farmer can sign up, log in, log out, and land on a working home screen.

| Feature | Why It's Needed |
|---|---|
| Login (FR01) | Only the right person can get in |
| Sign-Up | A new farmer can actually make an account |
| Logout | Safely end a session |
| Home Page | Somewhere to go after logging in |
| Consistent UI | The app looks finished, not thrown together |

**Why first:** nothing else matters if a farmer can't log in and reach the rest of the app.

## Release 2 - Farm Features

**Goal:** A farmer can track crops, inventory, and livestock.

| Feature | What It Does |
|---|---|
| Crops (FR02) | Add a crop, see it in a list |
| Inventory (FR05) | Add and track items |
| Incoming/Outgoing Stock (FR06) | Track stock coming in and going out |
| Livestock (FR07) | Add an animal with a valid date, see it in a list |

**Why after Release 1:** all these features work the same way underneath (save something, list it). Building Crops first made the rest much faster to build.

## What Changed

| Change | Reason |
|---|---|
| Added Sign-Up and Home Page into Release 1 | Originally only planned Login and Logout, but the real app needs a full sign-up flow and somewhere to land after logging in |
