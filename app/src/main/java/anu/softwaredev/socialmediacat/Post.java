package anu.softwaredev.socialmediacat;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {

    /** Addition Ref - Write New Post */
    // https://firebase.google.com/docs/database/android/read-and-write

    public String uid;
    public String author;
    public String title;
    public String content;
    public int starCount;
    public Map<String, Boolean> stars = new HashMap<>();
    // Photo

    public Post(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(anu.softwaredev.socialmediacat.Post.class)
    }

    public Post(String uid, String author, String title, String body) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.content = body;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("content", content);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }

}
