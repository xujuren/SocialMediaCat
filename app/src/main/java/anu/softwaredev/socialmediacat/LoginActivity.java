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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String acc;
    private String pw;
    private TextInputLayout accLayout;             // [TextView]: without .setErrorEnabled(True)
    private TextInputLayout pwLayout;
    private EditText accEdit;
    private EditText pwEdit;
    private Button loginBt;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    /** M: Ref (https://github.com/givemepassxd999/filebase_auth_register_demo/blob/master/app/src/main/java/com/example/givemepass/firebase_auth/LoginActivity.java) */
    private void initView() {
        mAuth = FirebaseAuth.getInstance();
        accEdit = (EditText) findViewById(R.id.login_msg_acc_text);                      // prev: [tv_user_email]
        pwEdit = (EditText) findViewById(R.id.login_msg_pw_text);                          // tv_user_pw
        accLayout = (TextInputLayout) findViewById(R.id.login_msg_acc);         // above: [login_msg_acc]
        pwLayout = (TextInputLayout) findViewById(R.id.login_msg_pw);               // login_msg_pw
        accLayout.setErrorEnabled(true);
        pwLayout.setErrorEnabled(true);

        /* login button - onClick */
        loginBt = (Button) findViewById(R.id.Create_Acc_Confirm_bt);
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
                pwLayout.setError("");          // Reset error msg - else remains visible for new input

                // sign in method
                mAuth.signInWithEmailAndPassword(acc, pw)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    user = mAuth.getCurrentUser();

                                    Toast.makeText(LoginActivity.this, R.string.login_msg_success, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra("userEmail", acc);          // excessive
                                    intent.setClass(LoginActivity.this, AppActivity.class);    // or [Main] (?)
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


    // Refresh the Activity (login page)
    public void refresh(){          //refresh is onClick name given to the button
        onRestart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }


}