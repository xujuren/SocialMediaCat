package anu.softwaredev.socialmediacat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

import anu.softwaredev.socialmediacat.Classes.User;

public class CreateAccActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String email;
    private String pw;
    private EditText accEdit;
    private EditText pwEdit;
    private EditText pwEdit2;
    private TextInputLayout accLayout;
    private TextInputLayout pwLayout;
    private FirebaseUser user;

    /**
     * main method, put all logic inside
     * @param savedInstanceState android unique class (Cloneable, Parcelable)saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        initView();

    }

    /**
     * initialise view
     */
    private void initView() {
        accEdit = (EditText) findViewById(R.id.createAcc_input_text);
        pwEdit = (EditText) findViewById(R.id.createAcc_input_pw_text);
        pwEdit2 = (EditText) findViewById(R.id.createAcc_input_pw2_text);

        /** Input check */
        //input email format constraint
        accLayout = (TextInputLayout) findViewById(R.id.createAcc_input_layout);
        accLayout.setErrorEnabled(true);
        accLayout.setCounterMaxLength(20);

        // 1st password format check
        pwLayout = (TextInputLayout) findViewById(R.id.createAcc_input_pw_layout);
        pwLayout.setErrorEnabled(true);
        pwLayout.setCounterMaxLength(12);

        // 2nd password format check
        TextInputLayout pwLayout2 = (TextInputLayout) findViewById(R.id.createAcc_input_pw2_layout);
        pwLayout2.setErrorEnabled(true);
        pwLayout2.setCounterMaxLength(12);
    }


    /**
     * Click button [Create Acc]
     * @param v UI
     */
     public void signUpOnClick(View v) {

        /* Check Input Error (basic, on top of firebase's) */
        email = accEdit.getText().toString();
        pw = pwEdit.getText().toString();
        String pw2 = pwEdit2.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pw)){
            accLayout.setError(getString(R.string.createAcc_msg_emptyInput));
            return;
        }
        if (!pw.equals(pw2)){
            pwLayout.setError("check password");
            return;
        }
        accLayout.setError("");             // reset upon new Click
        pwLayout.setError("");

        createAccFirebase();
    }

    /** Create account on firebase based on input */
    public void createAccFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(CreateAccActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Update firebase
                            user = mAuth.getCurrentUser();
                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                            String uId = user.getUid();
                            User newUser = new User(uId, email);
                            Map<String, Object> userValues = newUser.toMap();
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/Users/" + uId, userValues);
                            dbRef.updateChildren(childUpdates);

                            // notify User - create account successful
                            Toast.makeText(CreateAccActivity.this, "All good! you may login now", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            Toast.makeText(CreateAccActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}