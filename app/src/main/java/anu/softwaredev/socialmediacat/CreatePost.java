package anu.softwaredev.socialmediacat;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anu.softwaredev.socialmediacat.Classes.Post;

public class CreatePost extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference dbRef;
    private TextView latlngText;
    private TextInputLayout contentLayout;
    private TextInputLayout categoryLayout;
    private ToggleButton shareLocOption;
    private TextInputLayout photoURLLayout;
    private EditText contentEdit;
    private EditText categoryEdit;
    private EditText photoURLEdit;
    private LocationManager locManager;
    private LocationListener locListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        initView();

        // Location Manage & Listener
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latlngText.setText("["+String.format("%.2f", location.getLatitude()) + ", " + String.format("%.2f", location.getLongitude())+"]");
            }

            @Override
            public void onLocationChanged (@NonNull List < Location > locations) {}
            @Override
            public void onFlushComplete ( int requestCode){

            }
            @Override
            public void onStatusChanged (String provider,int status, Bundle extras){}
            @Override
            public void onProviderEnabled (@NonNull String provider){
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
            @Override
            public void onProviderDisabled (@NonNull String provider){}
        };
        // check Permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 100);
            }
        }
        locManager.requestLocationUpdates("gps", 1000, 1000, locListener);
    }

    private void initView() {
        contentLayout = (TextInputLayout) findViewById(R.id.content_createpost);
        categoryLayout = (TextInputLayout) findViewById(R.id.category_createpost);
        shareLocOption = (ToggleButton) findViewById(R.id.bt_shareLoc);
        latlngText = (TextView) findViewById(R.id.tv_show_latlng);
        photoURLLayout = (TextInputLayout) findViewById(R.id.photo_createpost);
        contentEdit = (EditText) findViewById(R.id.content_createpost_et);
        categoryEdit = (EditText) findViewById(R.id.category_createpost_et);
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
            Boolean shareLocBool = shareLocOption.getText().toString().equals("Loc will be shared!");
            String photoURL = photoURLEdit.getText().toString();

            // TODO
            /** shd be > UserActivityDAO */

            if (shareLocBool) {
                // getLoc();
                String latLng = latlngText.getText().toString();
                // add to Content if share location chosen and location is available.
                if (latLng.charAt(0)!='('){
                    content = content + latLng;
                }
            }

            // URL
            Boolean photoURLValid = false;
            if (!photoURL.equals("")) {
                try {
                    (new java.net.URL(photoURL)).openStream().close();
                    photoURLValid = true;
                } catch (Exception ex) {
                }
            }
            Toast.makeText(CreatePost.this, "PhotoValid? - " + photoURLValid.toString(), Toast.LENGTH_SHORT);


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

                Toast.makeText(CreatePost.this, "Post Created!", Toast.LENGTH_LONG).show();
                finish();

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