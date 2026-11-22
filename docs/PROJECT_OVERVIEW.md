# Project Overview

## Purpose
The Campus Lost-and-Found Tracker is a desktop utility for recording and searching items reported lost or found around a campus.

## Main interface
The Java Swing window contains:
- Summary cards for total, lost and found listings.
- A form for adding a Lost or Found record.
- A searchable table of campus listings.
- A type filter for All, Lost and Found records.
- Actions to delete a selected record, save records to CSV, and load records from CSV.

## Data model
- `Listing` is the abstract parent class containing the shared item fields.
- `LostListing` and `FoundListing` extend `Listing`.
- `ListingRepository` defines the save and load operations.
- `CsvListingRepository` implements those operations using CSV files.

## Demo sequence
1. Start the application and show the four sample records.
2. Add a new Lost item and show the total and lost counts update.
3. Search for an item by name or location.
4. Filter the table to show only Found items.
5. Select a record and demonstrate Delete Selected.
6. Save the listings to a CSV file.
7. Load the saved CSV file to restore the records.

## Technical details
- Java 17 or newer
- Java Swing GUI
- ArrayList for in-memory listings
- CSV file handling using Java standard libraries
- No external dependencies
