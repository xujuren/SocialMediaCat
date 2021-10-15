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
import java.util.Timer;
import java.util.TimerTask;

import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivity;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivityDao;
// TODO
/** Load data instances from different local files (Assets) */
public abstract class loadFromAssets {

    /** Template Methods */
    public abstract List<UserActivity> actionsFromAssets(Context ctx);
    public abstract List<Post> postsFromAssets(Context ctx);        // TODO: DIRECT (existing Posts) > still create (with PID, likes)

    // Methods - loading data  (now Create Post only) TODO
    /** Method (1) load activities > create posts (TODO)  */
    public List<Post> postsToCreate(Context ctx) {

        // 1) Get Data (List<UserActivity>):        loadFromAssets()
        List<UserActivity> userActsFromCsv = actionsFromAssets(ctx);        // change to NOT <UserAct>??

        // 2) Check Activity Type
        List<Post> postsToCreate = new ArrayList<>();
        for (UserActivity userAct : userActsFromCsv) {
            if (userAct.getAction().equals("create-post")) {
                String uId = userAct.getUId();
                String tags = userAct.getTags();
                String content = userAct.getContent();
                int photoId = userAct.getPhotoId();
                postsToCreate.add(new Post(uId, tags, content, photoId));
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


    /** create posts from data instances provided */
    public static void createPost_FixedTimeInterval(Context ctx) {

        List<Post> postsData = createPostsfromDataInstances(ctx);        // assume called first
        int size = postsData.size();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            int i=0;
            @Override
            public void run() {
                if (i <size){
                    Post postData = postsData.get(i);
                    UserActivityDao.getInstance().createPost(postData.getUId(), postData.getTags(), postData.getContent(), postData.getPhotoId());
                }
                i++;
            }
        }, 0, 20*1000);

    }

    // TODO - lo// TODO - add Action: also for (LIKES) if we add it


    // load the data for create post
    public static List<Post> createPostsfromDataInstances(Context ctx) {

        // load actions from assets
        boolean csv = true;
        boolean bespoke = true;
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

        if (dummy){
            Post post1 = new Post("dummy1", "#test", "", "just create it, empty photoId & photoiD-1, 100 likes", -1, 100);
            List<Post> dummyPosts = new ArrayList<>(Arrays.asList(post1));
            postsToCreate.addAll(dummyPosts);
            //Post post2 = new Post("dummy2", "test", "pId00", "Running ... ...");
            //Post post3 = new Post("dummy3", "#test", "pId00", "3rd...");
        }

        for (Post post : postsToCreate){

            // UserActivityDao.getInstance().createPost(post.getUId(), post.getTags(), post.getContent(), post.getPhotoId());
        }

        return postsToCreate;
    }

    /** Load existing posts from data instances  */
    public static void loadPostsfromDataInstances(Context ctx) {

        // load posts from assets
        boolean csv = true;
        boolean bespoke = true;
        boolean json = true;
        boolean dummy = true;           // test

        List<Post> postsLoaded = new ArrayList<>();

        if (csv) {
            loadFromCSV loadCSV = new loadFromCSV();
            postsLoaded.addAll(loadCSV.postsFromAssets(ctx));
        }

        if (bespoke) {
            loadFromBespoke loadBespoke = new loadFromBespoke();
            postsLoaded.addAll(loadBespoke.postsFromAssets(ctx));
        }

        if (json) {
            loadFromJson loadJson = new loadFromJson();
            postsLoaded.addAll(loadJson.postsFromAssets(ctx));
        }

        if (dummy){
            Post post1 = new Post("dummy1", "#test", "p00", "just create it", 50);
            Post post2 = new Post("dummy2", "test", "p00", "Running ... ...", 50);
            List<Post> dummyPosts = new ArrayList<>(Arrays.asList(post1, post2));
            postsLoaded.addAll(dummyPosts);
        }

        // load existing posts from assets
        for (Post post : postsLoaded){
            UserActivityDao.getInstance().loadPost(post.getUId(), post.getTags(), post.getPostId(), post.getContent(), post.getPhotoId(), post.getLikes());
        }

    }






}
