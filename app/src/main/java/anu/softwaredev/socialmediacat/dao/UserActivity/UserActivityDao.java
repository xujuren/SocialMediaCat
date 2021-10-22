package anu.softwaredev.socialmediacat.dao.UserActivity;
import static android.content.ContentValues.TAG;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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

/** Dao for UserActivity */
public class UserActivityDao implements IUserActivityDao {
    private static DatabaseReference dbRef;
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
            dbRef = FirebaseDatabase.getInstance().getReference();
        }
        return instance;
    }


    @Override
    public void createPost(String uId, String tag, String content, int photoId) {           // alt: only content (userName: below)
        try {
            // Check invalid characters in tag (empty if invalid)
            if (tag.contains(",") || tag.contains(";")) {tag="";}

            // Check invalid characters in content (remove inner "'s), then double quote for format alignment
            content = content.replaceAll("\"", "");
            content = "\"" + content + "\"";

            // Update firebase DB
            String postId = dbRef.child("Posts").push().getKey();             // unique Key for Posts
            Post newPost = new Post(uId, tag, postId, content, photoId);
            Map<String, Object> postValues = newPost.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/Posts/" + postId, postValues);
            dbRef.updateChildren(childUpdates);

            // Update Data Structure (insert current post into the RB-tree)
            Global_Data.getInstance().insert(newPost);

            FirebaseAuth user = FirebaseAuth.getInstance();
            if (newPost.getUId().equals(user.getUid())){
                Global_Data.getInstance().add_My_Posts(newPost);
            }

            // Write to file
            String text = "CP" + ";" + uId + ";" + tag + ";" + postId + ";" + content + ";" + photoId + ";" + "0" + "\n";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
            }
            System.out.println("Post created saved in " + file.getAbsolutePath());

        } catch (IOException e) { e.printStackTrace(); }

    }


    /** Load Posts for display */
    public void storePost(List<Post> posts) {

        for (Post post : posts){
            if (post==null || post.getTag()==null) {break;}

            // Update firebase DB
            Map<String, Object> postValues = post.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/Posts/" + post.getPostId(), postValues);
            dbRef.updateChildren(childUpdates);

            ///////////
            System.out.println("DAO : INSERT");
            System.out.println("Global_Data.getInstance(): " + Global_Data.getInstance());
            System.out.println(".insert(post) - post: " + post);


            // Update Data Structure (insert current post into the RB-tree)
            Global_Data.getInstance().insert(post);
            FirebaseAuth user = FirebaseAuth.getInstance();
            if (post.getUId().equals(user.getUid())){
                Global_Data.getInstance().add_My_Posts(post);
            }

            // write to file
            try {
                String text = "SP" + ";" + post.getUId() + ";" + post.getTag() + ";" + post.getPostId() + ";" + post.getContent() + ";" + post.getPhotoId() + ";" + post.getLikeCount() + "\n";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
                }
                System.out.println("Post loaded to " + file.getAbsolutePath());

            } catch (IOException e) { e.printStackTrace(); }
        }

    }


    @Override
    public void likePost(String postId) {
        // Update firebase DB
        Map<String, Object> updates = new HashMap<>();
        updates.put("/likeCount", ServerValue.increment(1));
        dbRef.child("Posts").child(postId).updateChildren(updates);
    }


    public void unlikePost(String postId) {
        // Update firebase DB
        Map<String, Object> updates = new HashMap<>();
        updates.put("/likeCount", ServerValue.increment(-1));
        dbRef.child("Posts").child(postId).updateChildren(updates);
    }

    @Override
    public void deletePost(String postId) {
        // Update firebase DB
        dbRef.child("Posts").addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (ds.getKey().equals(postId)){
                        ds.getRef().removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
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
                    if (items!=null && items.length==7 && ("CP".equals(items[0]) || "SP".equals(items[0]) )) {
                        Post post = new Post(items[1], items[2], items[3], items[4], Integer.parseInt(items[5]), Integer.parseInt(items[6]));
                        postsLoaded.add(post);

                        // Insert all posts to the global Data Structure of Posts
                        // TODO - try delete (10/21):
                        //  Global_Data.getInstance().insert(post);
                        // TODO

                        // TODO
                        // New, for locally created posts of User
//                        FirebaseAuth user = FirebaseAuth.getInstance();
//                        if (post.getUId().equals(user.getUid())){
//                            Global_Data.getInstance().add_My_Posts(post);
//                        }

                    }
                }
            }


        } catch (IOException e) { e.printStackTrace(); }

        /** TODO
         * For test, need to delete
         */
        System.out.println("============================================Check data========================================");
