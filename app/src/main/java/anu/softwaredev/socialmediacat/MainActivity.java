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

import com.google.android.material.textfield.TextInputLayout;
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

    private FirebaseAuth fileAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    /** M (https://nickcarter9.github.io/2017/04/20/2017/2017_04_20-android-firebase-database-auth/)*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();         // ?
        FirebaseUser user = mAuth.getCurrentUser();

        /* [?] AuthStateListener: listen for Change in AuthState */
//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                Log.e("Nick", "onAuthStateChanged");
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user==null) {
//                    Log.e("Nick", "Try to Login: ");                // ?
//                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                } else {
//                    Log.e("Nick", "Login Success");
//                    FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
//                    DatabaseReference myRef = fireDB.getReference("聯絡人");   // ?
//                    // myRef.addChildEventListener(MainActivity.this);
//                }
//            }
//        };
//
//        // Check: Show Content if <NOT aldy logged in>
//        if (user == null) {
//            setContentView(R.layout.activity_main);
//            Button login = (Button) findViewById(R.id.login_button);
//            Button signUp = (Button) findViewById(R.id.create_acc_button);
//
//            // Ref: login.setOnClickListener(new View.OnClickListener(){}), also for signUp
//        } else {
//        // if <aldy logged in>: go APP's Interface (Ref: MainAct)
//            Intent i = new Intent();
//            i.setClass(MainActivity.this, AppActivity.class);
//        }
    }

    // onClick M for button (login_button)
    public void logIn(View v){
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(i, 1);       // request code: 1
    }

    // for logIn: to Complete the Transfer (username, pw)
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode, resultCode, data);        // run ori thgs

        // Log In [successful login > results]
        if (reqCode ==1 && resultCode==RESULT_OK) {             // for subj
            // boolean logInSucceed = data.getBooleanExtra("login_succeed", false);
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


    // Create Acc - Set Up (https://givemepass.blogspot.com/2017/04/firebase-authentication.html)
    private void initView() {
        mAuth = FirebaseAuth.getInstance();
//        accountEdit = (EditText) findViewById(R.id.input_email_createacc);
//        passwordEdit = (EditText) findViewById(R.id.input_pw1_createacc);
//        accoutLayout = (TextInputLayout) findViewById(R.id.account_layout);
//        passwordLayout = (TextInputLayout) findViewById(R.id.password_layout);
//        passwordLayout.setErrorEnabled(true);
//        accoutLayout.setErrorEnabled(true);
//        signUpBtn = (Button) findViewById(R.id.signup_button);
    }

    /* 檢查結束後 將提示訊息記得設回空字串，否則遇到第一次失敗，第二次要輸入的時候失敗字串還會顯示在上面，在上面程式碼中我們有跟 Firebase 取得 auth 的物件。 */



}