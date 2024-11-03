// File: BrowserHistoryManagerUI.java

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BrowserHistoryManagerUI extends JFrame {
    private History history = new History();
    private BookmarkManager bookmarkManager = new BookmarkManager();

    private JTextField urlField = new JTextField(25);
    private DefaultListModel<String> historyListModel = new DefaultListModel<>();
    private JList<String> historyList = new JList<>(historyListModel);

    public BrowserHistoryManagerUI() {
        setTitle("Browser History Manager");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Add panels to the main frame
        add(createControlsPanel(), BorderLayout.NORTH);
        add(createDisplayPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    // Creates the control panel containing URL input, navigation, and action buttons
    private JPanel createControlsPanel() {
        JPanel controlsPanel = new JPanel(new BorderLayout());
        
        // URL entry area
        JPanel urlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        urlField.setPreferredSize(new Dimension(300, 30));
        JButton visitButton = new JButton("Visit Page");
        visitButton.setPreferredSize(new Dimension(100, 30));
        visitButton.addActionListener(e -> visitPage());
        urlPanel.add(new JLabel("URL: "));
        urlPanel.add(urlField);
        urlPanel.add(visitButton);

        // Navigation buttons
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        Dimension buttonSize = new Dimension(130, 35);
        
        JButton backButton = new JButton("Back");
        JButton forwardButton = new JButton("Forward");
        JButton undoButton = new JButton("Undo Navigation");
        JButton redoButton = new JButton("Redo Navigation");
        JButton clearHistoryButton = new JButton("Clear History");
        
        // Set button sizes and add listeners
        for (JButton button : new JButton[]{backButton, forwardButton, undoButton, redoButton, clearHistoryButton}) {
            button.setPreferredSize(buttonSize);
        }
        backButton.addActionListener(e -> goBack());
        forwardButton.addActionListener(e -> goForward());
        undoButton.addActionListener(e -> undoNavigation());
        redoButton.addActionListener(e -> redoNavigation());
        clearHistoryButton.addActionListener(e -> clearHistory());

        navPanel.add(backButton);
        navPanel.add(forwardButton);
        navPanel.add(undoButton);
        navPanel.add(redoButton);
        navPanel.add(clearHistoryButton);

        // Action buttons (bookmarks and history)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        JButton addBookmarkButton = new JButton("Add Bookmark");
        JButton viewBookmarksButton = new JButton("View Bookmarks");
        JButton goToBookmarkButton = new JButton("Go to Bookmark");
        JButton viewHistoryButton = new JButton("View History");
        JButton searchHistoryButton = new JButton("Search History");

        for (JButton button : new JButton[]{addBookmarkButton, viewBookmarksButton, goToBookmarkButton, viewHistoryButton, searchHistoryButton}) {
            button.setPreferredSize(buttonSize);
        }
        addBookmarkButton.addActionListener(e -> addBookmark());
        viewBookmarksButton.addActionListener(e -> viewBookmarks());
        goToBookmarkButton.addActionListener(e -> goToBookmark());
        viewHistoryButton.addActionListener(e -> viewHistory());
        searchHistoryButton.addActionListener(e -> searchHistory());

        actionPanel.add(addBookmarkButton);
        actionPanel.add(viewBookmarksButton);
        actionPanel.add(goToBookmarkButton);
        actionPanel.add(viewHistoryButton);
        actionPanel.add(searchHistoryButton);

        // Assemble controls panel
        controlsPanel.add(urlPanel, BorderLayout.NORTH);
        controlsPanel.add(navPanel, BorderLayout.CENTER);
        controlsPanel.add(actionPanel, BorderLayout.SOUTH);
        
        return controlsPanel;
    }

    // Creates the display panel containing the history list
    private JPanel createDisplayPanel() {
        JPanel displayPanel = new JPanel(new BorderLayout());
        historyList.setVisibleRowCount(12);
        historyList.setFixedCellHeight(30);
        JScrollPane historyScrollPane = new JScrollPane(historyList);
        displayPanel.add(new JLabel("Browsing History:"), BorderLayout.NORTH);
        displayPanel.add(historyScrollPane, BorderLayout.CENTER);
        
        return displayPanel;
    }

    // Visit a new page
    private void visitPage() {
        String url = urlField.getText().trim();
        if (!url.isEmpty()) {
            history.visitPage(url);
            updateHistoryView();
            urlField.setText("");
        }
    }

    // Go back
    private void goBack() {
        history.goBack();
        updateHistoryView();
    }

    // Go forward
    private void goForward() {
        history.goForward();
        updateHistoryView();
    }

    // Undo navigation
    private void undoNavigation() {
        history.undoNavigation();
        updateHistoryView();
    }

    // Redo navigation
    private void redoNavigation() {
        history.redoNavigation();
        updateHistoryView();
    }

    // Add current page as a bookmark
    private void addBookmark() {
        Page currentPage = history.getCurrentPage();
        if (currentPage != null) {
            String bookmarkName = JOptionPane.showInputDialog(this, "Enter bookmark name:");
            if (bookmarkName != null && !bookmarkName.trim().isEmpty()) {
                bookmarkManager.addBookmark(bookmarkName, currentPage);
            }
        }
    }

    // View bookmarks in a dialog
    private void viewBookmarks() {
        StringBuilder bookmarksText = new StringBuilder("Bookmarks:\n");
        for (Map.Entry<String, Page> entry : bookmarkManager.viewBookmarks().entrySet()) {
            bookmarksText.append(entry.getKey()).append(": ").append(entry.getValue().url).append("\n");
        }
        JOptionPane.showMessageDialog(this, bookmarksText.toString(), "Bookmarks", JOptionPane.INFORMATION_MESSAGE);
    }

    // Go to a specific bookmark by name
    private void goToBookmark() {
        String bookmarkName = JOptionPane.showInputDialog(this, "Enter bookmark name:");
        if (bookmarkName != null && !bookmarkName.trim().isEmpty()) {
            Page bookmarkedPage = bookmarkManager.getBookmark(bookmarkName);
            if (bookmarkedPage != null) {
                history.visitPage(bookmarkedPage.url);
                updateHistoryView();
            } else {
                JOptionPane.showMessageDialog(this, "Bookmark not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // View the browsing history in the list view
    private void viewHistory() {
        updateHistoryView();
    }

    // Clear the browsing history
    private void clearHistory() {
        history.clearHistory();
        updateHistoryView();
    }

    // Search history by URL
    private void searchHistory() {
        String searchUrl = JOptionPane.showInputDialog(this, "Enter URL to search:");
        if (searchUrl != null && !searchUrl.trim().isEmpty()) {
            boolean found = history.searchHistory(searchUrl);
            if (found) {
                JOptionPane.showMessageDialog(this, "URL found in history: " + searchUrl, "Search Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "URL not found in history.", "Search Result", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // Update the history view in the list model
    private void updateHistoryView() {
        historyListModel.clear();
        Page firstPage = history.getCurrentPage();
        while (firstPage != null && firstPage.prev != null) {
            firstPage = firstPage.prev;
        }
        while (firstPage != null) {
            String pageText = firstPage.url;
            if (firstPage == history.getCurrentPage()) {
                pageText += " <- Current Page";
            }
            historyListModel.addElement(pageText);
            firstPage = firstPage.next;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BrowserHistoryManagerUI::new);
    }
}
