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

import java.util.HashMap;
import java.util.Map;

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
                                    String userUID = user.getUid();                      // [Uid] as KEY of user info
                                    Map<String, Object> hashMap = new HashMap<>();       // HashMap for attri name & values (under Uid)
                                    hashMap.put("email", acc);
                                    hashMap.put("password", pw);
                                    hashMap.put("displayName", "[DB testing]: acc, pw as inputted");         // can be added/updated later (Manage Profile)
                                    hashMap.put("phone", "");                                                   // "
                                    hashMap.put("profilePic", "");                                               // "

                                    // db's ref >> / put data (in Hashmap) into db [tested OK with dummy data]
                                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();            // path to store user data (named "Users")
                                    dbRef.child("Users").child(userUID).setValue(hashMap).addOnSuccessListener((OnSuccessListener) (aVoid) -> {
                                        Toast.makeText(CreateAccActivity.this, "Data Successfully added", Toast.LENGTH_SHORT).show();
                                    }).addOnFailureListener((e) -> {
                                        Toast.makeText(CreateAccActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                                    });
                                    // [?] both Listener NOT triggered...

                                    // finish: return to Main   /or: log in and to [AppActivity]?
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