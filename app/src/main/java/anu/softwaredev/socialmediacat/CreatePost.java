package anu.softwaredev.socialmediacat;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnSuccessListener;
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
    private TextInputLayout contentLayout;
    private TextInputLayout categoryLayout;
    private ToggleButton shareLocOption;
    private TextInputLayout photoURLLayout;
    private EditText contentEdit;
    private EditText categoryEdit;
    private EditText photoURLEdit;
    private LocationManager locManager;
    private LocationListener locListener;
    private String lat;
    private String lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        initView();


    }

    public void initView() {
        contentLayout = (TextInputLayout) findViewById(R.id.content_createpost);
        categoryLayout = (TextInputLayout) findViewById(R.id.category_createpost);
        shareLocOption = (ToggleButton) findViewById(R.id.bt_shareLoc);
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
            Boolean shareLocBool = shareLocOption.getText().toString().equals("Location will be shared!");
            String photoURL = photoURLEdit.getText().toString();


            // TODO
            /** shd be > UserActivityDAO */

            if (shareLocBool) {
                getLatLong();   // locationListener
                getLoc();


                Toast.makeText(CreatePost.this, "ll: " + lat + "," + lng, Toast.LENGTH_LONG).show();
                content = content + "["+lat+", "+lng+"]";
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

    public void getLoc() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // check Permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
        }

        locManager.requestLocationUpdates("gps", 10000, 1000, locListener);

        // Toast.makeText(CreatePost.this, "ll: "+latLong[0]+","+latLong[1], Toast.LENGTH_LONG).show();
    }

    /** location listener */
    private void getLatLong() {
        locListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                lat = String.format("%.2f", location.getLatitude());
                lng = String.format("%.2f", location.getLongitude());

                // TODO - super slow?? persistent?
                // Toast.makeText(CreatePost.this, "in getLL(): "+ lat+","+lng, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLocationChanged(@NonNull List<Location> locations) {

            }

            @Override
            public void onFlushComplete(int requestCode) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }
        };

    }





}