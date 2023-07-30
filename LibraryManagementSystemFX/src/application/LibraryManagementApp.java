package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class LibraryManagementApp extends Application {

    private Library library = new Library();
    private Member loggedInMember;

    private Button displayBooksButton;
    private Button borrowBookButton;
    private Button returnBookButton;
    private Button addBookButton;
    private Button loginButton;
    private Button signUpButton;
    private Button logoutButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Library Management");

        // Initialize UI components
        displayBooksButton = new Button("Display Available Books");
        displayBooksButton.setOnAction(e -> displayAvailableBooks());

        borrowBookButton = new Button("Borrow Book");
        borrowBookButton.setOnAction(e -> borrowBook());

        returnBookButton = new Button("Return Book");
        returnBookButton.setOnAction(e -> returnBook());

        addBookButton = new Button("Add Book");
        addBookButton.setOnAction(e -> addBook());

        loginButton = new Button("Login");
        loginButton.setOnAction(e -> login(primaryStage)); // Pass the primaryStage to the login method

        signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(e -> signUp(primaryStage)); // Pass the primaryStage to the signUp method

        logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout(primaryStage));
        logoutButton.setVisible(false); // Initially, the logout button is hidden until the user logs in.

        // Create the initial layout with login and signup buttons
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(loginButton, signUpButton);

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to display the available books in the library
    private void displayAvailableBooks() {
        List<Book> availableBooks = library.getAvailableBooks();
        if (availableBooks.isEmpty()) {
            showAlert("No available books in the library.");
        } else {
            StringBuilder message = new StringBuilder("Available Books:\n");
            for (Book book : availableBooks) {
                message.append(book.getTitle()).append(" by ").append(book.getAuthor()).append("\n");
            }
            showAlert(message.toString());
        }
    }

    // Method to handle book borrowing
    private void borrowBook() {
        if (loggedInMember == null) {
            showAlert("Please login to borrow books.");
            return;
        }

        // Show a dialog to let the user select a book from the available books
        List<Book> availableBooks = library.getAvailableBooks();
        if (availableBooks.isEmpty()) {
            showAlert("No available books in the library.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Borrow Book");
        dialog.setHeaderText("Enter Book Title:");
        dialog.setContentText("Title:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String title = result.get();
            boolean bookFound = false;

            for (Book book : availableBooks) {
                if (book.getTitle().equalsIgnoreCase(title)) {
                    bookFound = true;
                    library.borrowBook(book, loggedInMember);
                    break;
                }
            }

            if (!bookFound) {
                showAlert("The book " + title + " is not available for borrowing.");
            }
        } else {
            showAlert("Borrowing canceled. Please enter the book title to borrow.");
        }
    }

    // Method to handle book return
    private void returnBook() {
        if (loggedInMember == null) {
            showAlert("Please login to return books.");
            return;
        }

        List<Book> borrowedBooks = library.getBorrowedBooks(loggedInMember);
        if (borrowedBooks.isEmpty()) {
            showAlert("You have no books to return.");
            return;
        }

        List<String> bookTitles = new ArrayList<>();
        for (Book book : borrowedBooks) {
            bookTitles.add(book.getTitle());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(bookTitles.get(0), bookTitles);
        dialog.setTitle("Return Book");
        dialog.setHeaderText("Select Book to Return:");
        dialog.setContentText("Book:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String title = result.get();
            Book selectedBook = library.findBookByTitle(title);
            if (selectedBook != null) {
                library.returnBook(selectedBook);
                showAlert("You have returned the book " + title);
            }
        } else {
            showAlert("Returning canceled.");
        }
    }

    // Method to add a book to the library
    private void addBook() {
        TextInputDialog titleDialog = new TextInputDialog();
        titleDialog.setTitle("Add Book");
        titleDialog.setHeaderText("Enter Book Title:");
        titleDialog.setContentText("Title:");

        TextInputDialog authorDialog = new TextInputDialog();
        authorDialog.setTitle("Add Book");
        authorDialog.setHeaderText("Enter Book Author:");
        authorDialog.setContentText("Author:");

        // Show the dialogs and get user input
        Optional<String> titleResult = titleDialog.showAndWait();
        Optional<String> authorResult = authorDialog.showAndWait();

        // Check if both title and author are provided
        if (titleResult.isPresent() && authorResult.isPresent()) {
            String title = titleResult.get();
            String author = authorResult.get();

            // Create a new book and add it to the library
            Book newBook = new Book(title, author);
            library.addBook(newBook);
            showAlert("Book added successfully: " + title + " by " + author);
        } else {
            showAlert("Book addition canceled. Please provide both title and author.");
        }
    }

    // Method to handle member login
    private void login(Stage primaryStage) {
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setTitle("Member Login");
        usernameDialog.setHeaderText("Enter Username:");
        usernameDialog.setContentText("Username:");

        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Member Login");
        passwordDialog.setHeaderText("Enter Password:");
        passwordDialog.setContentText("Password:");

        // Show the dialogs and get user input
        Optional<String> usernameResult = usernameDialog.showAndWait();
        Optional<String> passwordResult = passwordDialog.showAndWait();

        // Check if both username and password are provided
        if (usernameResult.isPresent() && passwordResult.isPresent()) {
            String username = usernameResult.get();
            String password = passwordResult.get();

            // Check if the provided login credentials are valid
            if (library.login(username, password)) {
                // Login successful
                loggedInMember = findMemberByUsername(username);
                showAlert("Login successful. Welcome, " + loggedInMember.getName() + "!");
                updateLayoutAfterLogin(primaryStage);

            } else {
                // Login failed
                showAlert("Invalid login credentials. Please try again.");
            }
        } else {
            showAlert("Login canceled. Please provide both username and password.");
        }
    }

    // Method to update the layout after a successful login
    private void updateLayoutAfterLogin(Stage primaryStage) {
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                displayBooksButton,
                borrowBookButton,
                returnBookButton,
                addBookButton,
                logoutButton // Add the "Logout" button to allow users to log out
        );

        // Show the logout button and hide the login and signup buttons
        loginButton.setVisible(false);
        signUpButton.setVisible(false);
        logoutButton.setVisible(true);

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
    }

    // Method to update the layout after logout
    private void updateLayoutAfterLogout(Stage primaryStage) {
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(loginButton, signUpButton);

        // Show the login and signup buttons and hide the logout button
        loginButton.setVisible(true);
        signUpButton.setVisible(true);
        logoutButton.setVisible(false);

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
    }

    // Method to find a member by their username
    private Member findMemberByUsername(String username) {
        for (Member member : library.getMembers()) {
            if (member.getUsername().equals(username)) {
                return member;
            }
        }
        return null;
    }

    // Helper method to show an information alert
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Library Management");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to handle member sign-up
    private void signUp(Stage primaryStage) {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Sign Up");
        nameDialog.setHeaderText("Enter Your Name:");
        nameDialog.setContentText("Name:");

        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setTitle("Sign Up");
        usernameDialog.setHeaderText("Choose a Username:");
        usernameDialog.setContentText("Username:");

        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Sign Up");
        passwordDialog.setHeaderText("Choose a Password:");
        passwordDialog.setContentText("Password:");

        // Show the dialogs and get user input
        Optional<String> nameResult = nameDialog.showAndWait();
        Optional<String> usernameResult = usernameDialog.showAndWait();
        Optional<String> passwordResult = passwordDialog.showAndWait();

        // Check if all details are provided
        if (nameResult.isPresent() && usernameResult.isPresent() && passwordResult.isPresent()) {
            String name = nameResult.get();
            String username = usernameResult.get();
            String password = passwordResult.get();

            // Check if the username is already taken
            if (isUsernameTaken(username)) {
                showAlert("Username is already taken. Please choose a different username.");
            } else {
                // Create a new member and add them to the library
                Member newMember = new Member(name, generateMemberId(), username, password);
                library.addMember(newMember);
                // Log in the newly signed-up member
                loggedInMember = newMember;
                showAlert("Sign Up successful. Welcome to the library, " + name + "!");
                updateLayoutAfterLogin(primaryStage);
            }
        } else {
            showAlert("Sign Up canceled. Please provide all details.");
        }
    }

    // Method to check if a username is already taken
    private boolean isUsernameTaken(String username) {
        for (Member member : library.getMembers()) {
            if (member.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    // Method to log out the user
    private void logout(Stage primaryStage) {
        loggedInMember = null;
        showAlert("Logged out successfully.");
        updateLayoutAfterLogout(primaryStage); // Pass the primaryStage to the method
    }

    // Method to generate a unique member ID (for test purposes)
    private int generateMemberId() {
        return library.getMembers().size() + 1;
    }
}
