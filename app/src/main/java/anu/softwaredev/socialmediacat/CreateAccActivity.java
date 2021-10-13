package anu.softwaredev.socialmediacat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import anu.softwaredev.socialmediacat.dao.decorator.User;

public class CreateAccActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String acc;
    private String pw;
    private EditText accEdit;
    private EditText pwEdit;
    private EditText pwEdit2;       // check
    private TextInputLayout accLayout;
    private TextInputLayout pwLayout;
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
        accEdit = (EditText) findViewById(R.id.createAcc_input_text);
        pwEdit = (EditText) findViewById(R.id.createAcc_input_pw_text);
        pwEdit2 = (EditText) findViewById(R.id.createAcc_input_pw2_text);

        /* Additional Error Check */
        accLayout = (TextInputLayout) findViewById(R.id.createAcc_input_layout);
        accLayout.setErrorEnabled(true);
        accLayout.setCounterMaxLength(20);
        pwLayout = (TextInputLayout) findViewById(R.id.createAcc_input_pw_layout);
        pwLayout.setErrorEnabled(true);
        pwLayout.setCounterMaxLength(12);
        TextInputLayout pwLayout2 = (TextInputLayout) findViewById(R.id.createAcc_input_pw2_layout);
        pwLayout2.setErrorEnabled(true);
        pwLayout2.setCounterMaxLength(12);
    }

    /* Click button [Create Acc] */
    public void signUpOnClick(View v) {

        /** Check Input Error (basic, on top of firebase's) */
        acc = accEdit.getText().toString();
        pw = pwEdit.getText().toString();
        String pw2 = pwEdit2.getText().toString();
        if (TextUtils.isEmpty(acc) || TextUtils.isEmpty(pw)){
            accLayout.setError(getString(R.string.createAcc_msg_emptyInput));
            return;
        }
        if (!pw.equals(pw2)){
            pwLayout.setError("check password");
            return;
        }
        accLayout.setError("");             // reset upon new Click (?)
        pwLayout.setError("");              //

        /** Create Account [Firebase]*/
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(acc, pw)
                .addOnCompleteListener(CreateAccActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Notification [CreateAcc]
                            Toast.makeText(CreateAccActivity.this, R.string.createAcc_msg_Success, Toast.LENGTH_SHORT).show();

                            /** Reg > Store User Info in Firebase Realtime DB (Ref: Vid2) >> * Database location: United States (us-central1) */
                            user = mAuth.getCurrentUser();
                            String userUID = user.getUid();
                            User newUser = new User(userUID, acc);          // Set Up [User] obj for new user ([Uid] as KEY of user info)

                            // db's ref >> / put data (in Hashmap) into db [tested OK with dummy data]
                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();            // path to store user data (named "Users")
                            dbRef.child("Users").child(userUID).setValue(newUser).addOnSuccessListener((OnSuccessListener) (aVoid) -> {
                                Toast.makeText(CreateAccActivity.this, "Data Successfully added", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener((e) -> {
                                Toast.makeText(CreateAccActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                            });

                            // finish: return to Main   (Alt: logged in, to [AppActivity])
                            finish();

                        } else {
                            Toast.makeText(CreateAccActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}