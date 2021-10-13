package anu.softwaredev.socialmediacat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import java.util.List;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivity;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivityDao;

public class TimelineActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Timeline (the Recycler View)
        RecyclerView rvTimeline = (RecyclerView) findViewById(R.id.rv_timeline);

        // Data (create sth as posts here > set up a list w/ all posts)
        List<UserActivity> allPosts = getAllPosts();

        // Adapter
        TimelineAdapter timelineAdapter = new TimelineAdapter(getApplicationContext(), allPosts);
        rvTimeline.setAdapter(timelineAdapter);
        rvTimeline.setLayoutManager(new GridLayoutManager(this, 2));        // Grid Layout
            // rvTimeline.setLayoutManager(new LinearLayoutManager(this));       // set layout (Linear: ≈IG)

    }

    /** TODO - DATASET (posts) for TimeLineAdapter */
    public List<UserActivity> getAllPosts() {

        // Data (Dummy Posts) TODO Testing
        UserActivityDao.getInstance().createPost("Casual", "post01", "Hi", "uId01");
        UserActivityDao.getInstance().createPost("Formal", "post02", "Welcome", "uId02");
        UserActivityDao.getInstance().createPost("Sports", "post03", "Running ... ...", "uId03");

        return UserActivityDao.getInstance().findAllPosts();



//        // Data (create sth as posts here > set up a list w/ all posts)
//        UserActivityDao.getInstance().createPost("@woofie", "owuowu, owuowuowu, owu! OWU!");
//        UserActivityDao.getInstance().createPost("@pengu", "arrRRARARKRKRKRKKK!");
//
//        // All Posts
//        List<UserActivity> allPosts = UserActivityDao.getInstance().findAllPosts();

    }





}