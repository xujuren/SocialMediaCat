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

import anu.softwaredev.socialmediacat.Classes.User;

/** Activity for users to manage their profile: update information */
public class ProfileActivity extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference dbRef;
    private String currentUserName;
    private String currentProPic;
    private String currentCaption;
    TextInputLayout userNameLayout ;
    TextInputLayout proPicLayout ;
    TextInputLayout captionLayout;
    EditText displayNameEdit ;
    EditText proPicEdit ;
    EditText captionEdit;
    public static final int ZERO = 0;

    /**
     * main method, put all logic inside
     * @param savedInstanceState android unique class (Cloneable, Parcelable)saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();
        editProfit();
    }

    /**
     * edit profile related actions
     */
    private void editProfit() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Create record
                    if (!snapshot.exists() || !snapshot.hasChildren()) {
                        Toast.makeText(ProfileActivity.this, "(Setting up your profile ...)", Toast.LENGTH_LONG).show();
                        User newUser = new User(user.getUid(), user.getEmail());                            // Set Up User

                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                        dbRef.child("Users").child(user.getUid()).setValue(newUser);
                    }

                    // Get and display fields
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String k = ds.getKey();
                        switch (k) {
                            case "userName" :
                                currentUserName = (String) ds.getValue();
                                userNameLayout = (TextInputLayout) findViewById(R.id.profile_input_displayName);
                                userNameLayout.setHint("Edit your User name [current: " + currentUserName + "]");
                                continue;
                            case "proPic":
                                currentProPic = (String) (ds.getValue());
                                if (currentProPic.length()==ZERO) continue;
                                proPicLayout = (TextInputLayout) findViewById(R.id.profile_input_proPic);
                                proPicLayout.setHint("Edit your profile picture [" + currentProPic + "]");
                                // ToDo: show the PICTURE
                                continue;
                            case "caption":
                                currentCaption = (String) (ds.getValue());
                                if (currentCaption.length()==ZERO) continue;
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

            dbRef.addValueEventListener(userListener);

        } else {
            finish();   // Not signed in (unexpected case)
        }
    }


    /**
     * Confirm Button (Manage Profile)
     * @param v UI
     */
    private void profileInput(View v) {

        // Read Input
        String newName = displayNameEdit.getText().toString();
        String newProPic = proPicEdit.getText().toString();
        String newCaption = captionEdit.getText().toString();

        if (TextUtils.isEmpty(newName)) {newName = currentUserName;}
        if (TextUtils.isEmpty(newProPic)) {newProPic = currentProPic;}
        if (TextUtils.isEmpty(newCaption)) {newCaption = currentCaption;}

        // ignore if all input fields are either empty or same as current
        if (!(newName.equals(currentUserName) && newProPic.equals(currentProPic) && newCaption.equals(currentCaption))){
            Toast.makeText(ProfileActivity.this, "Updating Profile...", Toast.LENGTH_SHORT).show();

            // Update user profile
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
            dbRef.child("name").setValue(newName);
            dbRef.child("displayName").setValue(newName);
            dbRef.child("proPic").setValue(newProPic);
            dbRef.child("caption").setValue(newCaption);

            return;

        } else {
            Toast.makeText(ProfileActivity.this, "No updates has been made", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {
        userNameLayout = (TextInputLayout) findViewById(R.id.profile_input_displayName);
        proPicLayout = (TextInputLayout) findViewById(R.id.profile_input_proPic);
        captionLayout = (TextInputLayout) findViewById(R.id.profile_input_caption);
        displayNameEdit = (EditText) findViewById(R.id.profile_input_displayName_text);
        proPicEdit = (EditText) findViewById(R.id.profile_input_proPic_text);
        captionEdit = (EditText) findViewById(R.id.profile_input_caption_text);

    }


}