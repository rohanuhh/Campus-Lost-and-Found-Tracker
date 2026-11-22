package campus.model;

/** A listing for an item that someone has found. */
public class FoundListing extends Listing {
    public FoundListing(int id, String itemName, String location, String contact) {
        super(id, itemName, location, contact);
    }

    @Override
    public String getType() {
        return "Found";
    }
}
