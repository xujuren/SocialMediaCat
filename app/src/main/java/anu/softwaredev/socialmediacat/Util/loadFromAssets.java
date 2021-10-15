package anu.softwaredev.socialmediacat.Util;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivity;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivityDao;
// TODO
/** Load data instances from different local files (Assets) */
public abstract class loadFromAssets {

    // TODO
    // 1) create-post : WITHOUT Post ID
        // Instances, Methods here ([4])
    // 2) (Posts: WITH POST ID)

    // public abstract List<Post> getDataFromAsset(Context ctx, String fileName);

    /** Template Method */
    public abstract List<UserActivity> actionsFromAssets(Context ctx);
    public abstract List<Post> postsFromAssets(Context ctx);        // TODO: DIRECT (existing Posts) > still create (with PID, likes)

    // Methods - loading data  (now Create Post only) TODO
    /** Method (1) load activities > create posts (TODO)  */
    public List<Post> postsToCreate(Context ctx) {

        // 1) Get Data (List<UserActivity>):        loadFromAssets()
        List<UserActivity> userActsFromCsv = actionsFromAssets(ctx);

        // 2) Check Activity Type, (Filter?)
        List<Post> postsToCreate = new ArrayList<>();

        for (UserActivity userAct : userActsFromCsv) {              // TODO (1) to POSTS
            if (userAct.getAction().equals("create-post")) {
                String uId = userAct.getUId();                      // TODO to NAME
                String tags = userAct.getTags();
                String postId = userAct.getPostId();                // TODO - create post should be WITHOUT PID
                String content = userAct.getContent();
                postsToCreate.add(new Post(uId, tags, postId, content));
            }
        }

        return postsToCreate;
    }


    /** Method (2) Posts >> TODO create?  */
    public List<Post> readPosts(Context ctx, String fileName) {

        // 1) GetPosts
        List<Post> existingPosts = postsFromAssets(ctx);

        //TODO read all? create?

        return existingPosts;
    }


    /** create posts from data instances provided
     * Dataset: TODO +Sources */
    public static void createPostsfromDataInstances(Context ctx) {

        // load data from sources
        boolean csv = true;
        boolean bespoke = true;
        boolean json = true;
        boolean dummy = true;           // test

        /** (1) Activity: Create Posts */       // no POSTID - TODO (but HOW LIKE)
        List<Post> postsToCreate = new ArrayList<>();

        if (csv) {
            loadFromCSV loadCSV = new loadFromCSV();
            postsToCreate.addAll(loadCSV.postsToCreate(ctx));
        }

        if (bespoke) {
            loadFromBespoke loadBespoke = new loadFromBespoke();
            postsToCreate.addAll(loadBespoke.postsToCreate(ctx));
        }

        if (json) {} // TODO - not relevant for ACTIONS

        if (dummy){
            Post post1 = new Post("dummy1", "#test", "pId00", "just create it");
            List<Post> dummyPosts = new ArrayList<>(Arrays.asList(post1));
            postsToCreate.addAll(dummyPosts);
            //Post post2 = new Post("dummy2", "test", "pId00", "Running ... ...");
            //Post post3 = new Post("dummy3", "#test", "pId00", "3rd...");
        }

        for (Post post : postsToCreate){
            UserActivityDao.getInstance().createPost("@"+post.getUId(), post.getTags(), post.getPostId(), post.getContent());
        }

    }







}
