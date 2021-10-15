package anu.softwaredev.socialmediacat;
import static anu.softwaredev.socialmediacat.Util.loadFromAssets.getDataFromJson;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import java.util.List;

import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.Util.loadFromAssets;
import anu.softwaredev.socialmediacat.Util.loadFromBespoke;
import anu.softwaredev.socialmediacat.Util.loadFromCSV;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivity;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivityDao;

/** For the display of Posts in a timeline */
public class TimelineActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        RecyclerView rvTimeline = (RecyclerView) findViewById(R.id.rv_timeline);                        // Timeline (the Recycler View)
        List<UserActivity> allPosts = getAllPosts(getApplicationContext(), true, true, false,false);                  // Data
        TimelineAdapter timelineAdapter = new TimelineAdapter(getApplicationContext(), allPosts);        // Adapter
        rvTimeline.setAdapter(timelineAdapter);
        rvTimeline.setLayoutManager(new LinearLayoutManager(this));                              // (Linear timeline, more spaces for information)
            // rvTimeline.setLayoutManager(new GridLayoutManager(this, 2));                             // (Grid Layout)

    }

    /** TODO + Sources of data set */
    public List<UserActivity> getAllPosts(Context ctx, boolean csv, boolean bespoke, boolean json, boolean dummy) {

        // get from DATA INSTANCES only (Create Post)
        return loadFromAssets.getAllPosts(getApplicationContext(), csv, bespoke, json, dummy);


    }







}