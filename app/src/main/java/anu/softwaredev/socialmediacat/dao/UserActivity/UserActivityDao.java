package anu.softwaredev.socialmediacat.dao.UserActivity;
import android.os.Build;

import androidx.annotation.NonNull;

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

import Tree.Global_Data;
import anu.softwaredev.socialmediacat.Classes.Post;

/** Dao for UserActivity */
public class UserActivityDao implements IUserActivityDao {
    private static DatabaseReference dbRef1;
    private static DatabaseReference dbRef2;
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
        if (instance == null) {
            instance = new UserActivityDao();
            dbRef1 = FirebaseDatabase.getInstance().getReference("Posts");
            dbRef2 = FirebaseDatabase.getInstance().getReference("Posts");
        }
        return instance;
    }


    @Override
    public void createPost(String uId, String tag, String content, int photoId,int likeCount) {           // alt: only content (userName: below)
        try {

            // check photo ID (generate randomly between 20, 100 inclusive if unavailable)
            if (photoId==-1 || photoId<20 || photoId>100) {
                photoId = (int) (Math.random() *((100-20)+1) + 20); 	    // (use for URL below)
            }

            // Check invalid characters in tag (empty if invalid)
            if (tag.contains(",") || tag.contains(";")) {tag="";}

            // Check invalid characters in content (remove inner "'s)
            String contentText = content.substring(1, content.length()-1);
            contentText.replaceAll("\"", "");

            // Update firebase DB
            String postId = dbRef1.child("Posts").push().getKey();             // unique Key for Posts
            Post newPost = new Post(uId, tag, postId, content, photoId, likeCount);
            Map<String, Object> postValues = newPost.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/Posts/" + postId, postValues);
            dbRef1.updateChildren(childUpdates);

            // write to file                  // post
            String text = "create-post" + ";" + uId + ";" + tag + ";" + postId + ";" + content + ";" + photoId + ";" + likeCount + "\n";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
            }
            System.out.println("Post saved in " + file.getAbsolutePath());

        } catch (IOException e) { e.printStackTrace(); }

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

    @Override
    public UserActivity likePost(String userId, String postId) {

        // TODO - TO BE ADDED
        return null;
    }

    @Override
    public void deletePost(String postId) {
        // firebase DB
        dbRef2 = FirebaseDatabase.getInstance().getReference("Posts");
        dbRef2.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (ds.getKey().equals(postId)){
                        ds.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
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
                        Global_Data.getInstance().insert(post.getTag(), post);
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

