package anu.softwaredev.socialmediacat.Util;
import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivity;
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivityDao;

/** Load data instances from different local files (Assets) */
public abstract class AssetHandler {

    public abstract List<UserActivity> actionsFromAssets(Context ctx);
    public abstract List<Post> postsFromAssets(Context ctx);

    /** perform actions template */
    public static void performActions(Context ctx) {
        List<UserActivity> userActs = actionsFromDataInstances(ctx);
        streamOfData(userActs);
    }

    // load create post activities
    // TODO - action("create-post"),uId,tags,"content",photoId
    // TODO - action("like-post"),uId,postId
    // TODO - action("del-post"),uId,postId
    public static List<UserActivity> actionsFromDataInstances(Context ctx) {
        List<String> fileTypes = new ArrayList<>(Arrays.asList("csv", "txt")); // "dummy" for testing

        List<UserActivity> actions = new ArrayList<>();
        for (String type : fileTypes) {
            AssetHandlerFactory assetHandlerFty = new AssetHandlerFactory();
            AssetHandler assetHandler = assetHandlerFty.createHandler(type);

            List<UserActivity> result = assetHandler.actionsFromAssets(ctx);
            if (result!=null && result.size()!=0) {
                actions.addAll(result);
            }
        }
        return actions;
    }

    /* A Stream of activities (with data instances) */
    public static void streamOfData(List<UserActivity> data) {
        int size = data.size();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            int i=0;
            @Override
            public void run() {
                if (i <size) {
                    UserActivity act = data.get(i);
                    System.out.println(act.toString());
                    if (act!=null) {
                        if (act.getAction().equals("CP")){
                            UserActivityDao.getInstance().createPost(act.getUId(), act.getTag(), act.getContent(), act.getPhotoId());
                        } else {
                            if (act.getAction().equals("LP")){
                                UserActivityDao.getInstance().likePost(act.getPostId());
                            } else if (act.getAction().equals("DP")){
                                UserActivityDao.getInstance().deletePost(act.getPostId());
                            }
                        }
                    }
                }
                i++;
            }
        }, 0, 20000);

    }


    /** Load posts from data instances  */
    public static void loadPostsfromDataInstances(Context ctx) {

        List<Post> posts = new ArrayList<>();
        List<String> fileTypes = new ArrayList<>(Arrays.asList("csv", "txt", "json")); // "dummy": testing

        for (String type : fileTypes) {
            AssetHandlerFactory assetHandlerFty = new AssetHandlerFactory();
            AssetHandler assetHandler = assetHandlerFty.createHandler(type);
            List<Post> result = assetHandler.postsFromAssets(ctx);
            if (result!=null && result.size()!=0) {
                posts.addAll(result);
            }
        }

        UserActivityDao.getInstance().storePost(posts);
    }


}
