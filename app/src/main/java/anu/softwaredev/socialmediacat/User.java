package anu.softwaredev.socialmediacat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Model Class: stores User Info
public class User {
    /** Ref:
     * After signing up a new user you will receive a generated uid of user from Firebase,
     * >> take uid & create a new [user entry] in ur Firestore repo/Realtime db w/ the uid + additional info.*/

    String uId;
    String emailAddress;
    String userName = "";
    String password = "";
    String proPicLink = "";  // or [Uri] type?      Uri.parse("-")
    List<String> followers = new ArrayList<>(); // ???

    // Constructor (@Sign Up)
    public User(String uId, String emailAddress) {
        this.uId = uId;
        this.emailAddress = emailAddress;
    }

    // Constructor (@Sign Up)2
    public User(String uId, String emailAddress, String userName, String proPicLink, List<String> followers) {
        this.uId = uId;
        this.emailAddress = emailAddress;
        this.userName = userName;
        this.proPicLink = proPicLink;
        this.followers = followers;
    }

    // Getter methods
    public String getUId() {return uId;}
    public String getEmailAddress() {return emailAddress;}
    public String getUserName() {return userName;}
    public String getProPic() {return this.proPicLink;}

    // Setter methods
    public void setEmailAddress(String emailAddress) {this.emailAddress = emailAddress;}
    public void setUserName(String userName) {this.userName = userName;}
    public void setProPic(String profilePicLink) {this.proPicLink = profilePicLink;}


}
