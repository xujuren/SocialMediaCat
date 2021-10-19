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
    private String currentInterests;
    private String currentCaption;
    TextInputLayout userNameLayout ;
    TextInputLayout interestsLayout;
    TextInputLayout captionLayout;
    EditText userNameEdit;
    EditText interestsEdit;
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

        editProfile();
    }

    /**
     * edit profile related actions
     */
    private void editProfile() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Create record
                    if (!snapshot.exists() || !snapshot.hasChildren()) {
                        User newUser = new User(user.getUid(), user.getEmail());
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                        dbRef.child("Users").child(user.getUid()).setValue(newUser);
                    }

                    // Get and display fields
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String k = ds.getKey();
                        switch (k) {
                            case "userName" :
                                currentUserName = (String) ds.getValue();
                                userNameLayout = (TextInputLayout) findViewById(R.id.profile_input_userName);
                                userNameLayout.setHint("Edit your User name [current: " + currentUserName + "]");
                                continue;
                            case "interests":
                                currentInterests = (String) (ds.getValue());
                                if (currentInterests.length()==ZERO) continue;
                                interestsLayout = (TextInputLayout) findViewById(R.id.profile_input_interests);
                                interestsLayout.setHint("Edit your profile picture [" + currentInterests + "]");
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
            finish();   // exceptional case
        }
    }


    /**
     * Confirm Button (Manage Profile)
     * @param v UI
     */
    public void profileInput(View v) {

        // Read Input
        String newName = userNameEdit.getText().toString();
        String newInterests = interestsEdit.getText().toString();
        String newCaption = captionEdit.getText().toString();

        if (TextUtils.isEmpty(newName)) {newName = currentUserName;}
        if (TextUtils.isEmpty(newInterests)) {newInterests = currentInterests;}
        if (TextUtils.isEmpty(newCaption)) {newCaption = currentCaption;}

        // ignore if all input fields are either empty or same as current
        if (!(newName.equals(currentUserName) && newInterests.equals(currentInterests) && newCaption.equals(currentCaption))){
            // Update user profile
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .setPhotoUri(Uri.parse(newInterests))
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                                Toast.makeText(ProfileActivity.this, "Your profile has been updated!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            // Update Data
            dbRef.child("userName").setValue(newName);
            dbRef.child("caption").setValue(newCaption);
            dbRef.child("interests").setValue(newInterests);

            return;

        } else {
            Toast.makeText(ProfileActivity.this, "No updates has been made", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {
        userNameLayout = (TextInputLayout) findViewById(R.id.profile_input_userName);
        interestsLayout = (TextInputLayout) findViewById(R.id.profile_input_interests);
        captionLayout = (TextInputLayout) findViewById(R.id.profile_input_caption);
        userNameEdit = (EditText) findViewById(R.id.profile_input_displayName_text);
        interestsEdit = (EditText) findViewById(R.id.profile_input_interests_text);
        captionEdit = (EditText) findViewById(R.id.profile_input_caption_text);

    }


}