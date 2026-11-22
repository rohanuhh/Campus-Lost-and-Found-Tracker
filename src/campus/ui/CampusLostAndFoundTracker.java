package campus.ui;

import campus.model.FoundListing;
import campus.model.Listing;
import campus.model.LostListing;
import campus.storage.CsvListingRepository;
import campus.storage.ListingRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Main window for the Campus Lost-and-Found Tracker.
 * Built with Java Swing and event-driven button actions.
 */
public class CampusLostAndFoundTracker extends JFrame {

    private static final String APP_TITLE = "Campus Lost-and-Found Tracker";
    private static final Color NAVY = new Color(25, 55, 91);
    private static final Color BLUE = new Color(42, 105, 170);
    private static final Color BACKGROUND = new Color(242, 246, 250);
    private static final Color BORDER = new Color(220, 228, 236);
    private static final Color TEXT = new Color(40, 52, 66);
    private static final Color MUTED = new Color(111, 125, 140);
    private static int nextId = 1001;

    private final List<Listing> listings = new ArrayList<>();
    private final ListingRepository repository = new CsvListingRepository();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Type", "Item", "Location", "Contact"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JTable listingsTable;
    private JComboBox<String> typeCombo;
    private JComboBox<String> typeFilter;
    private JTextField itemField;
    private JTextField locationField;
    private JTextField contactField;
    private JTextField searchField;

    private JLabel totalValue;
    private JLabel lostValue;
    private JLabel foundValue;
    private JLabel visibleCountLabel;
    private JLabel statusLabel;

    public CampusLostAndFoundTracker() {
        super(APP_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 680));
        setSize(1220, 790);
        setLocationRelativeTo(null);

