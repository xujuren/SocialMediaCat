package anu.softwaredev.socialmediacat.Util;
import android.content.Context;

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
public abstract class AssetHandler {

    /** Template Methods */
    public abstract List<UserActivity> actionsFromAssets(Context ctx);
    public abstract List<Post> postsFromAssets(Context ctx);        // TODO: DIRECT (existing Posts) > still create (with PID, likes)



    // load action data
    public static List<UserActivity> actionsFromDataInstances(Context ctx) {
        List<String> fileTypes = new ArrayList<>(Arrays.asList("csv", "txt"));; // "json", "dummy"

        // create post
        List<UserActivity> postsToCreate = new ArrayList<>();
        for (String type : fileTypes) {
            AssetHandlerFactory assetHandlerFty = new AssetHandlerFactory();
            AssetHandler assetHandler = assetHandlerFty.createHandler(type);
            List<UserActivity> result = assetHandler.actionsFromAssets(ctx);

            if (result!=null && result.size()!=0) {
                postsToCreate.addAll(result);
            }

        }

        return postsToCreate;
    }

    // from actions TODO start;     // TODO  LIKES if it's added
    public static void createPostsFromDataInstances(Context ctx) {
        List<UserActivity> userActs = actionsFromDataInstances(ctx);

        // (1) create posts
        List<UserActivity> createPostActs = new ArrayList<>();

        for (UserActivity userAct : userActs) {
            if (userAct.getAction().equals("create-post")) {
                createPostActs.add(new UserActivity(userAct.getUId(), userAct.getTags(), userAct.getTags(), userAct.getContent(), userAct.getPhotoId()));
            }
        }
        createPosts(createPostActs);

    }

    /** ACTIONS (1) create posts from data instances created */
    public static void createPosts(List<UserActivity> data) {

        int size = data.size();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            int i=0;
            @Override
            public void run() {
                if (i <size){
                    UserActivity postData = data.get(i);
                    UserActivityDao.getInstance().createPost(postData.getUId(), postData.getTags(), postData.getContent(), postData.getPhotoId());
                }
                i++;
            }
        }, 0, 20*1000);

    }



    /** Load posts from data instances  */
    public static void loadPostsfromDataInstances(Context ctx) {

        List<Post> posts = new ArrayList<>();
        List<String> fileTypes = new ArrayList<>(Arrays.asList("csv", "txt")); // "json", "dummy"

        for (String type : fileTypes) {
            AssetHandlerFactory assetHandlerFty = new AssetHandlerFactory();
            AssetHandler assetHandler = assetHandlerFty.createHandler(type);
            List<Post> result = assetHandler.postsFromAssets(ctx);
            if (result!=null && result.size()!=0) {
                posts.addAll(result);
            }
        }

        // load Posts
        for (Post post : posts){
            UserActivityDao.getInstance().loadPost(post.getUId(), post.getTags(), post.getPostId(), post.getContent(), post.getPhotoId(), post.getLikes());
        }

    }


    /* test  */
    public static List<Post> loadPostsfromDataInstances_directly(Context ctx) {

        List<Post> posts = new ArrayList<>();
        List<String> fileTypes = new ArrayList<>(Arrays.asList("csv", "txt")); // "json", "dummy"

        for (String type : fileTypes) {
            AssetHandlerFactory assetHandlerFty = new AssetHandlerFactory();
            AssetHandler assetHandler = assetHandlerFty.createHandler(type);

            List<Post> result = assetHandler.postsFromAssets(ctx);
            if (result!=null && result.size()!=0) {
                posts.addAll(result);
            }
        }
        return posts;


    }



}
