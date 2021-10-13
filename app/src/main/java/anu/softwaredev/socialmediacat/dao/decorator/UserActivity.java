package anu.softwaredev.socialmediacat.dao.decorator;

// from Design Pattern Lec
public class UserActivity {

    private String action;
    private String uId;
    private String category;   // Category of Post (createPost)
    private String postId;     // ID of Post (createPost) (TODO <=> ++)
    private String content;

    public UserActivity(String action, String uId, String category, String postId, String content) {
        this.uId = uId;
        this.action = action;
        this.content = content;
        this.postId = postId;
        this.category = category;
    }


    // Getters
    public String getAction() {return action;}

    public String getCategory() {return category;}
    public String getPostId() {return postId;}
    public String getContent() {return content;}

    public String getuId() {return uId;}

}
