package application;

public class Book extends LibraryItem {
    private String author; // Author of the book
    private boolean isAvailable; // Availability status of the book

    public Book(String title, String author) {
        super(title);
        this.author = author; // Initialize the author of the book
        this.isAvailable = true; // Set the book as available by default
    }

    public String getAuthor() {
        return author; // Get the author of the book
    }

    public boolean isAvailable() {
        return isAvailable; // Check the availability status of the book
    }

    public void setAvailable(boolean available) {
        isAvailable = available; // Set the availability status of the book
    }

}