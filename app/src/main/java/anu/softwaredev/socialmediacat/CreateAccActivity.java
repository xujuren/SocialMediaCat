package anu.softwaredev.socialmediacat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String acc;
    private String pw;
    private EditText accEdit;
    private EditText pwEdit;
    private EditText pwEdit2;       // check
    private TextInputLayout msgLayout;
    private EditText msgTextEdit;
    private Button signUpBt;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);
        initView();
    }

    private void initView() {
        mAuth = FirebaseAuth.getInstance();
        accEdit = (EditText) findViewById(R.id.createAcc_input_text);
        pwEdit = (EditText) findViewById(R.id.createAcc_input_pw_text);
        // pwEdit2 = (EditText) findViewById(R.id.input_pw2_createacc);

        // why need?? got toast
        // msgLayout = (TextInputLayout) findViewById(R.id.createacc_msg_text);
        // msgLayout.setErrorEnabled(true);

        /* Click button [Create Acc] */
        signUpBt = (Button) findViewById(R.id.Create_Acc_Bt);
        signUpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acc = accEdit.getText().toString();
                pw = pwEdit.getText().toString();
                if(TextUtils.isEmpty(acc) || TextUtils.isEmpty(pw)){
                    // msgLayout.setError(getString(R.string.createAcc_msg_emptyInput));
                    // msgLayout.setError("");      - repeated?
                    return;
                }
                // msgLayout.setError("");

                /* Create Account (with pw) */
                mAuth.createUserWithEmailAndPassword(acc, pw)
                        .addOnCompleteListener(CreateAccActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CreateAccActivity.this, R.string.createAcc_msg_Success, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.setClass(CreateAccActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(CreateAccActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

}