package anu.softwaredev.socialmediacat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ArrayAdapter<String> fileDBAdapter;

    /** M (https://nickcarter9.github.io/2017/04/20/2017/2017_04_20-android-firebase-database-auth/)*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();         // ?
        FirebaseUser user = mAuth.getCurrentUser();

    }


    /** TODO Testing Only [@AppActivity] */      // Set Up the [to TimeLine Button] > [TimelineActivity] (see timeline grid/linear)
    public void JsonTest(View v) {
        Intent i = new Intent(getApplicationContext(), JsonTest.class);   // to [JsonTest] Page
        startActivity(i);
        finish();
    }

    /** Testing Only [@AppActivity] */      // Set Up the [to TimeLine Button] > [TimelineActivity] (see timeline grid/linear)
    public void timelineTest(View v) {
        Intent i = new Intent(getApplicationContext(), TimelineActivity.class);   // to [TimelineActivity] Page
        startActivity(i);
        finish();
    }

    /** Testing Only [@quick Log in] */     // Set Up the [to TimeLine Button] > [TimelineActivity] (see timeline grid/linear)
    public void logInTest(View v) {
        // sign in method
        mAuth.signInWithEmailAndPassword("testing@doggo.com", "Testing")
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


}