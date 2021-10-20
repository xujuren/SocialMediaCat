package anu.softwaredev.socialmediacat;
import com.google.firebase.auth.FirebaseAuth;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseUser;

import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivityDao;

public class CreatePost extends AppCompatActivity {
    private FirebaseUser user;
    private TextView latlngText;
    private ToggleButton shareLocOption;
    private EditText contentEdit;
    private EditText tagEdit;
    private EditText photoIDEdit;
    private LocationManager locManager;
    private LocationListener locListener;

    private static final int PHOTO_ID_ERROR_CODE = -1;
    private static final int PHOTO_LIMIT_LOWER = 0;
    private static final int PHOTO_LIMIT_UPPER = 100;
    private static String TAG_DEFAULT = "#random";

    @Override
    /**
     * main method, put all logic inside
     * @param savedInstanceState android unique class (Cloneable, Parcelable)saved state
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        initView();
    }

    /**
     * initialise view
     */
    private void initView() {
        shareLocOption = (ToggleButton) findViewById(R.id.bt_shareLoc);                             //share location button
        latlngText = (TextView) findViewById(R.id.tv_show_latlng);
        contentEdit = (EditText) findViewById(R.id.content_createpost_et);
        tagEdit = (EditText) findViewById(R.id.tag_createpost_et);
        photoIDEdit = (EditText) findViewById(R.id.photo_createpost_et);
        shareLocOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    latlngText.setText("(Loading location)");
                    checkLocation();
                } else {
                    latlngText.setText("N/A");
                }
            }
        });
    }


    /**
     * onClick Method for the "Create Post" Button
     * @param v UI
     */
    public void CreatePostConfirmBt(View v) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uId = user.getUid();

            // Get Inputs
            String content = contentEdit.getText().toString();
            String tag = tagEdit.getText().toString();
            String photoIDInput = photoIDEdit.getText().toString();

            // Check invalid characters in tag
            if (tag.contains(",") || tag.contains(";") || TextUtils.isEmpty(tag)) {
                tag = TAG_DEFAULT;
                Toast.makeText(CreatePost.this, "Invalid input - your post tagged " + tag + ": )", Toast.LENGTH_SHORT).show();
            }

            // add Location to Content, if permitted and available (Note: related to a known bug as reported)
            String latLng = latlngText.getText().toString();
            if (latLng.charAt(0)=='['){
                content = content + latLng;
            }

            // check Photo ID
            int photoId = PHOTO_ID_ERROR_CODE;
            if (!TextUtils.isEmpty(photoIDInput)) {
                try {
                    int photoIdInput = Integer.parseInt(photoIDInput);
                    if (photoIdInput>=PHOTO_LIMIT_LOWER && photoIdInput<=PHOTO_LIMIT_UPPER) {
                        photoId = photoIdInput;
                    } else {
                        Toast.makeText(CreatePost.this, "Invalid Photo ID! Photo will not be included in your post.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(CreatePost.this, "Invalid Photo ID! Photo will not be included in your post.", Toast.LENGTH_SHORT).show();
                }
            }

            // create post
            UserActivityDao.getInstance().createPost(uId, tag, content, photoId);
            Toast.makeText(CreatePost.this, "Post Created!", Toast.LENGTH_SHORT).show();


        } else { // UNEXPECTED Branch
            Toast.makeText(CreatePost.this, "An error occur. Sorry for the inconveniences", Toast.LENGTH_LONG).show();
        }
        finish();

    }

    /**
     * read GPS location
     */
    private void checkLocation() {

        // Location Manager & Listener
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latlngText.setText("[" + String.format("%.2f", location.getLatitude()) + ", " + String.format("%.2f", location.getLongitude()) + "]");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }
        };

        // check Permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 1);
            } else {
                locManager.requestLocationUpdates("gps", 1000, 1000, locListener);
            }
        }

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locManager.requestLocationUpdates("gps", 1000, 1000, locListener);

        } else if (requestCode == 1) {
            // Not ok
            latlngText.setText("N/A");
            shareLocOption.setText("Missing Permission!");
        }

    }

}