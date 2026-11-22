package campus.model;

/** A listing for an item that someone has lost. */
public class LostListing extends Listing {
    public LostListing(int id, String itemName, String location, String contact) {
        super(id, itemName, location, contact);
    }

    @Override
    public String getType() {
        return "Lost";
    }
}
