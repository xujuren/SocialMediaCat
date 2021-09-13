package anu.softwaredev.socialmediacat;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// Manage User Profile
public class ManageProfileActivity extends AppCompatActivity {

    FirebaseUser user;
    String currentEmail;
    String currentName;
    Uri currentProPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);

        /** Get the current Info of a User */
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in > try get INFO (https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseUser)
            currentEmail = user.getEmail();
            currentName = user.getDisplayName();
            currentProPic = user.getPhotoUrl();

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
        if ((TextUtils.isEmpty(newName) || newName.equals(currentName)) && (TextUtils.isEmpty(newProPic) || newProPic.equals(currentProPic))){
            Toast.makeText(ManageProfileActivity.this, "No updates has been made", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(ManageProfileActivity.this, "Updating Profile...", Toast.LENGTH_SHORT).show();
            // TODO
            return;
        }

    }


}