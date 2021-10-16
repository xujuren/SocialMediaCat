package anu.softwaredev.socialmediacat.Classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/** Posts to be created by users*/
public class Post {

    public String uId;              // [user ID] of author
    private String tags;             // for Tag Search (user input, EXPECT to be in the format of #tagContent (without whitespaces))
    private String postId;           // Unique KEY for Posts
    private String content;          // content of post, possibly with geographical coordinates of user
    private int likeCount=0;         // no. of likes
    private int photoId;            // ID of the photo


    /**
     *Constructor 2.1 (with PostId, but without Likes)
     * @param uId id of author
     * @param tags hashtag for search
     * @param postId unique identifier for each post
     * @param content post
     * @param photoId photo unique identifier
     */
    public Post(String uId, String tags, String postId, String content, int photoId) {
        this.uId = uId;
        this.tags = tags;
        this.postId = postId;
        this.content = content;
        this.photoId = photoId;
        this.likeCount = 0;
    }


    /**
     *     Constructor 2.2 (with PostID and like count)
     * @param uId id of author
     * @param tags hashtag for search
     * @param postId unique identifier for each post
     * @param content post
     * @param photoId photo unique identifier
     * @param likeCount num of likes
     */
    public Post(String uId, String tags, String postId, String content, int photoId, int likeCount) {
        this.uId = uId;
        this.tags = tags;
        this.postId = postId;
        this.content = content;
        this.photoId = photoId;
        this.likeCount = likeCount;
    }


    // Default constructor required for calls to DataSnapshot.getValue(anu.softwaredev.socialmediacat.Classes.Post.class)
    public Post() {}

    public String getUId(){return uId;}
    public String getTags(){return tags;}
    public String getPostId(){return postId;}
    public String getContent(){return content;}
    public int getPhotoId(){return photoId;}
    public int getLikes(){return likeCount;}


    /**
     *
     * @return a default formatted string represents the post
     */
    @Override
    public String toString(){
        return "@"+uId+ " " +content+" #"+tags+" ["+postId+"]: ";       // easy for understanding only, not following grammar
    }


    /**
     * convert the current class into a hashmap(dictionary), be called in UserActivity class (createPost method )
     * @return  a hashmap data structure with all the attributes of this class
     */
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uId", uId);
        result.put("tags", tags);
        result.put("postId", postId);
        result.put("content", content);
        result.put("likeCount", likeCount);
        result.put("photoId", photoId);
        return result;
    }




    /** Write New Post (https://firebase.google.com/docs/database/android/read-and-write) */


}
