package anu.softwaredev.socialmediacat.Classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {

    public String uId;              // [user ID] of author
    // public String category;         // TODO > (1) maybe DELETE? given that we have tag, (2) enum ?
    public String tags;             // TODO (NEW, for Tag Search) user input, in the format of #<tag1> or  #<tag1> #<tag2> ... #<tagn> (without whitespaces)
    public String postId;           // TODO > to be determined [Category + postId] = key ??
    public String content;          // Post Content
    public int likeCount=0;         // no. of likes
    // TODO - Photo (see time)

    /* Constructor 1.1 (uId, category, postId, content) */
//    public Post(String uId, String category, String postId, String content) {
//        this.uId = uId;
//        this.category = category;
//        this.postId = postId;
//        this.content = content;
//    }

    /* Constructor 1.2 (uId, category, postId, content + likeCount input) */
//    public Post(String uId, String category, String postId, String content, int likeCount) {
//        this.uId = uId;
//        this.category = category;
//        this.postId = postId;
//        this.content = content;
//        // this.likeCount = likeCount;
//    }

    /* Constructor 2.1 (TODO tags replace category) */
    public Post(String uId, String tags, String postId, String content) {
        this.uId = uId;
        this.tags = tags;
        this.postId = postId;
        this.content = content;
    }

    /* Constructor 2.2 - with likeCount (TODO tags replace category) */
    public Post(String uId, String tags, String postId, String content, int likeCount) {
        this.uId = uId;
        this.tags = tags;
        this.postId = postId;
        this.content = content;
        this.likeCount = likeCount;
    }


    // Default constructor required for calls to DataSnapshot.getValue(anu.softwaredev.socialmediacat.Classes.Post.class)
    public Post() {}

    public String getUId(){return uId;}
    public String getTags(){return tags;}
    public String getPostId(){return postId;}
    public String getContent(){return content;}
    public int getLikes(){return likeCount;}

    @Override
    public String toString(){
        return "@"+uId+ " " +content+" #"+tags+" ["+postId+"]: ";       // easy for understanding only, not following grammar
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uId", uId);
        result.put("tags", tags);
        result.put("postId", postId);
        result.put("content", content);
        result.put("likeCount", likeCount);
        // result.put("photo", photo);
        return result;
    }




    /** Write New Post (https://firebase.google.com/docs/database/android/read-and-write) */


}
