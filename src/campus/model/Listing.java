package campus.model;

/** Common parent class for campus item listings. */
public abstract class Listing {
    private final int id;
    private final String itemName;
    private final String location;
    private final String contact;

    protected Listing(int id, String itemName, String location, String contact) {
        this.id = id;
        this.itemName = itemName;
        this.location = location;
        this.contact = contact;
    }

    public int getId() { return id; }
    public String getItemName() { return itemName; }
    public String getLocation() { return location; }
    public String getContact() { return contact; }
    public abstract String getType();
}
