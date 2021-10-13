package anu.softwaredev.socialmediacat;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import anu.softwaredev.socialmediacat.Classes.Post;

public class CreatePost extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
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
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uId = user.getUid();

            // Get Input
            String content = contentEdit.getText().toString();
            String category = categoryEdit.getText().toString();
            String locShare = locEdit.getText().toString();
            String photoURL = photoURLEdit.getText().toString();
            String postId = "P101";                                     //TODO - unique, new

            if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(category)) {
                // Add to DB
                dbRef = FirebaseDatabase.getInstance().getReference();            // path to DB
                String key = dbRef.child("Posts").push().getKey();                  // unique Key for Posts
                Post newPost = new Post(uId, category, postId, content);           // TODO [postId]
                Toast.makeText(CreatePost.this, "Loading ... " + newPost.toString(), Toast.LENGTH_SHORT).show();

                Map<String, Object> postValues = newPost.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/Posts/" + key, postValues);
                // childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

                dbRef.updateChildren(childUpdates);

            } else {
                Toast.makeText(CreatePost.this, "You did not enter the post Category and/or Content!", Toast.LENGTH_LONG).show();
                finish();
            }

        } else {
                Toast.makeText(CreatePost.this, "An error occur. Sorry for the inconveniences", Toast.LENGTH_LONG).show();
                finish();   // UNEXPECTED Branch
            }

        }



}