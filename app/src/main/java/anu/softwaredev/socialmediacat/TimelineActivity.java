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

        RecyclerView rvTimeline = (RecyclerView) findViewById(R.id.rv_timeline);                        // Timeline (the Recycler View)
        List<UserActivity> allPosts = getAllPosts(true, true, false);                  // Data
        TimelineAdapter timelineAdapter = new TimelineAdapter(getApplicationContext(), allPosts);        // Adapter
        rvTimeline.setAdapter(timelineAdapter);
        rvTimeline.setLayoutManager(new GridLayoutManager(this, 2));                    // (Grid Layout)
            // rvTimeline.setLayoutManager(new LinearLayoutManager(this));                              // (Linear: ≈IG)

    }

    /** Dataset */
    public List<UserActivity> getAllPosts(boolean dummy, boolean csv, boolean json) {

        if (csv) {
            List<Post> posts = postsFromInfo();
            for (Post post : posts){
                UserActivityDao.getInstance().createPost("@"+post.getUId(), post.getCategory(), post.getPostId(), post.getContent());
            }
        }

        if (json) {
            // TODO
        }

        if (dummy){
            UserActivityDao.getInstance().createPost("Formal", "post02", "Welcome", "uId02");
            UserActivityDao.getInstance().createPost("Sports", "post03", "Running ... ...", "uId03");
            UserActivityDao.getInstance().createPost("Casual", "post01", "Hi", "uId01");
        }

        return UserActivityDao.getInstance().findAllPosts();
    }


    /** load Posts from CSV file */
    public List<Post> postsFromInfo(){
        BufferedReader bufferedReader;
        List<Post> postsFound = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("posts.csv"), StandardCharsets.UTF_8));
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