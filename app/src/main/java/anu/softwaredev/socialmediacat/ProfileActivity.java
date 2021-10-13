package anu.softwaredev.socialmediacat;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.firebase.database.ValueEventListener;

import anu.softwaredev.socialmediacat.dao.decorator.User;

// Manage User Profile
public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference userDbRef;        // database Reference to the [User] node

    private String currentUserName;
    private String currentProPic;
    private String currentCaption;
    private String currentEmail;

    TextInputLayout userNameLayout ;           // [C] can't set (type) findViewById() here: not yet link?
    TextInputLayout proPicLayout ;
    TextInputLayout captionLayout;
    EditText displayNameEdit ;
    EditText proPicEdit ;
    EditText captionEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set Up Views
        userNameLayout = (TextInputLayout) findViewById(R.id.profile_input_displayName);
        proPicLayout = (TextInputLayout) findViewById(R.id.profile_input_proPic);
        captionLayout = (TextInputLayout) findViewById(R.id.profile_input_caption);
        displayNameEdit = (EditText) findViewById(R.id.profile_input_displayName_text);
        proPicEdit = (EditText) findViewById(R.id.profile_input_proPic_text);
        captionEdit = (EditText) findViewById(R.id.profile_input_caption_text);

        // Get the current Info of a User
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            userDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
            // Syn
            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    // Create Profile if not exists
                    if (!snapshot.exists() || !snapshot.hasChildren()) {
                        Toast.makeText(ProfileActivity.this, "(Setting up profile on Database ...)", Toast.LENGTH_LONG).show();
                        // create record on realtime db
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        User newUser = new User(user.getUid(), user.getEmail());                            // Set Up a [User] obj ([Uid] as KEY of user info)
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();            // path to store user data (named "Users")
                        dbRef.child("Users").child(user.getUid()).setValue(newUser);
                    }

                    // Get each existing field (of user)
                    for (DataSnapshot ds : snapshot.getChildren()) {    // loop thru each [field (k,v)] of the [uesr(UID)]
                        String k = ds.getKey();
                        switch (k) {
                            case "name" :
                            case "userName" :
                                currentUserName = (String) ds.getValue();
                                userNameLayout = (TextInputLayout) findViewById(R.id.profile_input_displayName);
                                userNameLayout.setHint("Edit your display name (current: " + currentUserName + ")");
                                continue;
                            case "email":                                   // n/a now
                                currentEmail = (String) ds.getValue();
                                continue;
                            case "proPic":
                                currentProPic = (String) (ds.getValue());
                                if (currentProPic.length()==0) continue;
                                proPicLayout = (TextInputLayout) findViewById(R.id.profile_input_proPic);
                                proPicLayout.setHint("Edit your profile picture (current link: " + currentProPic + ")");
                                // TD: show the PICTURE
                                continue;
                            case "caption":
                                currentCaption = (String) (ds.getValue());
                                if (currentCaption.length()==0) continue;
                                captionLayout = (TextInputLayout) findViewById(R.id.profile_input_caption);
                                captionLayout.setHint("Edit your caption [" + currentCaption + "]");
                                continue;
                            default:
                                continue;
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Getting anu.softwaredev.socialmediacat.Classes.Post failed, log a message
                    Log.w(TAG, "read failed (onCancelled)", error.toException());
                }
            };

            userDbRef.addValueEventListener(userListener);

        } else {
            finish();   // Not signed in (unexpected case)
        }

    }

    // button [Confirm: edit profile]
    public void profileInput(View v) {

        // Read Input
        String newName = displayNameEdit.getText().toString();
        String newProPic = proPicEdit.getText().toString();
        String newCaption = captionEdit.getText().toString();

        // TODO: add Checkings [if new=empty / invalid]
        if (TextUtils.isEmpty(newName)) {newName = currentUserName;}
        if (TextUtils.isEmpty(newProPic)) {newProPic = currentProPic;}
        if (TextUtils.isEmpty(newCaption)) {newCaption = currentCaption;}

        // if all input fields are either empty or same as current
        if (!(newName.equals(currentUserName) && newProPic.equals(currentProPic) && newCaption.equals(currentCaption))){
            Toast.makeText(ProfileActivity.this, "Updating Profile...", Toast.LENGTH_SHORT).show();

            // User @ Authen
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .setPhotoUri(Uri.parse(newProPic))
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                                Toast.makeText(ProfileActivity.this, "User profile updated.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            // Update Data
            userDbRef.child("name").setValue(newName);
            userDbRef.child("displayName").setValue(newName);
            userDbRef.child("proPic").setValue(newProPic);
            userDbRef.child("caption").setValue(newCaption);

            return;

        } else {
            Toast.makeText(ProfileActivity.this, "No updates has been made", Toast.LENGTH_SHORT).show();
        }

    }


}