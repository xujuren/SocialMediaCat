package anu.softwaredev.socialmediacat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class AppActivity extends AppCompatActivity {

    private Button logoutBt;
    private Button timelineBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        /** Extract user's email (from Login) */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String user = extras.getString("userEmail");       // key arg: match that used in the other activity
            TextView welcomeText = (TextView) findViewById(R.id.tv_welcome_appAct);
            welcomeText.setText("Welcome, " + user);
        }

        setUpLogOutButton();
        setUpTimelineButton();

    }

    // Set Up the [Log Out Button] > return to Main Page
    private void setUpLogOutButton() {
        logoutBt = (Button) findViewById(R.id.logOut_bt);
        logoutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();                   // FirebaseAuth: signOut
                Intent intent = new Intent();                           // return to Main Page
                intent.setClass(AppActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    // Set Up the [Log Out Button] > return to Main Page
    private void setUpTimelineButton() {
        timelineBt = (Button) findViewById(R.id.to_timeline_bt);
        timelineBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent();                           // to [TimelineActivity] Page
                intent2.setClass(AppActivity.this, TimelineActivity.class);
                startActivity(intent2);
                finish();
            }
        });
    }


    // M1 (try above) [Button: toTimeLine_bt]  [c] need public
//    public void toTimelineAct(View v){
//        Intent i = new Intent(getApplicationContext(), TimelineActivity.class);        // ori: AppActivity.this
//        startActivity(i);
//        // finish();
//    }

}