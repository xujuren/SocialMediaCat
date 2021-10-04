package anu.softwaredev.socialmediacat.dao.decorator;

// from Design Pattern Lec
public class UserActivity {

    private String username;
    private String action;
    private String contest;
    private Integer idPost;

    public UserActivity(String username, String action, String contest, Integer idPost) {
        this.username = username;
        this.action = action;
        this.contest = contest;
        this.idPost = idPost;
    }

    public String getUsername() {return username;}

    public String getAction() {return action;}

    public String getContent() {return contest;}

    public Integer getidPost() {return idPost;}

}
