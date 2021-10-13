package anu.softwaredev.socialmediacat.Classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {

    public String uId;              // [user ID] of author
    public String category;         // TODO > enum ?
    public String postId;           // TODO > to be determined [Category + postId] = key ??
    public String content;          // Post Content
    // public int likeCount=0;           // no. of likes
    // TODO - Photo (see time)


    public Post(String uId, String category, String postId, String content) {
        this.uId = uId;
        this.category = category;
        this.postId = postId;
        this.content = content;
    }

    // Constructor with "likeCount"
    public Post(String uId, String category, String postId, String content, int likeCount) {
        this.uId = uId;
        this.category = category;
        this.postId = postId;
        this.content = content;
        // this.likeCount = likeCount;
    }

    // Default constructor required for calls to DataSnapshot.getValue(anu.softwaredev.socialmediacat.Classes.Post.class)
    public Post() {}

    public String getUId(){return uId;}
    public String getCategory(){return category;}
    public String getPostId(){return postId;}
    public String getContent(){return content;}


    @Override
    public String toString(){
        return uId+"/"+category+"/"+postId+"/"+content;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uId", uId);
        result.put("category", category);
        result.put("postId", postId);
        result.put("content", content);
        // result.put("photo", photo);
        // result.put("likeCount", likeCount);
        return result;
    }




    /** Write New Post (https://firebase.google.com/docs/database/android/read-and-write) */


}
