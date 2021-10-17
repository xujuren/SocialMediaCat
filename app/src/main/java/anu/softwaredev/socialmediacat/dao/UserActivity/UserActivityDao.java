package anu.softwaredev.socialmediacat.dao.UserActivity;
import android.os.Build;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import anu.softwaredev.socialmediacat.Classes.Post;

/** Dao for UserActivity */
public class UserActivityDao implements IUserActivityDao {
    private DatabaseReference dbRef;
//    private static Integer postId=0;

    private static UserActivityDao instance;        // Singleton instance for UserActivityDao
    private static File file;                       // temporary file
    static {
        try {
            file = File.createTempFile("user-action", ".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Singleton
    private UserActivityDao() {this.deleteAll();};
    public static UserActivityDao getInstance(){
        if (instance == null) { instance = new UserActivityDao(); }
        return instance;
    }


    @Override           /** TODO (userName, Content) ... */
    public void createPost(String uId, String tag, String content, int photoId,int likeCount) {           // alt: only content (userName: below)
        try {
            // check ID for photo (generate random ID for URL if invalid, [20, 100])
            if (photoId==-1 || photoId<20 || photoId>100) {
                photoId = (int) (Math.random() *((100-20)+1) + 20); 	    // (use for URL below)
            }

            // Check invalid characters in tag (empty if invalid)
            if (tag.contains(",") || tag.contains(";")) {tag="";}

            // Check invalid characters in content (remove inner "'s)
            String contentText = content.substring(1, content.length()-1);
            contentText.replaceAll("\"", "");

            // Update firebase DB
            dbRef = FirebaseDatabase.getInstance().getReference();            // path to DB
            String postId = dbRef.child("Posts").push().getKey();             // unique Key for Posts
            Post newPost = new Post(uId, tag, postId, content, photoId, likeCount);
            Map<String, Object> postValues = newPost.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/Posts/" + postId, postValues);
            dbRef.updateChildren(childUpdates);

            // write to file
            String text = "create-post" + ";" + uId + ";" + tag + ";" + postId + ";" + content + ";" + photoId + ";" + likeCount + "\n";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
            }
            System.out.println("Post saved in " + file.getAbsolutePath());

        } catch (IOException e) { e.printStackTrace(); }

    }

    @Override
    public void likePost(Integer idPost) {

    }


    /** TODO (n, Content) ... */
    public void loadPost(Post post) {           // alt: only content (userName: below)
        try {
            // write to file
            String text = "store-post" + ";" + post.getUId() + ";" + post.getTag() + ";" + post.getPostId() + ";" + post.getContent() + ";" + post.getPhotoId() + ";" + post.getLikes() + "\n";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
            }
            System.out.println("Post saved in " + file.getAbsolutePath());

        } catch (IOException e) { e.printStackTrace(); }

    }


    // TODO [@Kyle] see here for likePost
    @Override
    public UserActivity likePost(String userId, String postId) {

        return null;
    }


    // @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<Post> findAllPosts() {
        List<Post> postsLoaded = new ArrayList<>();
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            String fileContent = new String(bytes);
            String[] lines = fileContent.split("\n");

            if (lines != null) {
                for (String line : lines) {
                    String[] items = line.split(";");
                    if (items!=null && items.length==7 && ("create-post".equals(items[0]) || "store-post".equals(items[0]) )) {  //
                        Post post = new Post(items[1], items[2], items[3], items[4], Integer.parseInt(items[5]), Integer.parseInt(items[6]));
                        postsLoaded.add(post);
                    }
                }
            }

        } catch (IOException e) { e.printStackTrace(); }
        return postsLoaded;
    }


    @Override
    public String getFilePath() {
        return file.getAbsolutePath();
    }

    @Override
    public void deleteAll() {
        try {
            if (file.exists()){
                file.delete();
            }
            file = File.createTempFile("user-action", ".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}

