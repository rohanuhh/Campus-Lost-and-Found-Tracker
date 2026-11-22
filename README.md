# Campus Lost-and-Found Tracker

A Java Swing desktop application for the Object-Oriented Programming Using Java mini project.

## Features

- Add a Lost or Found item with its name, campus location and contact details.
- Search by ID, type, item name, location or contact.
- Filter the table to show All, Lost or Found listings.
- Delete a selected listing.
- Save all listings to a CSV file.
- Load listings from a previously saved CSV file.
- Automatically generated listing IDs.
- Four sample listings are loaded when the application starts.

## Run on Windows

1. Install **Java 17 or newer**. A JDK is recommended.
2. Extract the ZIP folder.
3. Double-click `run_windows.bat`.
4. The Campus Lost-and-Found Tracker window will open.

You can also run the included JAR from Command Prompt:

```bash
java -jar CampusLostAndFoundTracker.jar
```

## Run from source

If you have a JDK installed, double-click `run_from_source.bat`, or compile manually:

```bash
javac -encoding UTF-8 -d out src/campus/model/*.java src/campus/storage/*.java src/campus/ui/*.java
java -cp out campus.ui.CampusLostAndFoundTracker
```

## How to demonstrate it

1. The application opens with four sample listings.
2. Select **Lost** or **Found** in the left panel.
3. Enter an item name, location and contact details.
4. Click **Add Listing**. A new ID is generated and the table updates.
5. Search for an item using the search box, or use the All/Lost/Found filter.
6. Select a row and click **Delete Selected** to remove it.
7. Click **Save** and choose a location for the CSV file.
8. Click **Load** and select that CSV file to restore the saved records.

## Java OOP concepts demonstrated

| Concept | Where it is used |
|---|---|
| Classes and objects | `Listing`, `LostListing`, `FoundListing` and the application window |
| Encapsulation | Private fields and public getters in `Listing` |
| Inheritance | `LostListing` and `FoundListing` extend `Listing` |
| Abstraction | `ListingRepository` interface |
| Interface implementation | `CsvListingRepository` implements `ListingRepository` |
| Method overloading | The two `searchListings` methods in the UI class |
| Static and final | Static ID counter and final application title |
| Collections | `ArrayList<Listing>` stores item records |
| File handling | CSV save and load operations |
| Exception handling | File operations and input validation |
| Event-driven programming | Swing button and search actions |
| GUI | Java Swing components and `JTable` |

## Project structure

```text
Campus-Lost-and-Found-Tracker/
├── CampusLostAndFoundTracker.jar
├── run_windows.bat
├── run_from_source.bat
├── run_mac_linux.sh
├── README.md
└── src/
    └── campus/
        ├── model/
        │   ├── Listing.java
        │   ├── LostListing.java
        │   └── FoundListing.java
        ├── storage/
        │   ├── ListingRepository.java
        │   ├── CsvListingRepository.java
        │   └── CampusLostAndFoundTracker.java
```

The app is a desktop application, not a website. It uses only standard Java and Swing; no external libraries are required.
