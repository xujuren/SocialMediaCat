package anu.softwaredev.socialmediacat.dao.decorator;
import static android.content.ContentValues.TAG;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.CreatePost;
import anu.softwaredev.socialmediacat.ProfileActivity;
import anu.softwaredev.socialmediacat.R;

/** DAO for UserActivity */
public class UserActivityDao implements IUserActivityDao {
    private DatabaseReference dbRef;
    private FirebaseUser user;

    private static UserActivityDao instance;        /** defined as Singleton */
    private static Integer idCount=0;               // TODO TBC
    private static File file;                       // temporary fire
    static {
        try {
            file = File.createTempFile("user-action", ".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private UserActivityDao() {};


    @Override           /** TODO (userName, Content) ... */
    public UserActivity createPost(String uId, String tags, String content, int photoId) {           // alt: only content (userName: below)
        try {
            // Post Id  TODO (set with data)
//            user = FirebaseAuth.getInstance().getCurrentUser();
//            if (user != null) {
//                String userId = user.getUid();
//            } else {
//                // ERROR
//                return null;
//            }

            String action = "create-post";
            UserActivity userAct = new UserActivity(action, uId, tags, content, photoId);
            // ID for photo - check validity (*for local data instances)
            if (photoId==-1 || photoId<20 || photoId>100) {
                photoId = (int) (Math.random() *((100-20)+1) + 20); 	// gen rand id (use for URL below), max=100 min=(20)
            }

            // Update DB (new)
            dbRef = FirebaseDatabase.getInstance().getReference();            // path to DB
            String postId = dbRef.child("Posts").push().getKey();             // unique Key for Posts
            Post newPost = new Post(uId, tags, postId, content, photoId);
            Map<String, Object> postValues = newPost.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/Posts/" + postId, postValues);
            // childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

            dbRef.updateChildren(childUpdates);

            // ORI (for temp id)
//          idCount++;
//          postId = "temp"+idCount.toString();


            // write to file (action, uId, tags, postId, content, photoId);
            String text = action + ";" + uId + ";" + tags + ";" + postId + ";" + content + ";" + photoId + ";" + "0" + "\n";            //TODO > userName
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
            }

            System.out.println("Post saved in " + file.getAbsolutePath());

            return new UserActivity(action, uId, tags, content, photoId);

        } catch (IOException e) { e.printStackTrace(); }

        return null;
    }


    /** TODO (userName, Content) ... */
    public void loadPost(String uId, String tags, String postId, String content, int photoId, int likeCounts) {           // alt: only content (userName: below)
        try {

            String action = "store-post";
            // write to file
            String text = action + ";" + uId + ";" + tags + ";" + postId + ";" + content + ";" + photoId + ";" +likeCounts + "\n";            //TODO > userName
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
            }
            System.out.println("Post saved in " + file.getAbsolutePath());

            // return new UserActivity(action, uId, tags, content, photoId);   // TODO why?

        } catch (IOException e) { e.printStackTrace(); }

        // return null;
    }


    // TODO [@Kyle] see here for likePost
    @Override
    public UserActivity likePost(String username, Integer idPost) {
//        try{
//            String action = "like-post";
//            String content = "+1";
//            String text = action + ";" + username + ";" + content + ";" + idPost + "\n";        // TODO changed tags
//            Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
//            System.out.println("Like saved in " + file.getAbsolutePath());
//            UserActivity userActivity = new UserActivity(username, action, content, idPost);
//            return userActivity;
//        } catch (IOException e){
//            e.printStackTrace();
//        }
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
                    if (items != null && items.length == 7 & ("create-post".equals(items[0]) || "store-post".equals(items[0]))) {
                        String uId = items[1];
                        String tags = items[2];
                        String postId = items[3];
                        String content = items[4];
                        int photoId = Integer.parseInt(items[5]);
                        int likeCount = Integer.parseInt(items[6]);
                        Post post = new Post(uId, tags, postId, content, photoId, likeCount);
                        postsLoaded.add(post);
                    }
                }
            }

        } catch (IOException e) { e.printStackTrace(); }
        return postsLoaded;                            // return all posts (by e.g. List<UserActivity> allPosts = UserActivityDao.findAllPosts();)
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

    public static UserActivityDao getInstance(){
        if (instance == null) { instance = new UserActivityDao(); }
        return instance;
    }


}

