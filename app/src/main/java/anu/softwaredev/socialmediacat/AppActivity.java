package anu.softwaredev.socialmediacat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class AppActivity extends AppCompatActivity {

    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        /** Extract user's email (from Login) */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String user = extras.getString("userEmail");       //The key argument here must match that used in the other activity

            TextView welcomeText = (TextView) findViewById(R.id.tv_welcome_appAct);
            welcomeText.setText("Welcome, " + user);
        }

        setUpLogOutButton();

    }

    // Set Up the [Log Out Button] > return to Main Page
    private void setUpLogOutButton() {
        logout = (Button) findViewById(R.id.logOut_bt);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();                   // FirebaseAuth
                Intent intent = new Intent();
                intent.setClass(AppActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // [Button: toTimeLine_bt]
    protected void toTimelineAct(View v){
        Intent i = new Intent(AppActivity.this, TimelineActivity.class);
        startActivity(i);
    }

    // to Complete Data Transfer btn Act
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode, resultCode, data);        // run ori thgs
        // [x] After Log In [successful login > results] ? (username, pw)
//        TextView welcomeText = (TextView) findViewById(R.id.tv_welcome_appAct);
//        if (reqCode ==1 && resultCode==RESULT_OK) {             // for subj
//            welcomeText.setText("Welcome, " + data.getStringExtra("userEmail"));
//        } else {
//            welcomeText.setText("???");
//        }

    }
}