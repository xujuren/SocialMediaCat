package anu.softwaredev.socialmediacat.Classes;

// Model Class: stores User Info
public class User {
    String uId;
    String emailAddress;
    String userName = "";
    String proPicLink = "";     // TODO - or [Uri] type?      Uri.parse("-")
    // List<String> followers = new ArrayList<>();

    // Constructor (@Sign Up)
    public User(String uId, String emailAddress) {
        this.uId = uId;
        this.emailAddress = emailAddress;
    }

    // Constructor
    public User(String uId, String emailAddress, String userName, String proPicLink) {
        this.uId = uId;
        this.emailAddress = emailAddress;
        this.userName = userName;
        this.proPicLink = proPicLink;
    }

}
