// File: BookmarkManager.java

import java.util.HashMap;
import java.util.Map;

public class BookmarkManager {
    private Map<String, Page> bookmarks = new HashMap<>();

    // Adds a bookmark with a name and a reference to a Page object
    public void addBookmark(String name, Page page) {
        bookmarks.put(name, page);
        System.out.println("Bookmarked: " + name + " -> " + page.url);
    }

    // Retrieves a specific bookmark by name
    public Page getBookmark(String name) {
        return bookmarks.get(name);
    }

    // Returns the entire map of bookmarks, allowing for iteration
    public Map<String, Page> viewBookmarks() {
        return bookmarks;
    }
}
