# Campus Lost-and-Found Tracker

A Java Swing desktop application for the **Object-Oriented Programming Using Java** mini project.

## Features

- Add a Lost or Found item with its name, campus location and contact details.
- Search by ID, type, item name, location or contact.
- Filter listings to show All, Lost or Found.
- Delete a selected listing.
- Save listings to a CSV file and load them again later.
- Automatically generated listing IDs.
- Four sample listings are loaded when the application starts.
- Dashboard cards show total, lost and found item counts.

## Application preview

The application opens as a Windows desktop window built with Java Swing. The main screen contains the add-listing form, summary cards, search and filter controls, and the item table.

## Requirements

- Java Development Kit (JDK) 17 or newer
- Windows for the supplied `.bat` launchers; macOS/Linux can use the shell script

No external Java libraries are required.

## Run on Windows

1. Download the repository using **Code → Download ZIP**, then extract it.
2. Install JDK 17 or newer.
3. Double-click `run_windows.bat`.
4. If a prebuilt JAR is present, the launcher runs it. Otherwise, it compiles the source and starts the app.

You can also double-click `run_from_source.bat` to compile and launch directly from the source code.

## Compile and run manually

From the repository folder:

```bash
javac -encoding UTF-8 -d out src/campus/model/*.java src/campus/storage/*.java src/campus/ui/*.java
java -cp out campus.ui.CampusLostAndFoundTracker
```

## How to demonstrate the application

1. The application opens with four sample listings.
2. Select **Lost** or **Found** in the form.
3. Enter the item name, location and contact details.
4. Click **Add Listing**. The item appears in the table with a new ID.
5. Search for a record or use the **All / Lost / Found** filter.
6. Select a row and click **Delete Selected** to remove it.
7. Click **Save** and choose a location to create a CSV file.
8. Click **Load** and select a saved CSV file to restore its listings.

## Java and OOP concepts used

| Concept | Where it is used |
|---|---|
| Classes and objects | `Listing`, `LostListing`, `FoundListing` and the main window |
| Encapsulation | Private fields and public getters in `Listing` |
| Inheritance | `LostListing` and `FoundListing` extend `Listing` |
| Abstraction | `ListingRepository` interface |
| Interface implementation | `CsvListingRepository` implements `ListingRepository` |
| Method overloading | The overloaded `searchListings` methods |
| Collections | `ArrayList<Listing>` stores the listings |
| File handling | CSV save and load operations |
| Exception handling | File operations and validation |
| Event-driven programming | Swing button and search actions |
| GUI components | Java Swing forms, buttons, dropdowns and `JTable` |

## Project structure

```text
Campus-Lost-and-Found-Tracker/
├── README.md
├── run_windows.bat
├── run_from_source.bat
├── run_mac_linux.sh
└── src/
    └── campus/
        ├── model/
        │   ├── Listing.java
        │   ├── LostListing.java
        │   └── FoundListing.java
        ├── storage/
        │   ├── ListingRepository.java
        │   └── CsvListingRepository.java
        └── ui/
            └── CampusLostAndFoundTracker.java
```

## Notes

- This is a **desktop application**, not a website.
- The sample records are demonstration data.
- Use **Save** to keep records between application sessions; newly added records are held in memory until saved.

---
Developed as a college mini project.
