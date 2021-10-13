package anu.softwaredev.socialmediacat.Classes;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import anu.softwaredev.socialmediacat.ProfileActivity;
import anu.softwaredev.socialmediacat.R;
import anu.softwaredev.socialmediacat.dao.decorator.User;

public class PostActivity extends AppCompatActivity {

//    categoryLayout = (TextInputLayout) findViewById(R.id.profile_input_displayName);
//    postIdLayout = (TextInputLayout) findViewById(R.id.profile_input_proPic);
//    titleLayout = (TextInputLayout) findViewById(R.id.profile_input_caption);
//    contentLayout = (TextInputLayout) findViewById(R.id.profile_input_caption);
//    likeCountLayout = (TextInputLayout) findViewById(R.id.profile_input_caption);
//    uIdLayout = (TextInputLayout) findViewById(R.id.profile_input_caption);
//    photoLayout = (TextInputLayout) findViewById(R.id.profile_input_caption);

//    public String category;         // TODO > enum
//    public String postId;           // TODO > to be determined [Category + postId] = key
//    public String title;            // Post Title
//    public String content;          // Post Content
//    public int likeCount=0;           // no. of likes
//    public String uId;              // [user ID] of author
//    // TODO - Photo (see how we manage resources)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
    }
//
//    // Get the current Info of a User
//    user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//        String userId = user.getUid();
//        userDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
//        // Syn
//        ValueEventListener userListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                // Create Profile if not exists
//                if (!snapshot.exists() || !snapshot.hasChildren()) {
//                    Toast.makeText(ProfileActivity.this, "(Setting up profile on Database ...)", Toast.LENGTH_LONG).show();
//                    // create record on realtime db
//                    user = FirebaseAuth.getInstance().getCurrentUser();
//                    User newUser = new User(user.getUid(), user.getEmail());                            // Set Up a [User] obj ([Uid] as KEY of user info)
//                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();            // path to store user data (named "Users")
//                    dbRef.child("Users").child(user.getUid()).setValue(newUser);
//                }
//
//                // Get each existing field (of user)
//                for (DataSnapshot ds : snapshot.getChildren()) {    // loop thru each [field (k,v)] of the [uesr(UID)]
//                    String k = ds.getKey();
//                    switch (k) {
//                        case "name" :
//                        case "userName" :
//                            currentUserName = (String) ds.getValue();
//                            userNameLayout = (TextInputLayout) findViewById(R.id.profile_input_displayName);
//                            userNameLayout.setHint("Edit your display name (current: " + currentUserName + ")");
//                            continue;
//                        case "email":                                   // n/a now
//                            currentEmail = (String) ds.getValue();
//                            continue;
//                        case "proPic":
//                            currentProPic = (String) (ds.getValue());
//                            if (currentProPic.length()==0) continue;
//                            proPicLayout = (TextInputLayout) findViewById(R.id.profile_input_proPic);
//                            proPicLayout.setHint("Edit your profile picture (current link: " + currentProPic + ")");
//                            // TD: show the PICTURE
//                            continue;
//                        case "caption":
//                            currentCaption = (String) (ds.getValue());
//                            if (currentCaption.length()==0) continue;
//                            captionLayout = (TextInputLayout) findViewById(R.id.profile_input_caption);
//                            captionLayout.setHint("Edit your caption [" + currentCaption + "]");
//                            continue;
//                        default:
//                            continue;
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Getting anu.softwaredev.socialmediacat.Classes.Post failed, log a message
//                Log.w(TAG, "read failed (onCancelled)", error.toException());
//            }
//        };
//
//        userDbRef.addValueEventListener(userListener);
//
//    } else {
//        finish();   // Not signed in (unexpected case)
//    }

    // Set Up Views
//    public void initViews(){
//        userNameLayout = (TextInputLayout) findViewById(R.id.profile_input_displayName);
//        proPicLayout = (TextInputLayout) findViewById(R.id.profile_input_proPic);
//        captionLayout = (TextInputLayout) findViewById(R.id.profile_input_caption);
//        displayNameEdit = (EditText) findViewById(R.id.profile_input_displayName_text);
//        proPicEdit = (EditText) findViewById(R.id.profile_input_proPic_text);
//        captionEdit = (EditText) findViewById(R.id.profile_input_caption_text);

//    }



}