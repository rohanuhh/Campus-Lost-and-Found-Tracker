package campus.storage;

import campus.model.Listing;
import java.io.File;
import java.io.IOException;
import java.util.List;

/** Abstraction for saving and loading item listings. */
public interface ListingRepository {
    void save(List<Listing> listings, File file) throws IOException;
    List<Listing> load(File file) throws IOException;
}
