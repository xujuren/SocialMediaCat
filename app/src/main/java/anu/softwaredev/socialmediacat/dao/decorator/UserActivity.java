package anu.softwaredev.socialmediacat.dao.decorator;

// from Design Pattern Lec
public class UserActivity {

    private String action;
    private String uId;         // user ID of the author
    private String category;   // Category of Post (createPost)
    private String postId;     // ID of Post (createPost) (TODO <=> ++)
    private String content;

    public UserActivity(String action, String uId, String category, String postId, String content) {
        this.action = action;
        this.uId = uId;
        this.category = category;
        this.postId = postId;
        this.content = content;
    }


    // Getters
    public String getAction() {return action;}
    public String getCategory() {return category;}
    public String getPostId() {return postId;}
    public String getContent() {return content;}
    public String getUId() {return uId;}

}
