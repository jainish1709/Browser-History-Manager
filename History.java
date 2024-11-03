// File: History.java

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class History {
    private Page currentPage;
    private Stack<Page> undoStack = new Stack<>();
    private Stack<Page> redoStack = new Stack<>();

    // Visit a new page and properly link it in the history
    public void visitPage(String url) {
        Page newPage = new Page(url);

        // Link the new page to the current page
        if (currentPage != null) {
            currentPage.next = newPage;
            newPage.prev = currentPage;
            undoStack.push(currentPage);  // Push the current page to the undo stack
            redoStack.clear();            // Clear the redo stack, as forward history is invalidated
        }

        // Update the current page to the new page
        currentPage = newPage;
        System.out.println("Visited: " + url);
    }

    // Go back
    public void goBack() {
        if (currentPage == null || currentPage.prev == null) {
            System.out.println("No previous page to go back to.");
            return;
        }
        redoStack.push(currentPage);  // Push the current page to the redo stack
        currentPage = currentPage.prev;
        System.out.println("Went back to: " + currentPage.url);
    }

    // Go forward
    public void goForward() {
        if (currentPage == null || currentPage.next == null) {
            System.out.println("No forward page to go to.");
            return;
        }
        undoStack.push(currentPage);  // Push the current page to the undo stack
        currentPage = currentPage.next;
        System.out.println("Went forward to: " + currentPage.url);
    }

    // Undo navigation
    public void undoNavigation() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        redoStack.push(currentPage);
        currentPage = undoStack.pop();
    }

    // Redo navigation
    public void redoNavigation() {
        if (redoStack.isEmpty()) {
            System.out.println("Nothing to redo.");
            return;
        }
        undoStack.push(currentPage);
        currentPage = redoStack.pop();
    }

    // Clear history
    public void clearHistory() {
        currentPage = null;
        undoStack.clear();
        redoStack.clear();
        System.out.println("History cleared.");
    }

    // Display browsing history as a list of strings
    public List<String> viewHistory() {
        List<String> historyList = new ArrayList<>();

        // Start from the very first page in the history
        Page firstPage = currentPage;
        while (firstPage != null && firstPage.prev != null) {
            firstPage = firstPage.prev;
        }

        // Traverse from the first page to the end
        while (firstPage != null) {
            String pageText = firstPage.url;
            if (firstPage == currentPage) {
                pageText += " <- Current Page";
            }
            historyList.add(pageText);
            firstPage = firstPage.next;
        }

        return historyList;
    }

    // Print history to console
    public void printHistory() {
        List<String> historyList = viewHistory();
        System.out.println("Browsing History:");
        for (String pageText : historyList) {
            System.out.println(pageText);
        }
    }

    // Search history for a specific URL and return true if found, false otherwise
    public boolean searchHistory(String url) {
        Page temp = currentPage;
        // Navigate to the first page in the history
        while (temp != null && temp.prev != null) {
            temp = temp.prev;
        }
        // Iterate through the history list
        while (temp != null) {
            if (temp.url.equals(url)) {
                System.out.println("Page found: " + temp.url);
                return true;
            }
            temp = temp.next;
        }
        System.out.println("Page not found: " + url);
        return false;
    }

    // Get the current page
    public Page getCurrentPage() {
        return currentPage;
    }
}
