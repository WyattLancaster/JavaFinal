package application;

public class Member {
    private String name; // Name of the member
    private int memberId; // ID of the member
    private String username; // Username for login
    private String password; // Password for login

    public Member(String name, int memberId, String username, String password) {
        this.name = name; // Initialize the name of the member
        this.memberId = memberId; // Initialize the ID of the member
        this.username = username; // Initialize the username
        this.password = password; // Initialize the password
    }

    public String getName() {
        return name; // Get the name of the member
    }

    public int getMemberId() {
        return memberId; // Get the ID of the member
    }

    public String getUsername() {
        return username; // Get the username
    }

    public String getPassword() {
        return password; // Get the password
    }
}
