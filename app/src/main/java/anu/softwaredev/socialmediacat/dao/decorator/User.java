package anu.softwaredev.socialmediacat.dao.decorator;
import java.util.ArrayList;
import java.util.List;

// Model Class: stores User Info
public class User {
    String uId;
    String emailAddress;
    String userName = "";
    String proPicLink = "";  // TODO - or [Uri] type?      Uri.parse("-")
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
