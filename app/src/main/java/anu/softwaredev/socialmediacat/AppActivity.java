package anu.softwaredev.socialmediacat;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Fragment;
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

public class AppActivity extends AppCompatActivity {
    FirebaseUser user;
    String userName;                // user's displayName (if available) or e-mail address
    BottomNavigationView navView;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        /* Action bar & title */
        actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        /* User Info */
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (!TextUtils.isEmpty(user.getDisplayName())) {
            userName = user.getDisplayName();
        } else {
            // Extract user's email (from Login)       -  Excessive
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
               userName = extras.getString("userEmail");       // key arg: match that used in the other activity
            }
        }
        // Display Welcome Message
        TextView welcomeText = (TextView) findViewById(R.id.tv_welcome_appAct);
        welcomeText.setText("Welcome, " + userName);

        /** Default Fragment: Home Fragment Transaction */
        actionBar.setTitle("Home");    // change actionBar title
        HomeFragment frag1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, frag1, "");
        ft1.commit();

        /** Bottom Nav Bar (navView.setOnItemSelectedListener(listener))*/
        navView = findViewById(R.id.bottomNavMenu);
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
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
        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(i);
        // finish();
    }



}