package anu.softwaredev.socialmediacat;

import static anu.softwaredev.socialmediacat.Util.loadFromAssets.createPostsFromCsv;
import static anu.softwaredev.socialmediacat.Util.loadFromAssets.getDataFromBespoke;
import static anu.softwaredev.socialmediacat.Util.loadFromAssets.loadUserActFromCSV;
import static anu.softwaredev.socialmediacat.Util.loadFromAssets.loadPostsFromCSV;
import static anu.softwaredev.socialmediacat.Util.loadFromAssets.getDataFromJson;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;

import java.util.List;

import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivity;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivityDao;

public class TimelineActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        RecyclerView rvTimeline = (RecyclerView) findViewById(R.id.rv_timeline);                        // Timeline (the Recycler View)
        List<UserActivity> allPosts = getAllPosts(true, false, false,false);                  // Data
        TimelineAdapter timelineAdapter = new TimelineAdapter(getApplicationContext(), allPosts);        // Adapter
        rvTimeline.setAdapter(timelineAdapter);
        rvTimeline.setLayoutManager(new GridLayoutManager(this, 2));                    // (Grid Layout)
            // rvTimeline.setLayoutManager(new LinearLayoutManager(this));                              // (Linear: ≈IG)

    }

    /** Dataset: Sources ((just loading POSTS info >> CREATE from file!?) */
    public List<UserActivity> getAllPosts(boolean csv, boolean bespoke, boolean json, boolean dummy) {

        /** TODO Testing (from below) */
        if (csv) {
            List<Post> posts = createPostsFromCsv(getApplicationContext(), "posts.csv");
            for (Post post : posts){
                UserActivityDao.getInstance().createPost("@"+post.getUId(), post.getCategory(), post.getPostId(), post.getContent());
            }
        }

//        if (csv) {
//            List<Post> posts = loadPostsFromCSV(getApplicationContext(), "posts.csv");
//            for (Post post : posts){
//                UserActivityDao.getInstance().createPost("@"+post.getUId(), post.getCategory(), post.getPostId(), post.getContent());
//            }
//        }

        if (bespoke) {
            List<Post> posts = getDataFromBespoke(getApplicationContext(), "posts_bespoken.txt");
            for (Post post : posts){
                UserActivityDao.getInstance().createPost("@"+post.getUId(), post.getCategory(), post.getPostId(), post.getContent());
            }
        }

        if (json) {
            List<Post> posts = getDataFromJson(getApplicationContext(), "posts.json");
            for (Post post : posts){
                UserActivityDao.getInstance().createPost("@"+post.getUId(), post.getCategory(), post.getPostId(), post.getContent());
            }
        }

        if (dummy){
            // UserActivityDao.getInstance().createPost("uId02", "Formal", "post02", "Welcome");
            //UserActivityDao.getInstance().createPost("uId03", "Sports", "post03", "Running ... ...");
            //UserActivityDao.getInstance().createPost("uId01", "Casual", "post01", "Hi");
        }

        // All
        return UserActivityDao.getInstance().findAllPosts();
    }







}