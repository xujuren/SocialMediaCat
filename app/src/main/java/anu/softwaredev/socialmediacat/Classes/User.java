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
    private String interests = "";

    /* Constructors */
    public User(String uId, String emailAddress) {
        this.uId = uId;
        this.emailAddress = emailAddress;
    }

    public User(String uId, String emailAddress, String userName, String caption, String interests) {
        this.uId = uId;
        this.emailAddress = emailAddress;
        this.userName = userName;
        this.caption = caption;
        this.interests = interests;
    }

    /* Getters */
    public String getUid(){return uId;}
    public String getEmailAddress(){return emailAddress;}
    public String getUserName(){return userName;}
    public String getCaption(){return caption;}
    public String getInterests(){return interests;}

    /* Setters */
    public String setUid(){return uId;}
    public String setEmailAddress(){return emailAddress;}
    public String setUserName(){return userName;}
    public String setCaption(){return caption;}
    public String setInterests(){return interests;}


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
        result.put("interests", interests);
        return result;
    }


}
