package anu.softwaredev.socialmediacat;

// Model Class: stores User Info
public class User {
    /** Ref:
     * After signing up a new user you will receive a generated uid of user from Firebase,
     * >> take uid & create a new [user entry] in ur Firestore repo/Realtime db w/ the uid + additional info.*/

    String uId;
    String emailAddress;
    String password;
    String displayName;
    Character gender;
    // DoB

    public User(String uId, String emailAddress, String password) {
        this.uId = uId;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public User(String uId, String emailAddress, String displayName, Character gender) {
        this.displayName = displayName;
        this.emailAddress = emailAddress;
        this.gender = gender;
    }

    // Getter methods
    public String getUId() {return uId;}
    public String getEmailAddress() {return emailAddress;}
    public String getName() {return displayName;}
    public Character getGender() {return gender;}


    // Setter methods
    public void setEmailAddress(String emailAddress) {this.emailAddress = emailAddress;}
    public void setName(String name) {this.displayName = name;}
    public void setGender(Character gender) {this.gender = gender;}


}
