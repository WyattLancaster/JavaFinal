package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {
    private List<Book> books; // List to store the books in the library
    private List<Member> members; // List to store the members of the library
    private Map<Member, List<Book>> borrowedBooks; // Map to store borrowed books for each member

    public Library() {
        this.books = new ArrayList<>(); // Initialize the books list as an empty ArrayList
        this.members = new ArrayList<>(); // Initialize the members list as an empty ArrayList
        this.borrowedBooks = new HashMap<>(); // Initialize the borrowedBooks map
    }
    
    public void addBook(Book book) {
        books.add(book); // Add a book to the library
    }

    public void addMember(Member member) {
        members.add(member); // Add a member to the library
    }

    public List<Book> getAvailableBooks() {
        List<Book> availableBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isAvailable()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    public List<Member> getMembers() {
        return members; // Get the list of members in the library
    }


    public boolean login(String username, String password) {
        for (Member member : members) {
            if (member.getUsername().equals(username) && member.getPassword().equals(password)) {
                return true; // Login successful
            }
        }
        return false; // Login failed
    }

    public void borrowBook(Book book, Member member) {
        if (!books.contains(book) || !members.contains(member)) {
            return; // Book or member not found in the library
        }

        if (!borrowedBooks.containsKey(member)) {
            borrowedBooks.put(member, new ArrayList<>());
        }

        List<Book> memberBorrowedBooks = borrowedBooks.get(member);
        if (!memberBorrowedBooks.contains(book) && book.isAvailable()) {
            book.setAvailable(false);
            memberBorrowedBooks.add(book);
        }
    }

    public void returnBook(Book book) {
        if (!books.contains(book)) {
            return; // Book not found in the library
        }

        for (Member member : borrowedBooks.keySet()) {
            List<Book> memberBorrowedBooks = borrowedBooks.get(member);
            if (memberBorrowedBooks.contains(book)) {
                book.setAvailable(true);
                memberBorrowedBooks.remove(book);
                return;
            }
        }
    }
    
    public Book findBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }


    public List<Book> getBorrowedBooks(Member member) {
        return borrowedBooks.getOrDefault(member, new ArrayList<>());
    }
}

