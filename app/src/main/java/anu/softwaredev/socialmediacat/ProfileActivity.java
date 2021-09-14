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

// Manage User Profile
public class ProfileActivity extends AppCompatActivity {
    private FirebaseUser user;
    private String currentEmail;
    private String currentName;
    private Uri currentProPic;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /** Get the current Info of a User */
        // (reconfirm User signed in) > try get INFO (https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseUser)
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            /** M2 - get DATA from database */
            String userId = user.getUid();      // Tester: "17J7kImXC9XudMSe35KteHzj7aa2"
            mDatabase = FirebaseDatabase.getInstance().getReference("Users/" + userId);
            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Get [User] object and use the values to update the UI        (??)
                    User userFromDb = snapshot.getValue(User.class);
                    // System.out.println(userFromDb);     // test this instead
                    // currentEmail = userFromDb.getEmailAddress();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "read failed (onCancelled)", error.toException());
                }
            };
            mDatabase.addValueEventListener(userListener);



            /** M1 - user.getFields() @Authn*/
//            currentEmail = user.getEmail();
//            currentName = user.getDisplayName();
//            currentProPic = user.getPhotoUrl();

            TextInputLayout userNameLayout = (TextInputLayout) findViewById(R.id.profile_input_userName);
            TextInputLayout proPicLayout = (TextInputLayout) findViewById(R.id.profile_input_proPic);
            TextInputLayout birthYearLayout = (TextInputLayout) findViewById(R.id.profile_input_birthYear);
            //TODO:
            if (currentName==null) {
                userNameLayout.setHint("Set your display name");
            } else {
                userNameLayout.setHint("Edit your display name (current: " + currentName + ")");
            }

            if (currentProPic==null) {
                proPicLayout.setHint("Set your profile picture: Enter a link");
            } else {    // TD: show picture
                proPicLayout.setHint("Edit your profile picture (current: " + currentProPic + ")");
            }

            // yearOfBirthLayout.setHint("Input your year of birth " + "(Uri: " + currentProPic);

        } else {
            // No user is signed in
            finish();
        }

    }

    // button [Confirm: edit profile]
    public void profileInput(View v) {
        EditText nameEdit = (EditText) findViewById(R.id.profile_input_userName_text);
        EditText proPicEdit = (EditText) findViewById(R.id.profile_input_proPic_text);
        // EditText birthYearEdit = (EditText) findViewById(R.id.profile_input_birthYear_text);

        // Read Input
        String newName = nameEdit.getText().toString();
        String newProPic = proPicEdit.getText().toString();

        // if all input fields are either empty or same as current
        if ((TextUtils.isEmpty(newName) || newName.equals(currentName)) && (TextUtils.isEmpty(newProPic) || newProPic.equals(currentProPic.toString()))){
            Toast.makeText(ProfileActivity.this, "No updates has been made", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ProfileActivity.this, "Updating Profile...", Toast.LENGTH_SHORT).show();
            // TODO: add Checkings
            if (TextUtils.isEmpty(newName)) {newName = currentName;}
            if (TextUtils.isEmpty(newProPic)) {newProPic = currentProPic.toString();}

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


            return;
        }

    }


}