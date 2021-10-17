package anu.softwaredev.socialmediacat.Classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

// Model Class: stores User Info
public class User {
    private String uId;
    private String emailAddress;
    private String userName = "";
    private String proPicLink = "";     // TODO - or [Uri]
    // List<String> followers = new ArrayList<>();

    // Constructors
    public User(String uId, String emailAddress) {
        this.uId = uId;
        this.emailAddress = emailAddress;
    }

    public User(String uId, String emailAddress, String userName, String proPicLink) {
        this.uId = uId;
        this.emailAddress = emailAddress;
        this.userName = userName;
        this.proPicLink = proPicLink;
    }

    // Getter
    public String getUid(){
        return uId;
    }

    /**
     * convert the current class into a hashmap(dictionary), be called in UserActivity class (createPost method )
     * @return  a hashmap data structure with all the attributes of this class
     */
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("emailAddress", emailAddress);
        result.put("userName", userName);
        result.put("proPicLink", proPicLink);
        return result;
    }


}
