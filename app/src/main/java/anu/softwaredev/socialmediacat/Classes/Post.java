package anu.softwaredev.socialmediacat.Classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {

    public String category;         // TODO > enum
    public String postId;           // TODO > to be determined [Category + postId] = key
    public String content;          // Post Content
    public int likeCount=0;           // no. of likes
    public String uId;              // [user ID] of author
    // TODO - Photo (see how we manage resources)


    public Post(String category, String postId, String title, String content, String uId) {
        this.category = category;
        this.postId = postId;
        this.content = content;
        this.uId = uId;
    }

    // Constructor with "likeCount"
    public Post(String category, String postId, String title, String content, String uId, int likeCount) {
        this.category = category;
        this.postId = postId;
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
        result.put("category", category);
        // result.put("photo", photo);
        result.put("content", content);
        result.put("likeCount", likeCount);
        return result;
    }

    /** Write New Post (https://firebase.google.com/docs/database/android/read-and-write) */


}
