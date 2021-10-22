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

/**
 * Data Access Object for UserActivity
 * Realise the data access and persistence processes via Firebase,
 * and also partly with the local Data Structure (RB Tree of Posts)
 */
public class UserActivityDao implements IUserActivityDao {
    private static DatabaseReference dbRef;
    private static UserActivityDao instance;        // Singleton instance for UserActivityDao
    private UserActivityDao() {
        //this.deleteAll();
    }
    public static UserActivityDao getInstance(){
        if (instance == null) {
            instance = new UserActivityDao();
            dbRef = FirebaseDatabase.getInstance().getReference();
        }
        return instance;
    }

    /** Create Posts in response to user's action */
    @Override
    public void createPost(String uId, String tag, String content, int photoId) {           // alt: only content (userName: below)
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

    }

    /** Store the loaded Posts */
    public void storePost(List<Post> posts) {

        for (Post post : posts){
            if (post==null || post.getTag()==null) {break;}

            // Update firebase DB
            Map<String, Object> postValues = post.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/Posts/" + post.getPostId(), postValues);
            dbRef.updateChildren(childUpdates);

            // Update Data Structure (insert current post into the RB-tree)
            Global_Data.getInstance().insert(post);
            FirebaseAuth user = FirebaseAuth.getInstance();
            if (post.getUId().equals(user.getUid())){
                Global_Data.getInstance().add_My_Posts(post);
            }
        }
    }


    /** Like Post (in response to user's action) */
    @Override
    public void likePost(String postId) {
        // Update firebase DB
        Map<String, Object> updates = new HashMap<>();
        updates.put("/likeCount", ServerValue.increment(1));
        dbRef.child("Posts").child(postId).updateChildren(updates);
    }

    /** Like Post (in response to user's action) */
    @Override
    public void unlikePost(String postId) {
        // Update firebase DB
        Map<String, Object> updates = new HashMap<>();
        updates.put("/likeCount", ServerValue.increment(-1));
        dbRef.child("Posts").child(postId).updateChildren(updates);
    }

    /** Like Post (in response to user's action) */
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


    /**
     * Find the Profile of provided userId on Firebase.
     */
    @Override
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
     * Find the profile of the provided user on Firebase.
     */
    @Override
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
    @Override
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