//        Global_Data.getInstance().getData().find("random").getPostsTree().inorderPrint(Global_Data.getInstance().getData().find("random").getPostsTree().root);

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


    /**
     * Find the Profile of provided userId on Firebase,
     * and display the corresponding information
     */
    public void findUserProfile(String userId, TextView uIdTv, TextView captionTv) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("snapshot: "+snapshot.getKey() + ", value: " + snapshot.getValue());
                // Try to match Firebase Records
                if (snapshot.exists() && snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String k = ds.getKey();
                        System.out.println("key: "+k);
                        switch (k) {
                            case "userName" :
                                String userName = (String) ds.getValue();
                                if (userName!=null && userName.length()>0 && !userName.equals("null")){
                                    uIdTv.setText("@"+userName);
                                    System.out.println("uIdTv: " + userName);
                                } else {
                                    uIdTv.setText("@"+userId);
                                }
                                continue;
                            case "caption":
                                String caption = (String) ds.getValue();
                                if (caption!=null && caption.length()>0 && !caption.equals("null")){
                                    captionTv.setText(caption);
                                } else {
                                    captionTv.setText("");
                                }
                                continue;
                            default:
                                continue;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Firebase Read Fail", error.toException());
            }
        };
        dbRef.child("Users").child(userId).addValueEventListener(listener);
    }

    /**
     * Find the profile of the provided user on Firebase,
     * and display the corresponding information
     */
    public void findUserProfile(FirebaseUser user, TextInputLayout userNameLayout, TextInputLayout interestsLayout, TextInputLayout captionLayout) {
        if (user != null) {
            String userId = user.getUid();
            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Create record
                    if (!snapshot.exists() || !snapshot.hasChildren()) {
                        User newUser = new User(userId, user.getEmail());
                        dbRef.child("Users").child(userId).setValue(newUser);
                    }

                    // Get and display fields
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String k = ds.getKey();
                        switch (k) {
                            case "userName" :
                                String currentUserName = (String) ds.getValue();
                                if (currentUserName.length()==0) continue;
                                userNameLayout.setHint("Edit User Name [" + currentUserName + "]");
                                continue;
                            case "interests":
                                String currentInterests = (String) ds.getValue();
                                if (currentInterests.length()==0) continue;
                                interestsLayout.setHint("Edit Interests [" + currentInterests + "]");
                                continue;
                            case "caption":
                                String currentCaption = (String) ds.getValue();
                                if (currentCaption.length()==0) continue;
                                captionLayout.setHint("Edit Caption [" + currentCaption + "]");
                                continue;
                            default:
                                continue;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "Firebase Read Fail", error.toException());
                }
            };
            dbRef.child("Users").child(userId).addValueEventListener(userListener);
        }
    }

    /**
     * Update User Profile on Firebase
     */
    public void updateProfile(Context ctx, FirebaseUser user, String newName, String newInterests, String newCaption) {
        // Update Profile
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ctx, "Your profile has been updated!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Update Data
        String uId = user.getUid();
        dbRef.child("Users").child(uId).child("userName").setValue(newName);
        dbRef.child("Users").child(uId).child("caption").setValue(newCaption);
        dbRef.child("Users").child(uId).child("interests").setValue(newInterests);
    }

}

