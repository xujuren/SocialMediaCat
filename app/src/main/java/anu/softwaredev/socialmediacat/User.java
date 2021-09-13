package anu.softwaredev.socialmediacat;

// Model Class: stores User Info
public class User {

    /** Ref:
     * After signing up a new user you will receive a generated uid of user from Firebase,
     * >> take uid & create a new [user entry] in ur Firestore repo/Realtime db w/ the uid + additional info.*/

    String emailAddress, userName;
    Character gender;
    // DoB

    public User(String emailAddress, String userName, Character gender) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.gender = gender;
    }

    // Getter methods
    public String getEmailAddress() {return emailAddress;}
    public String getName() {return userName;}
    public Character getGender() {return gender;}


    // Setter methods
    public void setEmailAddress(String emailAddress) {this.emailAddress = emailAddress;}
    public void setName(String name) {this.userName = name;}
    public void setGender(Character gender) {this.gender = gender;}


}
