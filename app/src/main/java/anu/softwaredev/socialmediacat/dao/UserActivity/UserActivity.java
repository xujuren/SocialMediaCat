package anu.softwaredev.socialmediacat.dao.UserActivity;

/** User Activity */
public class UserActivity {
    private String action;      // name of action
    private String uId;         // user ID
    private String tag;        // tag
    private String content;     // content of post/action
    private int photoId;        // ID of photo used of Post

    public UserActivity(String action, String uId, String tag, String content, int photoId) {
        this.action = action;
        this.uId = uId;
        this.tag = tag;
        this.content = content;
        this.photoId = photoId;
    }


    // Constructor without PHOTO ID
    public UserActivity(String action, String uId, String tag, String content) {
        this.action = action;
        this.uId = uId;
        this.tag = tag;
        this.content = content;
    }

    // Getters
    public String getAction() {return action;}
    public String getUId() {return uId;}
    public String getTag() {return tag;}
    public String getContent() {return content;}
    public int getPhotoId() {return photoId;}


}
