package anu.softwaredev.socialmediacat.dao.UserActivity;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
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
import anu.softwaredev.socialmediacat.Classes.User;
import anu.softwaredev.socialmediacat.ProfileActivity;
import anu.softwaredev.socialmediacat.R;
import anu.softwaredev.socialmediacat.Util.AssetHandler;

/** Dao for UserActivity */
public class UserActivityDao implements IUserActivityDao {
    private static DatabaseReference dbRef;
    private static List<Post> allPosts;
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
            dbRef = FirebaseDatabase.getInstance().getReference("Posts");
        }
        return instance;
    }


    @Override
    public void createPost(String uId, String tag, String content, int photoId) {           // alt: only content (userName: below)
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
            String postId = dbRef.child("Posts").push().getKey();             // unique Key for Posts
            Post newPost = new Post(uId, tag, postId, content, photoId);
            Map<String, Object> postValues = newPost.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/Posts/" + postId, postValues);
            dbRef.updateChildren(childUpdates);

            // write to file                  // post
            String text = "create-post" + ";" + uId + ";" + tag + ";" + postId + ";" + content + ";" + photoId + ";" + "0" + "\n";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
            }
            System.out.println("Post saved in " + file.getAbsolutePath());

        } catch (IOException e) { e.printStackTrace(); }

    }


    /** Load Posts for display */
    public void loadPost(List<Post> posts) {

        for (Post post : posts){
            // write to file
            try {
                String text = "store-post" + ";" + post.getUId() + ";" + post.getTag() + ";" + post.getPostId() + ";" + post.getContent() + ";" + post.getPhotoId() + ";" + post.getLikeCount() + "\n";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
                }
                System.out.println("Post saved in " + file.getAbsolutePath());

            } catch (IOException e) { e.printStackTrace(); }
        }

        // Firebase TODO
//        dbRef = FirebaseDatabase.getInstance().getReference().child("Posts");
//        ChildEventListener listener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Post newPost = snapshot.getValue(Post.class);
//                allPosts.add(newPost);
//            }
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//            }
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                Integer postId = Integer.parseInt(snapshot.getKey());
//            }
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) { }
//        };
//        dbRef.addChildEventListener(listener);


    }

    @Override
    public void likePost(String userId, String postId) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("/likeCount", ServerValue.increment(1));
        dbRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postId);
        dbRef.updateChildren(updates);
    }

    @Override
    public void deletePost(String postId) {
        // firebase DB
        dbRef = FirebaseDatabase.getInstance().getReference("Posts");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener(){
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

            // allPosts TODO
//            for (Post post : allPosts) {
//                Global_Data.getInstance().insert(post.getTag(), post);
//            }

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

