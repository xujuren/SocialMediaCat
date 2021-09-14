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
    private DatabaseReference userDbRef;        // database Reference to the [User] node
    private String currentEmail;
    private String currentDisplayName;
    private String currentProPic;
    private int currentbirthYear;

    TextInputLayout userNameLayout ;           // [C] can't set (type) findViewById() here: not yet link?
    TextInputLayout proPicLayout ;
    TextInputLayout birthYearLayout ;
    EditText nameEdit ;
    EditText proPicEdit ;
    EditText birthYearEdit ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userNameLayout = (TextInputLayout) findViewById(R.id.profile_input_userName);
        proPicLayout = (TextInputLayout) findViewById(R.id.profile_input_proPic);
        birthYearLayout = (TextInputLayout) findViewById(R.id.profile_input_birthYear);
        nameEdit = (EditText) findViewById(R.id.profile_input_userName_text);
        proPicEdit = (EditText) findViewById(R.id.profile_input_proPic_text);
        birthYearEdit = (EditText) findViewById(R.id.profile_input_birthYear_text);

        // Default Hint (w/o fields' current value)
        userNameLayout.setHint("Set your display name");    // default, before replaced by sentence w/ DB loaded [displayName]
        proPicLayout.setHint("Set your profile picture: Enter a link");
        birthYearLayout.setHint("Set your year of birth");


        /** Get the current Info of a User */
        // (reconfirm User signed in) > try get INFO (https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseUser)
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            /** M2 - get DATA from database */
            String userId = user.getUid();      // testing@doggo.com: "sibHOgo1a2Qzx3Jxui91ugzhqB63"
            userDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

            /* M1 (ValueEventListener): Syn */
            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String k = ds.getKey();
                        switch (k) {
                            case "name":
                                currentDisplayName = (String) ds.getValue();
                                userNameLayout = (TextInputLayout) findViewById(R.id.profile_input_userName);
                                userNameLayout.setHint("Edit your display name (current: " + currentDisplayName + ")");
                                continue;
                            case "emailAddress":
                                currentEmail = (String) ds.getValue();
                                continue;
                            case "proPic":
                                currentProPic = (String) (ds.getValue());
                                proPicLayout = (TextInputLayout) findViewById(R.id.profile_input_proPic);
                                proPicLayout.setHint("Edit your profile picture (current link: " + currentProPic + ")");
                                // TD: show the PICTURE
                                continue;
                            case "birthYear":
                                currentbirthYear = (int) (ds.getValue());
                                birthYearLayout = (TextInputLayout) findViewById(R.id.profile_input_birthYear);
                                birthYearLayout.setHint("Edit your profile picture (current link: " + currentbirthYear + ")");
                                continue;
                            default:
                                continue;
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


            /* M1 (get) */
//            userDbRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                    if (!task.isSuccessful()) {
//                        Log.e("firebase", "Error getting data", task.getException());
//                    }
//                    else {
//                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                        /** where's the data ???*/
//
//                    }
//                }
//            });

            /* M0 - user.getFields() @Authn*/
//            currentName = user.getDisplayName();
//            currentProPic = user.getPhotoUrl();


        } else {    // No user is signed in
            finish();
        }

    }

    // button [Confirm: edit profile]
    public void profileInput(View v) {

        // Read Input
        String newName = nameEdit.getText().toString();
        String newProPic = proPicEdit.getText().toString();
        // int newBirthYear = birthYearEdit.getText().toString(); ...

        // if all input fields are either empty or same as current
        if ((TextUtils.isEmpty(newName) || newName.equals(currentDisplayName)) && (TextUtils.isEmpty(newProPic) || newProPic.equals(currentProPic.toString()))){
            Toast.makeText(ProfileActivity.this, "No updates has been made", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ProfileActivity.this, "Updating Profile...", Toast.LENGTH_SHORT).show();

            // TODO: add Checkings
            if (TextUtils.isEmpty(newName)) {newName = currentDisplayName;}
            if (TextUtils.isEmpty(newProPic)) {newProPic = currentProPic;}

            /** Update [M1: user @authn]*/
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

            /** [M2*] update fields in Realtime DB */
            // TODO align field name (name / userName / displayName)
            userDbRef.child("name").setValue(newName);
            // userDbRef.child("proPic").setValue(newProPic);

            return;
        }

    }


}