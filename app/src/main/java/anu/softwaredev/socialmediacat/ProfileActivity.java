package anu.softwaredev.socialmediacat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivityDao;

/** Activity for users to view and update their profile,
 * Which includes User name, Interest, and a Caption (i.e., a favourite quote) */

public class ProfileActivity extends AppCompatActivity {
    private FirebaseUser user;
    TextInputLayout userNameLayout ;
    TextInputLayout interestsLayout;
    TextInputLayout captionLayout;
    EditText userNameEdit;
    EditText interestsEdit;
    EditText captionEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Set Up Views
        userNameLayout = (TextInputLayout) findViewById(R.id.profile_userName_layout);
        interestsLayout = (TextInputLayout) findViewById(R.id.profile_interests_layout);
        captionLayout = (TextInputLayout) findViewById(R.id.profile_caption_layout);
        userNameEdit = (EditText) findViewById(R.id.profile_input_userName);
        interestsEdit = (EditText) findViewById(R.id.profile_input_interests);
        captionEdit = (EditText) findViewById(R.id.profile_input_caption);
        UserActivityDao.getInstance().findUserProfile(user, userNameLayout, interestsLayout, captionLayout);
    }

    /**
     * OnClick method for the Confirm Button (Manage Profile)
     * @param v UI
     */
    public void ConfirmManageProfileBt(View v) {
        String currentName = parseFields(userNameLayout);
        String currentInterests = parseFields(interestsLayout);
        String currentCaption = parseFields(captionLayout);

        // Read Input, Ignore Empty fields
        String newName = userNameEdit.getText().toString().replace("\n", "");
        String newInterests = interestsEdit.getText().toString().replace("\n", "");
        String newCaption = captionEdit.getText().toString().replace("\n", "");
        if ("".equals(newName)) {newName = currentName; }
        if ("".equals(newInterests)) {newInterests = currentInterests; }
        if ("".equals(newCaption)) { newCaption = currentCaption; }

        // ignore if all input fields are empty or same as current
        if (newName.equals(currentName) && newInterests.equals(currentInterests) && newCaption.equals(currentCaption)) {
            Toast.makeText(ProfileActivity.this, "No updates has been made", Toast.LENGTH_SHORT).show();
        // Update user profile
        } else {
            UserActivityDao.getInstance().updateProfile(getApplicationContext(), user, newName, newInterests, newCaption);
        }
    }

    /**
     * get the content of attributes from the grammar of display
     * trivial parsing
     */
    private String parseFields(TextInputLayout layout){
        String text = layout.getHint().toString();
        int indexPos = text.indexOf('[');
        System.out.println("indexPos: " + indexPos);
        if (indexPos==-1) return "";
        return text.substring(indexPos+1, text.length()-1);
    }



}