package anu.softwaredev.socialmediacat.dao.UserActivity;

/** User Activity */
public class UserActivity {
    private String action;      // name of action
    private String uId;         // user ID
    private String tag;         // tag
    private String content;     // content of post/action
    private int photoId;        // ID of photo used of Post
    private String postId;      // postId involved

    public UserActivity(String action, String uId, String tag, String content, int photoId) {
        this.action = action;
        this.uId = uId;
        this.tag = tag;
        this.content = content;
        this.photoId = photoId;
    }

    public UserActivity(String action, String uId, String tag, String content, int photoId, int start) {
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

    // Constructor with 3 items
    public UserActivity(String action, String uId, String postId) {
        this.action = action;
        this.uId = uId;
        this.postId = postId;
    }

    // Getters
    public String getAction() {return action;}
    public String getUId() {return uId;}
    public String getTag() {return tag;}
    public String getContent() {return content;}
    public int getPhotoId() {return photoId;}
    public String getPostId() {return postId;}
}
