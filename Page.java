// File: Page.java

public class Page {
    String url;
    Page prev;
    Page next;

    public Page(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }
}
