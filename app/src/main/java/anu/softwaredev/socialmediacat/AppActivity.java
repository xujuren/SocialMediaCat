package anu.softwaredev.socialmediacat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivityDao;

/** The main page of the app for logged-in Users */
public class AppActivity extends AppCompatActivity {
    private FirebaseUser user;              // User on Firebase
    private String userName;                // user's displayName (if available) or e-mail address

    /**
     * main method, put all logic inside
     * @param savedInstanceState android unique class (Cloneable, Parcelable)saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        // get userName, or userEmail if userName not available
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (!TextUtils.isEmpty(user.getDisplayName())) {
            userName = user.getDisplayName();
        } else {
            userName = user.getEmail();
        }

        // set welcome text
        TextView welcomeText = (TextView) findViewById(R.id.tv_welcome_appAct);
        welcomeText.setText("Welcome, " + userName);
    }

    /**
     * Set up buttons
     * @param v , UI botton
     */
    public void logOutClick(View v) {
        FirebaseAuth.getInstance().signOut();                   // signOut
        Intent intent = new Intent();                           // return to Main Page
        intent.setClass(AppActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * switch to timeline page
     * @param v UI
     */
    public void toTimelineAct(View v){
        Intent i = new Intent(getApplicationContext(), TimelineActivity.class);        // ori: AppActivity.this
        i.putExtra("MESSAGE", true);
        startActivity(i);
    }

    /**
     * switch to manageProfile page
     * @param v UI
     */
    public void toManageProfile(View v){
        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
        this.finish();
        startActivity(i);
    }

    /**
     * switch to createPost page
     * @param v UI
     */
    public void toCreatePost(View v){
        Intent i = new Intent(getApplicationContext(), CreatePost.class);
        startActivity(i);
    }


    /**
     * switch to myPost page
     * @param v UI
     */
    public void toMyPost(View v) {
        Intent i = new Intent(getApplicationContext(), TimelineActivity.class);
        i.putExtra("MESSAGE", false);
        startActivity(i);
    }

}