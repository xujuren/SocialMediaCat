package anu.softwaredev.socialmediacat.dao.decorator;

/** User Activity */
public class UserActivity {
    private String action;      // name of action
    private String uId;         // user ID
    private String category;   // Category (*for createPost)
    private String postId;     // PostId (TODO <=> ++)
    private String content;     // content of post/action

    public UserActivity(String action, String uId, String category, String postId, String content) {
        this.action = action;
        this.uId = uId;
        this.category = category;
        this.postId = postId;
        this.content = content;
    }

    // Getters
    public String getAction() {return action;}
    public String getUId() {return uId;}
    public String getCategory() {return category;}
    public String getPostId() {return postId;}
    public String getContent() {return content;}

}
