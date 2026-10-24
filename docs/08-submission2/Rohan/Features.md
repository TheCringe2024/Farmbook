# Crop Management - Feature Implementation

## Overview
Implemented crop lifecycle tracking, filtering, validation, growth logs, and plant needs tracking.

## Implemented Features

### 1. Crop Details & Lifecycle
- **Crop Attributes:** Added `Location` (Field/Plot) and `Status`.
- **Status Tracking:** Crops can be marked as `Growing`, `Harvested`, or `Failed`.
- **Active Filtering:** Only `Growing` crops appear by default in tables and charts.
- **Historical Crops:** A checkbox toggles visibility of harvested and failed crops.
- **Notes:** Optional notes can be saved, displayed, and edited.

### 2. Growth Tracking & Plant Needs
- **Needs Log:** Record crop-specific needs, such as watering and fertilizing.
- **Growth Log:** Add dated growth entries.
- **Chronological Sorting:** Growth entries display newest first using SQL `ORDER BY entry_date DESC`.
- **Dynamic Updates:** Selecting a crop loads its associated needs and growth logs into the side panel.

### 3. Data Validation & Feedback
- **Validation:** Added a `CropValidator` class.
- **Required Fields:** Prevents saving when required fields, such as Name or Amount, are empty.
- **Numeric Validation:** Rejects non-numeric values in the `Amount` field.
- **Status Feedback:** Displays validation errors in red and successful saves in green.

### 4. UI Updates & Analytics
- **Instant Refresh:** Tables update after crops are added, edited, or deleted.
- **Live Charts:** Pie and bar charts update automatically to reflect current data.

## Database & Architecture Changes

1. **Schema Updates:** Added `location` and `status` columns to the `crops` table.
2. **Relational Tables:** Created two SQLite tables:
    - `crop_needs`: `id`, `crop_id`, `description`
    - `crop_growth`: `id`, `crop_id`, `entry_date`, `description`
3. **Cascade Cleanup:** Updated `CropDAO.delete()` to remove associated needs and growth records when a crop is deleted.
4. **Separation of Concerns:** Moved validation logic from the UI controller into `CropValidator`.