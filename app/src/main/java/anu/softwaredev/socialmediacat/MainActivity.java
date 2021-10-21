package anu.softwaredev.socialmediacat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import anu.softwaredev.socialmediacat.Util.AssetHandler;


public class MainActivity extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    private String acc;
    private String pw;
    private TextInputLayout accLayout;             // [TextView]: without .setErrorEnabled(True)
    private TextInputLayout pwLayout;
    private EditText accEdit;
    private EditText pwEdit;
    private Button loginBt;
    private FirebaseUser user;

    /**
     * onCreate() for the Main page of the Application
     * @param savedInstanceState android unique class (Cloneable, Parcelable)saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    /**
     * Set up for basic view components
     */
    private void initView() {
        accEdit = (EditText) findViewById(R.id.login_msg_acc_text);                      // prev: [tv_user_email]
        pwEdit = (EditText) findViewById(R.id.login_msg_pw_text);                          // tv_user_pw
        accLayout = (TextInputLayout) findViewById(R.id.login_msg_acc);         // above: [login_msg_acc]
        pwLayout = (TextInputLayout) findViewById(R.id.login_msg_pw);               // login_msg_pw
        accLayout.setErrorEnabled(true);
        pwLayout.setErrorEnabled(true);

        // onClick method for the Login Button
        loginBt = (Button) findViewById(R.id.login_button);
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acc = accEdit.getText().toString();
                pw = pwEdit.getText().toString();
                if (TextUtils.isEmpty(acc)){
                    accLayout.setError(getString(R.string.login_errormsg_inputAcc));
                    return;
                }
                if (TextUtils.isEmpty(pw)){
                    pwLayout.setError(getString(R.string.login_errormsg_pw));
                    return;
                }
                accLayout.setError("");
                pwLayout.setError("");          // Reset error msg
                logInAttempt();
            }
        });
    }



    /**
     * for the Log in Method
     * Complete transfer the username to the next Activity
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
     * onClick method for the Create Account Button
     * Direct user to another Page to create account
     * @param v UI component
     */
    public void createAcc(View v) {
        Intent i = new Intent(MainActivity.this, CreateAccActivity.class);
        startActivity(i);
    }


    // Try log in with user input
    public void logInAttempt(){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(acc, pw)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
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
                            Toast.makeText(MainActivity.this, R.string.login_msg_success, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, AppActivity.class);
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
