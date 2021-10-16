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

/** Load data instances from different local files (Assets) */
public abstract class AssetHandler {

    public abstract List<UserActivity> actionsFromAssets(Context ctx);
    public abstract List<Post> postsFromAssets(Context ctx);        // TODO: DIRECT (existing Posts) > still create (with PID, likes)

    /** Create Posts template */
    public static void createPosts(Context ctx) {
        List<UserActivity> userActs = actionsFromDataInstances(ctx);                    // load action data
        List<UserActivity> createPostActs = createPostsFromDataInstances(userActs);     // extract: createPost
        createPosts(createPostActs);                                                    // create posts
    }

    // load create post activities
    public static List<UserActivity> actionsFromDataInstances(Context ctx) {
        List<String> fileTypes = new ArrayList<>(Arrays.asList("csv", "txt"));; // "json", "dummy"

        List<UserActivity> createPostActs = new ArrayList<>();
        for (String type : fileTypes) {
            AssetHandlerFactory assetHandlerFty = new AssetHandlerFactory();
            AssetHandler assetHandler = assetHandlerFty.createHandler(type);
            List<UserActivity> result = assetHandler.actionsFromAssets(ctx);
            if (result!=null && result.size()!=0) {
                createPostActs.addAll(result);
            }
        }

        return createPostActs;
    }

    //  TODO  + LIKES if added
    // (1) create posts
    public static List<UserActivity> createPostsFromDataInstances(List<UserActivity> userActs) {
        List<UserActivity> createPostActs = new ArrayList<>();
        for (UserActivity userAct : userActs) {
            if (userAct.getAction().equals("create-post")) {
                createPostActs.add(userAct);
            }
        }
        return createPostActs;
    }

    /* create posts from the postData */
    public static void createPosts(List<UserActivity> data) {
        int size = data.size();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            int i=0;
            @Override
            public void run() {
                if (i <size){
                    UserActivity act = data.get(i);
                    UserActivityDao.getInstance().createPost(act.getUId(), act.getTag(), act.getContent(), act.getPhotoId());
                }
                i++;
            }
        }, 0, 30000);

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

        for (Post post : posts){
            UserActivityDao.getInstance().loadPost(post);
        }

    }



}
