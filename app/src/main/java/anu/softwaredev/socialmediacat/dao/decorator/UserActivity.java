package anu.softwaredev.socialmediacat.dao.decorator;

import anu.softwaredev.socialmediacat.Classes.Post;

/** User Activity */
public class UserActivity {
    private String action;      // name of action
    private String uId;         // user ID
    private String tags;   // Category (*for createPost) TODO: > tags
    private String postId;     // PostId (TODO <=> ++)
    private String content;     // content of post/action

    public UserActivity(String action, String uId, String tags, String postId, String content) {
        this.action = action;
        this.uId = uId;
        this.tags = tags;
        this.postId = postId;
        this.content = content;
    }

    // Constructor without Post ID (e.g. creat post)
    public UserActivity(String action, String uId, String tags, String content) {
        this.action = action;
        this.uId = uId;
        this.postId = "";
        this.tags = tags;
        this.content = content;
    }

    // Getters
    public String getAction() {return action;}
    public String getUId() {return uId;}
    public String getTags() {return tags;}
    public String getPostId() {return postId;}
    public String getContent() {return content;}

    // Post
    public Post getPost(){
        return new Post(uId, tags, postId, content);
    }

}
