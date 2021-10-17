package anu.softwaredev.socialmediacat;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;

import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivityDao;

/** For the display of Posts in a timeline */
public class TimelineActivity extends AppCompatActivity {

    /**
     * main method, put all logic inside
     * @param savedInstanceState android unique class (Cloneable, Parcelable)saved state
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Set Up timeline view and data
        RecyclerView rvTimeline = (RecyclerView) findViewById(R.id.rv_timeline);                        // Timeline
        TimelineAdapter timelineAdapter = new TimelineAdapter(getApplicationContext(), UserActivityDao.getInstance().findAllPosts());        // Adapter to Data
        rvTimeline.setAdapter(timelineAdapter);
        rvTimeline.setLayoutManager(new LinearLayoutManager(this));                              // Linear timeline (more spaces for information)
    }



}