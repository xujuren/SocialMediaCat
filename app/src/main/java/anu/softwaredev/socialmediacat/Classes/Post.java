package anu.softwaredev.socialmediacat.Classes;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/** The model class that stores a Post's information in this App*/
public class Post implements Comparable<Post>{
    private String uId;              // user ID the of author
    private String tag;              // a user-defined "tag" for the post (see report for details)
    private String postId;           // the unique key for a post
    private String content;          // content of post, possibly with geographical coordinates of user
    private int likeCount=0;        // no. of likes
    private int photoId=-1;         // ID of the photo, representing a photo hosted on a website
    private final int PHOTO_ID_LOWER=0;
    private final int PHOTO_ID_UPPER=100;

    /**
     *Constructor 2.1 (with PostId, but without Likes)
     * @param uId id of author
     * @param tag hashtag for search
     * @param postId unique identifier for each post
     * @param content post
     * @param photoId photo unique identifier
     */
    public Post(String uId, String tag, String postId, String content, int photoId) {
        this.uId = uId;
        this.tag = tag;
        this.postId = postId;
        this.content = content.replace("\"", "").replace("\'", "");
        this.photoId = verifyPhotoId(photoId);
        this.likeCount = 0;
    }

    /**
     * Constructor 2.2 (with PostID and like count)
     * @param uId id of author
     * @param tag hashtag for search
     * @param postId unique identifier for each post
     * @param content post
     * @param photoId photo unique identifier
     * @param likeCount num of likes
     */
    public Post(String uId, String tag, String postId, String content, int photoId, int likeCount) {
        this.uId = uId;
        this.tag = tag;
        this.postId = postId;
        this.content = content.replace("\"", "").replace("\'", "");
        this.photoId = verifyPhotoId(photoId);
        this.likeCount = likeCount;
    }

    // Default constructor
    public Post() {}

    // check photo ID (standardized using -1 if not valid)
    public int verifyPhotoId(int photoId){
        if (photoId>=PHOTO_ID_LOWER && photoId<=PHOTO_ID_UPPER){
            return photoId;
        } else {
            return -1;
        }
    }

    /** Getters */
    public String getUId(){return uId;}
    public String getPostId(){return postId;}           // Excluded?
    public String getTag(){return tag;}
    public String getContent(){ return content; }
    public int getPhotoId(){return photoId;}
    public int getLikeCount(){return likeCount;}

    /** Setters */
    public void setPostId(String postId){this.postId = postId;}  // excluded?
    public void setUId(String uId){this.uId = uId;}
    public void setTag(String tag){this.tag = tag;}
    public void setContent(String content){this.content = content;}
    public void setPhotoId(int photoId){this.photoId =photoId;}
    public void setLikeCount(int likeCount){this.likeCount = likeCount;}

    public void likePost() {
        likeCount += 1;
    }

    public void unlikePost() {
        likeCount -= 1;
    }
    

    /**
     * Notes: the format is not following our CFG
     * @return a default formatted string, representing the post
     */
    @Override
    public String toString(){
        return uId + ";" + tag + ";" + postId + ";" + content + ";" + photoId + ";" + likeCount;
    }

    /**
     * convert the current class into a hashmap(dictionary), be called in UserActivity class (createPost method )
     * @return  a hashmap data structure with all the attributes of this class
     */
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uId", uId);
        result.put("tag", tag);
        result.put("content", content);
        result.put("photoId", photoId);
        result.put("likeCount", likeCount);
        return result;
    }

    @Override
    public int compareTo(Post o) {
        return postId.compareTo(o.getPostId());
    }

}
