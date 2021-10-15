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
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import anu.softwaredev.socialmediacat.Classes.Post;
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
    private EditText photoURLEdit;
    private LocationManager locManager;
    private LocationListener locListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        initView();

    }

    // TEST - can extract (so that call only if asked and permit?)
    private void checkLocation() {

        // Location Manager & Listener
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

                // TEST
                Toast.makeText(CreatePost.this, "should request permission now...", Toast.LENGTH_SHORT).show();

                // TODO did NOT pop up... WHY???
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 100);

            } else {
                locManager.requestLocationUpdates("gps", 1000, 1000, locListener);
            }

        } // version issue

    }

    // TODO - try get permission result
    // TODO [BUG] ??? when called (check Loc above), show "NO permission" directly (without request)
    @Override
    @SuppressLint("MissingPermission")
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // M1
        if (requestCode==100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission OK
            Toast.makeText(CreatePost.this, "got permission", Toast.LENGTH_SHORT).show();
            locManager.requestLocationUpdates("gps", 1000, 1000, locListener);

        } else {
            // Not ok
            Toast.makeText(CreatePost.this, "NO permission - grantResults[0]: "+grantResults[0], Toast.LENGTH_SHORT).show();
            latlngText.setText("(permission required)");
        }

    }


    private void initView() {
        contentLayout = (TextInputLayout) findViewById(R.id.content_createpost);
        categoryLayout = (TextInputLayout) findViewById(R.id.category_createpost);
        shareLocOption = (ToggleButton) findViewById(R.id.bt_shareLoc);
        latlngText = (TextView) findViewById(R.id.tv_show_latlng);
        photoURLLayout = (TextInputLayout) findViewById(R.id.photo_createpost);
        contentEdit = (EditText) findViewById(R.id.content_createpost_et);
        tagsEdit = (EditText) findViewById(R.id.category_createpost_et);
        photoURLEdit = (EditText) findViewById(R.id.photo_createpost_et);

        shareLocOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    latlngText.setText("(Loading location)");
                    checkLocation();
                } else {
                    latlngText.setText("(Not selected)");
                }
            }
        });
    }

    // Create Post - button
    public void bt_confirm_CreatePost(View v) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uId = user.getUid();
            // Get Input
            String content = contentEdit.getText().toString();
            String category = tagsEdit.getText().toString();
            Boolean shareLocBool = shareLocOption.getText().toString().equals("Loc will be shared!");
            String photoURL = photoURLEdit.getText().toString();

            // TODO - bug (location)
            // add Location to Content, if chosen and permitted
            String latLng = latlngText.getText().toString();
            if (latLng.charAt(0)!='('){
                content = content + latLng;
            }

            // URL             // TODO - Check Validity
            Boolean photoURLValid = false;
            if (!TextUtils.isEmpty(photoURL)) {
                try {
                    (new java.net.URL(photoURL)).openStream().close();
                    photoURLValid = true;
                } catch (Exception ex) {
                }
            }
            Toast.makeText(CreatePost.this, "PhotoValid? - " + photoURLValid.toString(), Toast.LENGTH_SHORT);

            // Check required info - Create Post
            if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(category)) {
                UserActivity newPostAct = UserActivityDao.getInstance().createPost(uId, category, "P-1", content);  // dummy postId
                Post newPost = newPostAct.getPost();          // TODO [postId]
                Toast.makeText(CreatePost.this, "Post Created!" + newPost.toString(), Toast.LENGTH_SHORT).show();
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