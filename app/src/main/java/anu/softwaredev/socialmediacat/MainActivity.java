package anu.softwaredev.socialmediacat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import anu.softwaredev.socialmediacat.Util.AssetHandler;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    /**
     * main method, put all logic inside
     *
     * @param savedInstanceState android unique class (Cloneable, Parcelable)saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

    }


    /**
     * onClick for button (login_button)
     * switch from main welcome page to login page
     * @param v login UI component
     */
    public void logIn(View v) {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }


    /**
     * for logIn: to Complete the Transfer (username, pw) TODO
     *
     * @param reqCode
     * @param resultCode
     * @param data       bstract description of an operation to be performed
     */
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);        // run ori thgs
        if (reqCode == 1 && resultCode == RESULT_OK) {             // for subj
            TextView welcomeText = (TextView) findViewById(R.id.tv_welcome);
            welcomeText.setText("Welcome, " + data.getStringExtra("username"));
        }
    }


    /**
     * Create account Button: swap to create account page
     *
     * @param v UI component
     */
    public void createAcc(View v) {
        Intent i = new Intent(MainActivity.this, CreateAccActivity.class);
        startActivity(i);
    }


    /**
     * for Testing Only
     */
    public void logInTest(View v) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("test@test.com", "testtest")
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            user = mAuth.getCurrentUser();

                            Toast.makeText(MainActivity.this, R.string.login_msg_success, Toast.LENGTH_SHORT).show();
                            // Direct to the interface for users
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, AppActivity.class);
                            AssetHandler.loadPostsfromDataInstances(getApplicationContext());
                            startActivity(intent);
                            finish();

                        } else {
                            // error message
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