        loadDemoListings();
        buildInterface();
        refreshTable();
        updateStatus("Ready. Add a listing or search the sample records.");
    }

    private void buildInterface() {
        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(22, 28, 22, 28));

        JPanel titleArea = new JPanel();
        titleArea.setOpaque(false);
        titleArea.setLayout(new BoxLayout(titleArea, BoxLayout.Y_AXIS));

        JLabel eyebrow = new JLabel("CAMPUS UTILITY  /  JAVA MINI PROJECT");
        eyebrow.setFont(new Font("Segoe UI", Font.BOLD, 11));
        eyebrow.setForeground(new Color(177, 205, 232));

        JLabel title = new JLabel(APP_TITLE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
                "Report lost items, find reported belongings and connect with the person who listed them.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(220, 231, 242));

        titleArea.add(eyebrow);
        titleArea.add(Box.createVerticalStrut(5));
        titleArea.add(title);
        titleArea.add(Box.createVerticalStrut(5));
        titleArea.add(subtitle);

        JLabel version = new JLabel("JAVA SWING");
        version.setFont(new Font("Segoe UI", Font.BOLD, 11));
        version.setForeground(Color.WHITE);
        version.setBorder(new LineBorder(new Color(105, 139, 174), 1, true));
        version.setOpaque(true);
        version.setBackground(new Color(38, 76, 116));
        version.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(105, 139, 174), 1, true),
                new EmptyBorder(8, 13, 8, 13)));

        header.add(titleArea, BorderLayout.CENTER);
        header.add(version, BorderLayout.EAST);
        return header;
    }

    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout(0, 16));
        main.setBackground(BACKGROUND);
        main.setBorder(new EmptyBorder(18, 22, 16, 22));

        main.add(createStatsPanel(), BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                createFormPanel(),
                createListingsPanel());
        splitPane.setResizeWeight(0.29);
        splitPane.setDividerLocation(330);
        splitPane.setDividerSize(10);
        splitPane.setBorder(null);
        splitPane.setOpaque(false);

        main.add(splitPane, BorderLayout.CENTER);
        return main;
    }

    private JPanel createStatsPanel() {
        JPanel stats = new JPanel(new GridLayout(1, 3, 14, 0));
        stats.setOpaque(false);

        totalValue = new JLabel("0");
        lostValue = new JLabel("0");
        foundValue = new JLabel("0");

        stats.add(createStatCard("TOTAL LISTINGS", totalValue, "All campus reports"));
        stats.add(createStatCard("LOST ITEMS", lostValue, "Items reported missing"));
        stats.add(createStatCard("FOUND ITEMS", foundValue, "Items reported found"));

        return stats;
    }

    private JPanel createStatCard(String heading, JLabel valueLabel, String description) {
        JPanel card = new JPanel(new BorderLayout(0, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel headingLabel = new JLabel(heading);
        headingLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        headingLabel.setForeground(MUTED);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        valueLabel.setForeground(NAVY);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descriptionLabel.setForeground(MUTED);

        card.add(headingLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(descriptionLabel, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createFormPanel() {
        JPanel form = new JPanel();
        form.setBackground(Color.WHITE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(18, 18, 18, 18)));
        form.setPreferredSize(new Dimension(320, 400));

        JLabel heading = new JLabel("Add a listing");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 19));
        heading.setForeground(TEXT);

        JLabel helper = new JLabel("Enter the details of the lost or found item.");
        helper.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        helper.setForeground(MUTED);

        form.add(heading);
        form.add(Box.createVerticalStrut(5));
        form.add(helper);
        form.add(Box.createVerticalStrut(22));

        typeCombo = new JComboBox<>(new String[]{"Lost", "Found"});
        itemField = createTextField("e.g. Scientific Calculator");
        locationField = createTextField("e.g. Library, 2nd Floor");
        contactField = createTextField("e.g. name@campus.edu");

        addFormField(form, "ITEM TYPE", typeCombo);
        addFormField(form, "ITEM NAME", itemField);
        addFormField(form, "LOCATION", locationField);
        addFormField(form, "CONTACT DETAILS", contactField);

        form.add(Box.createVerticalStrut(8));

        JButton addButton = createButton("Add Listing", BLUE, Color.WHITE);
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        addButton.addActionListener(event -> addListing());

        JButton clearButton = createButton("Clear Fields", new Color(235, 240, 245), TEXT);
        clearButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        clearButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        clearButton.addActionListener(event -> clearForm());

        form.add(addButton);
        form.add(Box.createVerticalStrut(9));
        form.add(clearButton);
        form.add(Box.createVerticalGlue());

        JLabel note = new JLabel("<html><b>Note:</b> All four fields are required.</html>");
        note.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        note.setForeground(MUTED);
        note.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(note);

        return form;
    }

    private JPanel createListingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(17, 17, 15, 17)));

        JPanel headingArea = new JPanel(new BorderLayout());
        headingArea.setOpaque(false);

        JLabel heading = new JLabel("Campus item listings");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 19));
        heading.setForeground(TEXT);

        visibleCountLabel = new JLabel("0 records");
        visibleCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        visibleCountLabel.setForeground(MUTED);

        headingArea.add(heading, BorderLayout.WEST);
        headingArea.add(visibleCountLabel, BorderLayout.EAST);

        JPanel controls = new JPanel(new BorderLayout(8, 8));
        controls.setOpaque(false);

        JPanel searchControls = new JPanel(new BorderLayout(7, 0));
        searchControls.setOpaque(false);

        searchField = createTextField("");
        searchField.setToolTipText("Search by item, location, contact, type or ID");
        searchField.addActionListener(event -> refreshTable());

        JButton searchButton = createButton("Search", BLUE, Color.WHITE);
        searchButton.addActionListener(event -> {
            refreshTable();
            updateStatus("Search completed.");
        });

        JButton clearSearchButton = createButton("Clear", new Color(235, 240, 245), TEXT);
        clearSearchButton.addActionListener(event -> {
            searchField.setText("");
            typeFilter.setSelectedItem("All");
            refreshTable();
            updateStatus("Search filters cleared.");
        });

        searchControls.add(searchField, BorderLayout.CENTER);

        JPanel searchButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        searchButtons.setOpaque(false);
        searchButtons.add(searchButton);
        searchButtons.add(clearSearchButton);
        searchControls.add(searchButtons, BorderLayout.EAST);

        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filterRow.setOpaque(false);

        JLabel filterLabel = new JLabel("Show: ");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filterLabel.setForeground(MUTED);

        typeFilter = new JComboBox<>(new String[]{"All", "Lost", "Found"});
        typeFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        typeFilter.setPreferredSize(new Dimension(110, 31));
        typeFilter.addActionListener(event -> refreshTable());

        filterRow.add(filterLabel);
        filterRow.add(typeFilter);

        controls.add(searchControls, BorderLayout.NORTH);
        controls.add(filterRow, BorderLayout.SOUTH);

        listingsTable = new JTable(tableModel);
        listingsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        listingsTable.setForeground(TEXT);
        listingsTable.setRowHeight(35);
        listingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listingsTable.setShowVerticalLines(false);
        listingsTable.setShowHorizontalLines(true);
        listingsTable.setGridColor(new Color(232, 237, 242));
        listingsTable.setFillsViewportHeight(true);
        listingsTable.setAutoCreateRowSorter(true);
        listingsTable.setSelectionBackground(new Color(220, 235, 249));
        listingsTable.setSelectionForeground(TEXT);

        JTableHeader tableHeader = listingsTable.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setPreferredSize(new Dimension(0, 38));
        tableHeader.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean selected,
                    boolean focused, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, selected, focused, row, column);
                label.setOpaque(true);
                label.setBackground(new Color(235, 241, 247));
                label.setForeground(NAVY);
                label.setFont(new Font("Segoe UI", Font.BOLD, 11));
                label.setBorder(new EmptyBorder(0, 10, 0, 8));
                return label;
            }
        });

        listingsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean selected,
                    boolean focused, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, selected, focused, row, column);
                label.setBorder(new EmptyBorder(0, 10, 0, 8));
                label.setForeground(TEXT);
                if (selected) {
                    label.setBackground(new Color(220, 235, 249));
                } else {
                    label.setBackground(row % 2 == 0
                            ? Color.WHITE : new Color(248, 250, 252));
                }
                return label;
            }
        });

        listingsTable.getColumnModel().getColumn(0).setPreferredWidth(48);
        listingsTable.getColumnModel().getColumn(1).setPreferredWidth(62);
        listingsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        listingsTable.getColumnModel().getColumn(3).setPreferredWidth(145);
        listingsTable.getColumnModel().getColumn(4).setPreferredWidth(165);

        JScrollPane scrollPane = new JScrollPane(listingsTable);
        scrollPane.setBorder(new LineBorder(BORDER, 1, true));
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 0));
        actionRow.setOpaque(false);

        JButton deleteButton = createButton("Delete Selected", new Color(190, 69, 69), Color.WHITE);
        deleteButton.addActionListener(event -> deleteSelected());

        JButton saveButton = createButton("Save", new Color(42, 125, 91), Color.WHITE);
        saveButton.addActionListener(event -> saveListings());

        JButton loadButton = createButton("Load", new Color(235, 240, 245), TEXT);
        loadButton.addActionListener(event -> loadListings());

        actionRow.add(deleteButton);
        actionRow.add(saveButton);
        actionRow.add(loadButton);

        panel.add(headingArea, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(controls, BorderLayout.NORTH);
        center.add(scrollPane, BorderLayout.CENTER);

        panel.add(center, BorderLayout.CENTER);
        panel.add(actionRow, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(232, 238, 244));
        footer.setBorder(new EmptyBorder(8, 24, 8, 24));

        statusLabel = new JLabel("Ready.");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(MUTED);

        JLabel footerText = new JLabel("Campus Lost-and-Found Tracker  •  OOP Using Java");
        footerText.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerText.setForeground(MUTED);

        footer.add(statusLabel, BorderLayout.WEST);
        footer.add(footerText, BorderLayout.EAST);
        return footer;
    }

    private void addFormField(JPanel container, String labelText, JComponent input) {
        JPanel field = new JPanel(new BorderLayout(0, 6));
        field.setOpaque(false);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 10));
        label.setForeground(MUTED);

        input.setPreferredSize(new Dimension(100, 38));

        field.add(label, BorderLayout.NORTH);
        field.add(input, BorderLayout.CENTER);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        container.add(field);
        container.add(Box.createVerticalStrut(12));
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setForeground(TEXT);
        field.setBackground(Color.WHITE);
        field.setToolTipText(placeholder);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        return field;
    }

    private JButton createButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 14, 10, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void addListing() {
        String type = String.valueOf(typeCombo.getSelectedItem());
        String item = clean(itemField.getText());
        String location = clean(locationField.getText());
        String contact = clean(contactField.getText());

        if (item.isEmpty() || location.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the item name, location and contact details.",
                    "Missing information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Listing listing;
        if ("Lost".equalsIgnoreCase(type)) {
            listing = new LostListing(nextId++, item, location, contact);
        } else {
            listing = new FoundListing(nextId++, item, location, contact);
        }

        listings.add(listing);
        clearForm();
        refreshTable();
        updateStatus("Added " + type.toLowerCase(Locale.ROOT) + " item: " + item);
    }

    private void clearForm() {
        itemField.setText("");
        locationField.setText("");
        contactField.setText("");
        typeCombo.setSelectedItem("Lost");
        itemField.requestFocusInWindow();
    }

    private void deleteSelected() {
        int selectedRow = listingsTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Select a listing in the table first.",
                    "No listing selected",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int modelRow = listingsTable.convertRowIndexToModel(selectedRow);
        int id = Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete listing ID " + id + "?",
                "Confirm deletion",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            listings.removeIf(listing -> listing.getId() == id);
            refreshTable();
            updateStatus("Deleted listing ID " + id + ".");
        }
    }

    private void saveListings() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save campus item listings");
        chooser.setFileFilter(new FileNameExtensionFilter("CSV files (*.csv)", "csv"));
        chooser.setSelectedFile(new File("campus_lost_found_listings.csv"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = addCsvExtension(chooser.getSelectedFile());

        if (file.exists()) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "This file already exists. Replace it?",
                    "Confirm save",
                    JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            repository.save(listings, file);
            updateStatus("Saved " + listings.size() + " listings to " + file.getName() + ".");
            JOptionPane.showMessageDialog(
                    this,
                    "Your listings were saved successfully.\n\n" + file.getAbsolutePath(),
                    "Save complete",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException exception) {
            showError("Could not save the listings.", exception);
        }
    }

    private void loadListings() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load campus item listings");
        chooser.setFileFilter(new FileNameExtensionFilter("CSV files (*.csv)", "csv"));

        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        try {
            List<Listing> loaded = repository.load(file);
            listings.clear();
            listings.addAll(loaded);
            updateNextId();
            refreshTable();
            updateStatus("Loaded " + listings.size() + " listings from " + file.getName() + ".");
            JOptionPane.showMessageDialog(
                    this,
                    "Loaded " + listings.size() + " listings successfully.",
                    "Load complete",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException exception) {
            showError("Could not load the selected file.", exception);
        }
    }

    private void showError(String message, Exception exception) {
        JOptionPane.showMessageDialog(
                this,
                message + "\n\n" + exception.getMessage(),
                "File operation failed",
                JOptionPane.ERROR_MESSAGE);
        updateStatus("The file operation could not be completed.");
    }

    private File addCsvExtension(File file) {
        if (file.getName().toLowerCase(Locale.ROOT).endsWith(".csv")) {
            return file;
        }
        return new File(file.getParentFile(), file.getName() + ".csv");
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        String selectedType = typeFilter == null
                ? "All" : String.valueOf(typeFilter.getSelectedItem());

        List<Listing> results = searchListings(searchField == null ? "" : searchField.getText());

        for (Listing listing : results) {
            if (!"All".equalsIgnoreCase(selectedType)
                    && !listing.getType().equalsIgnoreCase(selectedType)) {
                continue;
            }

            tableModel.addRow(new Object[]{
                    listing.getId(),
                    listing.getType(),
                    listing.getItemName(),
                    listing.getLocation(),
                    listing.getContact()
            });
        }

        updateStatistics();
        if (visibleCountLabel != null) {
            int count = tableModel.getRowCount();
            visibleCountLabel.setText(count + (count == 1 ? " record" : " records"));
        }
    }

    /**
     * Overloaded search method for a general search across all listing fields.
     */
    private List<Listing> searchListings(String keyword) {
        return searchListings(keyword, "");
    }

    /**
     * Overloaded search method that can also apply a separate location condition.
     */
    private List<Listing> searchListings(String keyword, String locationQuery) {
        List<Listing> results = new ArrayList<>();
        String query = clean(keyword).toLowerCase(Locale.ROOT);
        String location = clean(locationQuery).toLowerCase(Locale.ROOT);

        for (Listing listing : listings) {
            String searchable = (listing.getId() + " " + listing.getType() + " "
                    + listing.getItemName() + " " + listing.getLocation() + " "
                    + listing.getContact()).toLowerCase(Locale.ROOT);

            boolean generalMatch = query.isEmpty() || searchable.contains(query);
            boolean locationMatch = location.isEmpty()
                    || listing.getLocation().toLowerCase(Locale.ROOT).contains(location);

            if (generalMatch && locationMatch) {
                results.add(listing);
            }
        }

        return results;
    }

    private void updateStatistics() {
        int lost = 0;
        int found = 0;

        for (Listing listing : listings) {
            if ("Lost".equalsIgnoreCase(listing.getType())) {
                lost++;
            } else if ("Found".equalsIgnoreCase(listing.getType())) {
                found++;
            }
        }

        totalValue.setText(String.valueOf(listings.size()));
        lostValue.setText(String.valueOf(lost));
        foundValue.setText(String.valueOf(found));
    }

    private void loadDemoListings() {
        listings.add(new LostListing(
                1001, "Scientific Calculator", "Library 2nd Floor", "alex@campus.edu"));
        listings.add(new FoundListing(
                1002, "Blue Water Bottle", "Cafeteria Table 4", "security@campus.edu"));
        listings.add(new LostListing(
                1003, "Room Keys with Red Lanyard", "Sports Complex", "jordan@campus.edu"));
        listings.add(new FoundListing(
                1004, "Wireless Earbuds Case", "Lab Block B - 302", "techlab@campus.edu"));
        updateNextId();
    }

    private void updateNextId() {
        int maximumId = 1000;
        for (Listing listing : listings) {
            maximumId = Math.max(maximumId, listing.getId());
        }
        nextId = maximumId + 1;
    }

    private String clean(String value) {
        if (value == null) {
            return "";
        }
        return value.replace('\n', ' ').replace('\r', ' ').trim();
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Swing will use its default look and feel if the system one is unavailable.
            }

            new CampusLostAndFoundTracker().setVisible(true);
        });
    }
}
