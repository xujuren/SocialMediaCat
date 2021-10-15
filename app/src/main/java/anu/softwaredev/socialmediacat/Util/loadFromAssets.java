package anu.softwaredev.socialmediacat.Util;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
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

        for (UserActivity userAct : userActsFromCsv) {             // TODO (1) to POSTS
            if (userAct.getAction().equals("create-post")) {
                String uId = userAct.getUId();                      // TODO to NAME
                String tags = userAct.getTags();
                String postId = userAct.getPostId();
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


    /** Dataset: TODO +Sources */
    public static void createPostsfromDataInstances(Context ctx) {

        // load data from sources
        boolean csv = true;                    // TODO - FAIL??
        boolean bespoke = false;            // TODO
        boolean json = false;
        boolean dummy = true;           // test

        /** (1) Activity: Create Posts */       // no POSTID - TODO (but HOW LIKE)
        // TODO  - List<Post> posts =

        if (csv) {
            loadFromCSV loadCSV = new loadFromCSV();
            List<Post> posts = loadCSV.postsToCreate(ctx);
            for (Post post : posts){
                UserActivityDao.getInstance().createPost("@"+post.getUId(), post.getTags(), post.getPostId(), post.getContent());
            }
        }

        /** below tbd, test CSV first*/
        if (bespoke) {
            loadFromBespoke loadBespoke = new loadFromBespoke();
            List<Post> posts = loadBespoke.postsToCreate(ctx);
            for (Post post : posts){
                UserActivityDao.getInstance().createPost("@"+post.getUId(), post.getTags(), post.getPostId(), post.getContent());
            }
        }

        if (json) {
//            List<Post> posts = loadFromJson.postsToCreate(ctx);
//            for (Post post : posts){
//                UserActivityDao.getInstance().createPost("@"+post.getUId(), post.getTags(), post.getPostId(), post.getContent());
//            }
        }

        if (dummy){
            UserActivityDao.getInstance().createPost("dummy1", "#test", "pId00", "just create it");
            //UserActivityDao.getInstance().createPost("uId03", "Sports", "post03", "Running ... ...");
            //UserActivityDao.getInstance().createPost("uId01", "Casual", "post01", "Hi");
        }

        /** Test: Create > load directly */
        // return UserActivityDao.getInstance().findAllPosts();
    }







}
