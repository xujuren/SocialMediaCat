package anu.softwaredev.socialmediacat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        // Input Items
        EditText inputEmail = (EditText) findViewById(R.id.input_email_createacc);
        EditText inputPw1 = (EditText) findViewById(R.id.input_pw1_createacc);
        EditText inputPw2 = (EditText) findViewById(R.id.input_pw2_createacc);

        // ?
        // accoutLayout = (TextInputLayout) findViewById(R.id.account_layout);
        // passwordLayout = (TextInputLayout) findViewById(R.id.password_layout);



        // String userEmail =


    }

}