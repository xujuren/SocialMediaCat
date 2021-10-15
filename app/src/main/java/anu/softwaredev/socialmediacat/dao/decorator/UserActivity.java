package anu.softwaredev.socialmediacat.dao.decorator;

import anu.softwaredev.socialmediacat.Classes.Post;

/** User Activity */
public class UserActivity {
    private String action;      // name of action
    private String uId;         // user ID
    private String tags;   // Category (*for createPost) TODO: > tags
    private String content;     // content of post/action
    private int photoId;        // ID of photo used of Post
    private int likeCount;      // likeCount of Post

    public UserActivity(String action, String uId, String tags, String content, int photoId) {
        this.action = action;
        this.uId = uId;
        this.tags = tags;
        this.content = content;
        this.photoId = photoId;
    }


    // Constructor without PHOTO ID
    public UserActivity(String action, String uId, String tags, String content) {
        this.action = action;
        this.uId = uId;
        this.tags = tags;
        this.content = content;
    }

    // Getters
    public String getAction() {return action;}
    public String getUId() {return uId;}
    public String getTags() {return tags;}
    public String getContent() {return content;}
    public int getPhotoId() {return photoId;}


}
