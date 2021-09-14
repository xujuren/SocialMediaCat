package anu.softwaredev.socialmediacat;

// Model Class: stores User Info
public class User {
    /** Ref:
     * After signing up a new user you will receive a generated uid of user from Firebase,
     * >> take uid & create a new [user entry] in ur Firestore repo/Realtime db w/ the uid + additional info.*/

    String uId;
    String emailAddress;
    String password = "";
    String displayName = "";
    // Character gender = "";
    String proPicLink = "";  // or [Uri] type?      Uri.parse("-")
    int birthYear = -1;
    // DoB

    // Constructor (@Sign Up)
    public User(String uId, String emailAddress) {
        this.uId = uId;
        this.emailAddress = emailAddress;
    }

    // Constructor (@Sign Up)2
    public User(String uId, String emailAddress, String password) {
        this.uId = uId;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    // Getter methods
    public String getUId() {return uId;}
    public String getEmailAddress() {return emailAddress;}
    public String getName() {return displayName;}
    public String getProPic() {return this.proPicLink;}

    // Setter methods
    public void setEmailAddress(String emailAddress) {this.emailAddress = emailAddress;}
    public void setName(String name) {this.displayName = name;}
    public void setProPic(String profilePicLink) {this.proPicLink = profilePicLink;}


}
