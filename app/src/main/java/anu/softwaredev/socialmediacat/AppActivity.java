package anu.softwaredev.socialmediacat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AppActivity extends AppCompatActivity {

    FirebaseUser user;
    String userName;                // user's displayName (if available) or e-mail address

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (!TextUtils.isEmpty(user.getDisplayName())) {
            userName = user.getDisplayName();
        } else {
            /** Extract user's email (from Login) */
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
               userName = extras.getString("userEmail");       // key arg: match that used in the other activity
            }
        }
        // Display Welcome Message
        TextView welcomeText = (TextView) findViewById(R.id.tv_welcome_appAct);
        welcomeText.setText("Welcome, " + userName);


    }

    // Set Up the [Log Out Button] > return to Main Page
    public void logOutClick(View v) {
        FirebaseAuth.getInstance().signOut();                   // FirebaseAuth: signOut
        Intent intent = new Intent();                           // return to Main Page
        intent.setClass(AppActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    // Set Up the [to Timeline] Button
    public void toTimelineAct(View v){          // [c] need public
        Intent i = new Intent(getApplicationContext(), TimelineActivity.class);        // ori: AppActivity.this
        startActivity(i);
        // finish();
    }

    // Set Up the [Manage Profile] Button
    public void toManageProfile(View v){          // [c] need public
        Intent i = new Intent(getApplicationContext(), ManageProfileActivity.class);
        startActivity(i);
        // finish();
    }



}