// File: BrowserHistoryManager.java

import java.util.Scanner;

public class BrowserHistoryManager {
    public static void main(String[] args) {
        History history = new History();
        BookmarkManager bookmarkManager = new BookmarkManager();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("Browser History Manager");

        while (running) {
            System.out.println("\nOptions:");
            System.out.println("1. Visit New Page");
            System.out.println("2. Go Back");
            System.out.println("3. Go Forward");
            System.out.println("4. View History");
            System.out.println("5. Clear History");
            System.out.println("6. Add Bookmark");
            System.out.println("7. View Bookmarks");
            System.out.println("8. Go to Bookmark");
            System.out.println("9. Search History");
            System.out.println("10. Undo Navigation");
            System.out.println("11. Redo Navigation");
            System.out.println("12. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter URL to visit: ");
                    String url = scanner.nextLine();
                    history.visitPage(url);
                    break;
                case 2:
                    history.goBack();
                    break;
                case 3:
                    history.goForward();
                    break;
                case 4:
                    history.printHistory();
                    break;
                case 5:
                    history.clearHistory();
                    break;
                case 6:
                    if (history.getCurrentPage() != null) {
                        System.out.print("Enter bookmark name: ");
                        String name = scanner.nextLine();
                        bookmarkManager.addBookmark(name, history.getCurrentPage());
                    } else {
                        System.out.println("No page to bookmark.");
                    }
                    break;
                case 7:
                    bookmarkManager.viewBookmarks();
                    break;
                case 8:
                    System.out.print("Enter bookmark name: ");
                    String bookmarkName = scanner.nextLine();
                    Page bookmarkedPage = bookmarkManager.getBookmark(bookmarkName);
                    if (bookmarkedPage != null) {
                        history.visitPage(bookmarkedPage.url);
                    } else {
                        System.out.println("Bookmark not found.");
                    }
                    break;
                case 9:
                    System.out.print("Enter URL to search in history: ");
                    String searchUrl = scanner.nextLine();
                    history.searchHistory(searchUrl);
                    break;
                case 10:
                    history.undoNavigation();
                    break;
                case 11:
                    history.redoNavigation();
                    break;
                case 12:
                    running = false;
                    System.out.println("Exiting Browser History Manager.");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        }
        scanner.close();
    }
}
