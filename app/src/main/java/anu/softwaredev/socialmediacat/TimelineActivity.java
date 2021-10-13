package anu.softwaredev.socialmediacat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

        // TODO Test: (Dummy Posts) TODO Testing
        if (false){
            UserActivityDao.getInstance().createPost("Formal", "post02", "Welcome", "uId02");
            UserActivityDao.getInstance().createPost("Sports", "post03", "Running ... ...", "uId03");
            UserActivityDao.getInstance().createPost("Casual", "post01", "Hi", "uId01");

            return UserActivityDao.getInstance().findAllPosts();

        } else {
            List<Post> posts = postsFromInfo();
            for (Post post : posts){
                UserActivityDao.getInstance().createPost(post.getUId(), post.getCategory(), post.getPostId(), post.getContent());
            }

            return UserActivityDao.getInstance().findAllPosts();

        }

        // return UserActivityDao.getInstance().findAllPosts();
    }


    /** [Test: re Lab] read CSV file in the Assets folder - ALL */
    public List<Post> postsFromInfo(){
        BufferedReader bufferedReader;
        List<Post> postsFound = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("loginDetails.csv"), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (!tokens[0].equals("uId")) {                // excl Header
                    postsFound.add(new Post(tokens[0], tokens[1], tokens[2], tokens[3]));
                }
            }
            bufferedReader.close();

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "IO Exception!!!", Toast.LENGTH_SHORT).show();

        } finally {
            return postsFound;
        }
    }





}