package anu.softwaredev.socialmediacat.Classes;
import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;

/** The model class that stores a User's information in this App*/
public class User {
    private String uId;
    private String emailAddress;
    private String userName = "";
    private String caption = "";
    private String proPicLink = "";     // TODO - CHANGE into another field, and change ProfileActivity

    // Constructors
    public User(String uId, String emailAddress) {
        this.uId = uId;
        this.emailAddress = emailAddress;
    }

    public User(String uId, String emailAddress, String userName, String caption, String proPicLink) {
        this.uId = uId;
        this.emailAddress = emailAddress;
        this.userName = userName;
        this.caption = caption;
        this.proPicLink = proPicLink;
    }


    /** Getters */
    public String getUid(){return uId;}


    /**
     * convert the current class into a hashmap(dictionary), be called in UserActivity class (createPost method )
     * @return  a hashmap data structure with all the attributes of this class
     */
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userName", userName);
        result.put("emailAddress", emailAddress);
        result.put("caption", caption);
        result.put("proPicLink", proPicLink);
        return result;
    }


}
