package anu.softwaredev.socialmediacat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivityDao;

public class AppActivity extends AppCompatActivity {
    private FirebaseUser user;              //stored in firebase
    private String userName;                // user's displayName (if available) or e-mail address
    private BottomNavigationView navView;   // bottom Nav bar
    private ActionBar actionBar;

    /**
     * main method, put all logic inside
     * @param savedInstanceState android unique class (Cloneable, Parcelable)saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app); // set view according to UI layout
//        UserActivityDao.getInstance().findAllPosts();

        // Action bar - TODO (design layout with fragments)
        actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        // get userName, or userEmail if userName not available -TODO check
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (!TextUtils.isEmpty(user.getDisplayName())) {
            userName = user.getDisplayName();
        } else {
            userName = user.getEmail();
        }

        // set welcome text
        TextView welcomeText = (TextView) findViewById(R.id.tv_welcome_appAct);
        welcomeText.setText("Welcome, " + userName);


        // Default: Home Fragment Transaction
        actionBar.setTitle("Home");
        HomeFragment frag1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, frag1, "");
        ft1.commit();
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