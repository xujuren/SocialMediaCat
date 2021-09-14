package anu.softwaredev.socialmediacat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ForgetPwActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pw);

        /** Test DB [dummy info > upload] */
        /* Store User Info in Firebase Realtime DB too [HashMap->]  (Ref: Vid2)*/
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", "Link using dummy info (forgetpw Activity)");
        hashMap.put("password", "123456");
        hashMap.put("displayName", "after Storage Linked to DB.com, authen only");         // can be added/updated later (Manage Profile)
        hashMap.put("phone", "12345678");
        hashMap.put("profilePic", "URL");

        // Firebase db instance's ref
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();            // path to store user data (named "Users")
        dbRef.child("Users").child("ID01").setValue(hashMap);                      // put data (in Hashmap) into db

        /** Test DB - End */

    }
}