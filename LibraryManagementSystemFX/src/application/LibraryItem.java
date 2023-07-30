package application;

public abstract class LibraryItem {
    protected String title; // Title of the item

    public LibraryItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title; // Get the title of the item
    }
}