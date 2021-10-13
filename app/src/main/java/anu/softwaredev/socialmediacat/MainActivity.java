package anu.softwaredev.socialmediacat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ArrayAdapter<String> fileDBAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();         // ?
        FirebaseUser user = mAuth.getCurrentUser();

    }

    // onClick M for button (login_button)
    public void logIn(View v){
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);       // for Data: startActivityForResult(i, 1) - request code: 1
    }

    // for logIn: to Complete the Transfer (username, pw)
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode, resultCode, data);        // run ori thgs

        // Log In [successful login > results]
        if (reqCode ==1 && resultCode==RESULT_OK) {             // for subj
            TextView welcomeText = (TextView) findViewById(R.id.tv_welcome);
            welcomeText.setText("Welcome, " + data.getStringExtra("username"));
        }
    }


    // onClick M for button (forget_pw_button)
    public void forgetPw(View v){
        Intent i = new Intent(MainActivity.this, ForgetPwActivity.class);
        startActivity(i);
    }

    // onClick M for button (create_acc_button)
    public void createAcc(View v){
        Intent i = new Intent(MainActivity.this, CreateAccActivity.class);
        startActivity(i);
    }




    /** TODO Below 3 methods: Testing Only [@AppActivity] */

    public void JsonTest(View v) {
        Intent i = new Intent(getApplicationContext(), LoadData_Test.class);   // to [JsonTest] Page
        startActivity(i);
        finish();
    }

    /** Testing Only [@AppActivity] */
    public void timelineTest(View v) {
        Intent i = new Intent(getApplicationContext(), TimelineActivity.class);   // to [TimelineActivity] Page
        startActivity(i);
        finish();
    }

    /** Testing Only [@quick Log in] */
    public void logInTest(View v) {
        // sign in method
        mAuth.signInWithEmailAndPassword("test@test.com", "testtest")
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(MainActivity.this, AppActivity.class);
                            startActivity(i);
                        }
                    }
                });
        // finish();
    }




}