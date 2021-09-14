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

import java.util.ArrayList;
import java.util.HashMap;

// Manage User Profile
public class ProfileActivity extends AppCompatActivity {
    private FirebaseUser user;
    private String currentEmail;
    private String currentName;
    private Uri currentProPic;
    private DatabaseReference userDbRef;        // database Reference to the [User] node

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /** Get the current Info of a User */
        // (reconfirm User signed in) > try get INFO (https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseUser)
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            /** M2 - get DATA from database */
            String userId = user.getUid();      // testing@doggo.com: "sibHOgo1a2Qzx3Jxui91ugzhqB63"
            userDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

            /* M1 (ValueEventListener): Syn */
            ArrayList<String> list = new ArrayList<String>();
            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, Object> fieldPairs = new HashMap<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key = ds.getKey();
                        String value = (String) ds.getValue();
                        fieldPairs.put(key, value);
                        // Toast.makeText(getApplicationContext(), "got: " + anElemFromSnapShot, Toast.LENGTH_SHORT);
                    }

                    /** Test: got name etc. ? format? */
                    for (String k : fieldPairs.keySet()) {
                        if (k.equals("name")) {
                            userDbRef.child("proPci").setValue(fieldPairs.get(k));
                            return;
                        }

                    }

                    // [ERROR] Get [User] object and use the values to update the UI        (??)
                    // User userFromDb = snapshot.getValue(User.class);          // .getValue(User.class)
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Getting anu.softwaredev.socialmediacat.Post failed, log a message
                    Log.w(TAG, "read failed (onCancelled)", error.toException());
                }
            };
            userDbRef.addValueEventListener(userListener);

            /* M2 (get) */
            userDbRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        /** where's the data ???*/

                    }
                }
            });


            /** M1 - user.getFields() @Authn*/
//            currentName = user.getDisplayName();
//            currentProPic = user.getPhotoUrl();

            // dummies
            currentName = "-";
            currentProPic = Uri.parse("-");

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

            /** Add (to replace): UPDATE DB */
            // TODO align field name (name / userName / displayName)
            userDbRef.child("name").setValue(newName);



            return;
        }

    }


}