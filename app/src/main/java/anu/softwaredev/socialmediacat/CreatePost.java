package anu.softwaredev.socialmediacat;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import anu.softwaredev.socialmediacat.Classes.Post;

public class CreatePost extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference dbRef;
    private String uId = "";

    TextInputLayout contentLayout ;           // post_input_content_text
    TextInputLayout categoryLayout ;          // category_input_category_text
    TextInputLayout locLayout;              // post_input_location_reply
    TextInputLayout photoURLLayout;              // post_input_Photo_text
    EditText contentEdit ;
    EditText categoryEdit ;
    EditText locEdit;
    EditText photoURLEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        initView();

        // current User
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {uId = user.getUid();}

    }

    // Set Up Views
    public void initView() {
        contentLayout = (TextInputLayout) findViewById(R.id.content_createpost);
        categoryLayout = (TextInputLayout) findViewById(R.id.category_createpost);
        locLayout = (TextInputLayout) findViewById(R.id.loc_createpost);
        photoURLLayout = (TextInputLayout) findViewById(R.id.photo_createpost);
        contentEdit = (EditText) findViewById(R.id.content_createpost_et);
        categoryEdit = (EditText) findViewById(R.id.category_createpost_et);
        locEdit = (EditText) findViewById(R.id.loc_createpost_et);
        photoURLEdit = (EditText) findViewById(R.id.photo_createpost_et);
    }


    // Create Post - button
    public void bt_confirm_CreatePost(View v) {
        String content = contentEdit.getText().toString();
        String category = categoryEdit.getText().toString();
        String locShare = locEdit.getText().toString();
        String photoURL = photoURLEdit.getText().toString();

        String postId = "P101";     //TODO - unique, new

        // TODO: add Checkings [if new=empty / invalid]
        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(category)) {
            dbRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postId);
            ValueEventListener postListener = new ValueEventListener() {            // Syn
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Create Post
                    if (!snapshot.exists() || !snapshot.hasChildren()) {
                        Toast.makeText(CreatePost.this, "(Creating Post ...)", Toast.LENGTH_LONG).show();
                        Post newPost = new Post(uId, category, postId, content);                // TODO [postId]
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();            // path to store user data (named "Users")
                        dbRef.child("Posts").child(postId).setValue(newPost);                           // *postId
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(CreatePost.this, error.toString(), Toast.LENGTH_LONG).show();
                    Log.w(TAG, "Create Post failed. Please try later!", error.toException());
                }
            };
            dbRef.addValueEventListener(postListener);

        } else {
            Toast.makeText(CreatePost.this, "You did not enter the post Category and/or Content!", Toast.LENGTH_LONG).show();
            // finish();   // Not signed in (unexpected case)
        }



    }






}