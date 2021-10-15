package anu.softwaredev.socialmediacat;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Build;
import android.os.Bundle;
import java.util.List;

import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.Util.loadFromAssets;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivity;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivityDao;

/** For the display of Posts in a timeline */
public class TimelineActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Set Up timeline view and data
        RecyclerView rvTimeline = (RecyclerView) findViewById(R.id.rv_timeline);                        // Timeline
        TimelineAdapter timelineAdapter = new TimelineAdapter(getApplicationContext(), getAllPosts());        // Adapter to Data
        rvTimeline.setAdapter(timelineAdapter);
        rvTimeline.setLayoutManager(new LinearLayoutManager(this));                              // Linear timeline (more spaces for information)
            // rvTimeline.setLayoutManager(new GridLayoutManager(this, 2));                             // (Grid Layout)

    }

    /** TODO + Sources of data set */
    public List<Post> getAllPosts() {

        // load Posts
        loadFromAssets.loadPostsfromDataInstances(getApplicationContext());

        // TODO [###]: this >> Stream?
        loadFromAssets.createPostsfromDataInstances(getApplicationContext());

        return UserActivityDao.getInstance().findAllPosts();
    }







}