package campus.storage;

import campus.model.FoundListing;
import campus.model.Listing;
import campus.model.LostListing;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/** CSV implementation of ListingRepository. */
public class CsvListingRepository implements ListingRepository {
    @Override
    public void save(List<Listing> listings, File file) throws IOException {
        File parent = file.getAbsoluteFile().getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Could not create the selected folder.");
        }
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            writer.write("ID,Type,Item,Location,Contact");
            writer.newLine();
            for (Listing listing : listings) {
                writer.write(listing.getId() + "," + csv(listing.getType()) + ","
                    + csv(listing.getItemName()) + "," + csv(listing.getLocation()) + ","
                    + csv(listing.getContact()));
                writer.newLine();
            }
        }
    }

    @Override
    public List<Listing> load(File file) throws IOException {
        List<Listing> loaded = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null) return loaded;
            if (!header.trim().equalsIgnoreCase("ID,Type,Item,Location,Contact")) {
                throw new IOException("The selected file is not a Campus Lost-and-Found CSV file.");
            }
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;
                List<String> columns = parseCsvLine(line);
                if (columns.size() != 5) throw new IOException("Invalid data on CSV line " + lineNumber + ".");
                try {
                    int id = Integer.parseInt(columns.get(0).trim());
                    String type = columns.get(1).trim();
                    String item = columns.get(2).trim();
                    String location = columns.get(3).trim();
                    String contact = columns.get(4).trim();
                    if (item.isEmpty() || location.isEmpty() || contact.isEmpty()) {
                        throw new IOException("An item, location or contact field is empty on line " + lineNumber + ".");
                    }
                    if (type.equalsIgnoreCase("Lost")) loaded.add(new LostListing(id, item, location, contact));
                    else if (type.equalsIgnoreCase("Found")) loaded.add(new FoundListing(id, item, location, contact));
                    else throw new IOException("Unknown listing type on CSV line " + lineNumber + ".");
                } catch (NumberFormatException ex) {
                    throw new IOException("Invalid ID on CSV line " + lineNumber + ".");
                }
            }
        }
        return loaded;
    }

    private String csv(String value) {
        String safe = value == null ? "" : value.replace("\r", " ").replace("\n", " ");
        return "\"" + safe.replace("\"", "\"\"") + "\"";
    }

    private List<String> parseCsvLine(String line) throws IOException {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (insideQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else insideQuotes = !insideQuotes;
            } else if (c == ',' && !insideQuotes) {
                values.add(current.toString());
                current.setLength(0);
            } else current.append(c);
        }
        if (insideQuotes) throw new IOException("A quoted field was not closed correctly.");
        values.add(current.toString());
        return values;
    }
}
