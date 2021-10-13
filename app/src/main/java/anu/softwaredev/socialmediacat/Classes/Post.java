package anu.softwaredev.socialmediacat.Classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {

    public String category;         // TODO > enum
    public String postId;           // TODO > to be determined [Category + postId] = key
    public String title;            // Post Title
    public String content;          // Post Content
    public int likeCount;          // no. of likes
    public String uId;              // user ID of author
    // TODO - Photo (see how we manage resources)


    public Post(String category, String postId, String title, String content, String uid, String author) {
        this.category = category;
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.uId = uId;
        this.likeCount = 0;
    }

    // Constructor with "likeCount"
    public Post(String category, String postId, String title, String content, String uid, String author, int likeCount) {
        this.category = category;
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.uId = uId;
        this.likeCount = likeCount;
    }

    // Default constructor required for calls to DataSnapshot.getValue(anu.softwaredev.socialmediacat.Classes.Post.class)
    public Post() {}


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uId", uId);
        result.put("uId", category);
        // result.put("userName", userName);
        result.put("title", title);
        result.put("content", content);
        result.put("likeCount", likeCount);
        return result;
    }

    /** Write New Post (https://firebase.google.com/docs/database/android/read-and-write) */


}
