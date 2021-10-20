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

/** Activity for users to view and update their profile,
 * Which includes User name, Interest, and a Caption (i.e., or a quote) */
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set Up Views
        userNameLayout = (TextInputLayout) findViewById(R.id.profile_input_userName);
        interestsLayout = (TextInputLayout) findViewById(R.id.profile_input_interests);
        captionLayout = (TextInputLayout) findViewById(R.id.profile_input_caption);
        userNameEdit = (EditText) findViewById(R.id.profile_input_displayName_text);
        interestsEdit = (EditText) findViewById(R.id.profile_input_interests_text);
        captionEdit = (EditText) findViewById(R.id.profile_input_caption_text);

        editProfile();
    }


    /**
     * OnClick method for the Confirm Button (Manage Profile)
     * @param v UI
     */
    public void ConfirmManageProfileBt(View v) {

        // Read Input, Ignore Empty fields
        String newName = userNameEdit.getText().toString().replace("\n", "");
        String newInterests = interestsEdit.getText().toString().replace("\n", "");
        String newCaption = captionEdit.getText().toString().replace("\n", "");
        if (TextUtils.isEmpty(newName)) {newName = currentUserName;}
        if (TextUtils.isEmpty(newInterests)) {newInterests = currentInterests;}
        if (TextUtils.isEmpty(newCaption)) {newCaption = currentCaption;}

        // ignore if all input fields are empty or same as current
        if (!(newName.equals(currentUserName) && newInterests.equals(currentInterests) && newCaption.equals(currentCaption))) {
            Toast.makeText(ProfileActivity.this, "No updates has been made", Toast.LENGTH_SHORT).show();

        // Update user profile
        } else {
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
        }

    }


    /**
     * Match existing User Profile on Firebase
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
                                if (currentInterests.length()==ZERO) continue;
                                userNameLayout = (TextInputLayout) findViewById(R.id.profile_input_userName);
                                userNameLayout.setHint("Edit User name [" + currentUserName + "]");
                                continue;
                            case "interests":
                                currentInterests = (String) ds.getValue();
                                if (currentInterests.length()==ZERO) continue;
                                interestsLayout = (TextInputLayout) findViewById(R.id.profile_input_interests);
                                interestsLayout.setHint("Edit Interests [" + currentInterests + "]");
                                continue;
                            case "caption":
                                currentCaption = (String) ds.getValue();
                                if (currentCaption.length()==ZERO) continue;
                                captionLayout = (TextInputLayout) findViewById(R.id.profile_input_caption);
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
            dbRef.addValueEventListener(userListener);

        } else {
            finish();   // exceptional case (not loggin in)
        }
    }


}