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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivity;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivityDao;

public class CreatePost extends AppCompatActivity {
    private FirebaseUser user;
    private TextView latlngText;
    private TextInputLayout contentLayout;
    private TextInputLayout categoryLayout;
    private ToggleButton shareLocOption;
    private TextInputLayout photoURLLayout;
    private EditText contentEdit;
    private EditText tagsEdit;
    private EditText photoIDEdit;
    private LocationManager locManager;
    private LocationListener locListener;
    public static final int ERROR_CODE = -1;
    public static final int POSITION_ZERO = -1;
    public static final int PHOTO_LIMIT_LOWER = 20;
    public static final int PHOTO_LIMIT_UPPER = 100;





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
        //post content input
        contentLayout = (TextInputLayout) findViewById(R.id.content_createpost);
        //category input
        categoryLayout = (TextInputLayout) findViewById(R.id.category_createpost);
        //share location button
        shareLocOption = (ToggleButton) findViewById(R.id.bt_shareLoc);
        latlngText = (TextView) findViewById(R.id.tv_show_latlng);
        photoURLLayout = (TextInputLayout) findViewById(R.id.photo_createpost);
        contentEdit = (EditText) findViewById(R.id.content_createpost_et);
        tagsEdit = (EditText) findViewById(R.id.category_createpost_et);
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
     *     // Create Post - button
     * @param v UI
     */
    public void bt_confirm_CreatePost(View v) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uId = user.getUid();
            // Get Input
            String content = contentEdit.getText().toString();
            String category = tagsEdit.getText().toString();
            String photoIDInput = photoIDEdit.getText().toString();     // *ori: photoURL

            // add Location to Content (if permitted and available)
            String latLng = latlngText.getText().toString();
            if (latLng.charAt(POSITION_ZERO)=='['){
                content = content + latLng;
            }

            // ID for Post Photo
            int photoId = ERROR_CODE;   // error code
            if (!TextUtils.isEmpty(photoIDInput)) {
                try {
                    int photoIdInput = Integer.parseInt(photoIDInput);
                    if (photoIdInput>=PHOTO_LIMIT_LOWER && photoIdInput<=PHOTO_LIMIT_UPPER) {
                        photoId = photoIdInput;
                    } else {
                        Toast.makeText(CreatePost.this, "Invalid ID! random Photo ID generated for you ...", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(CreatePost.this, "Invalid ID! random Photo ID generated for you ...", Toast.LENGTH_SHORT).show();
                }
            }

            // Check required info - Create Post
            if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(category)) {
                UserActivityDao.getInstance().createPost(uId, category, content, photoId);
                Toast.makeText(CreatePost.this, "Post Created!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreatePost.this, "You did not enter the post Category and/or Content!", Toast.LENGTH_LONG).show();
            }

        } else {// UNEXPECTED Branch
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

        // TODO Issue: does not re-ask permission even if run (unless Emulator rerun?)
        // check Permissions
        //Unnecessary; SDK_INT is always >= 26 might need to be deleted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                // Toast.makeText(CreatePost.this, "should request permission now...", Toast.LENGTH_SHORT).show();
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 1);

            } else {
                locManager.requestLocationUpdates("gps", 1000, 1000, locListener);
            }

        } // NOT expected (unless version issue)

    }

    /**
     *  never used
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locManager.requestLocationUpdates("gps", 1000, 1000, locListener);

        } else if (requestCode == 1) {
            // Not ok
            Toast.makeText(CreatePost.this, "NO permission - grantResults[0]: " + grantResults[0], Toast.LENGTH_SHORT).show();
            latlngText.setText("N/A");
            shareLocOption.setText("Missing Permission!");
        }

    }

}