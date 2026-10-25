# Crop Management - Feature Implementation

## Overview
Implemented crop lifecycle tracking, filtering, validation, growth logs, and plant needs tracking.

## User Stories
Related to:
### FR02 - Crop Records `Record Planted Crops `
- I can add a crop with type, field/plot, quantity and date planted.
- A crop cannot be saved with a missing field or a non-numeric
  quantity

### FR03 - Crop Progress `Record Planted Crops `
- I can add a dated growth entry (e.g. stage, height, notes) to a
  crop.
- A crop’s growth entries are listed in date order.

### FR03 - Crop Progress `Mark Crops Harvested or Failed`
- I can set a crop’s status to Growing, Harvested or Failed.
- Harvested and failed crops are hidden from the active list but
  can still be viewed.

### FR04 - Crop Needs `Record Crop Needs`
- I can add a need (watering, fertiliser or pest treatment) to a crop
   with a frequency or due date.
- A crop’s needs are shown when the crop is selected.

### FR05 - Inventory Records `Add Inventory Items`
- A non-numeric quantity is rejected with a message instead of
  crashing.

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