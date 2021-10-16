package anu.softwaredev.socialmediacat;
import static anu.softwaredev.socialmediacat.Util.AssetHandler.createPosts;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import anu.softwaredev.socialmediacat.Util.AssetHandler;

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
        createPosts(getApplicationContext());

        // Action bar
        actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

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


        // Default: Home Fragment Transaction
        actionBar.setTitle("Home");
        HomeFragment frag1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, frag1, "");
        ft1.commit();

        // Bottom Nav Bar (navView.setOnItemSelectedListener(listener))
        navView = findViewById(R.id.bottomNavMenu);
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            /** TODO - Notes:
             * Navigation Bar (created just to see whether this could be used in the layout design,
             * can delete if not)
             * @param item
             * @return true if clicked valid button and set different title, false if didnt click
             */
             @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {     //  handle item clicks
                    case R.id.nav_home:         // [home] fragment transaction
                        actionBar.setTitle("Home");    // change actionBar title
                        HomeFragment frag1 = new HomeFragment();
                        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                        ft1.replace(R.id.content, frag1, "");
                        ft1.commit();
                        return true;
                    case R.id.nav_acc:          // [acc] fragment transaction
                        actionBar.setTitle("Profile");    // change actionBar title
                        ProfileFragment frag2 = new ProfileFragment();
                        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                        ft2.replace(R.id.content, frag2, "");
                        ft2.commit();
                        return true;
                    case R.id.nav_users:        // [users] fragment transaction
                        actionBar.setTitle("Users");    // change actionBar title
                        ProfileFragment frag3 = new ProfileFragment();
                        FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                        ft3.replace(R.id.content, frag3, "");
                        ft3.commit();
                        return true;
                }
                return false;
            }
        });

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
        startActivity(i);
    }

    /**
     * switch to manageProfile page
     * @param v UI
     */
    public void toManageProfile(View v){
        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
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



}