package anu.softwaredev.socialmediacat.Util;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivity;
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivityDao;

/** Load data instances from different local files (Assets) */
public abstract class AssetHandler {
    public abstract List<UserActivity> actionsFromAssets(Context ctx);
    public abstract List<Post> postsFromAssets(Context ctx);

    /** perform actions template */
    public static void performActions(Context ctx) {
        // loadPostsfromDataInstances(ctx);
        List<UserActivity> userActDataToStream = actionsFromDataInstances(ctx);
        streamOfData(userActDataToStream); // TODO remove param?
    }

    /** load activities */
    public static List<UserActivity> actionsFromDataInstances(Context ctx) {
        List<String> fileTypes = new ArrayList<>(Arrays.asList("csv", "txt"));

        List<UserActivity> actions = new ArrayList<>();
        for (String type : fileTypes) {
            AssetHandlerFactory assetHandlerFty = new AssetHandlerFactory();
            AssetHandler assetHandler = assetHandlerFty.createHandler(type);

            List<UserActivity> results = assetHandler.actionsFromAssets(ctx);
            if (results!=null && results.size()!=0) {
                actions.addAll(results);
            }
        }
        return actions;
    }


    /** A Stream of activities (with data instances) */
    public static void streamOfData(List<UserActivity> data) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @SuppressWarnings("SynchronizeOnNonFinalField")
            @Override
            public synchronized void run() {
                synchronized (data) {
                    try {
                        UserActivity act = data.get(0);
                        data.remove(0);
                        if (act!=null) {
                            if (act.getAction().equals("CP")) {
                                UserActivityDao.getInstance().createPost(act.getUId(), act.getTag(), act.getContent(), act.getPhotoId());
                            } else if (act.getAction().equals("LP")) {
                                UserActivityDao.getInstance().likePost(act.getPostId());
                            } else if (act.getAction().equals("DP")) {
                                UserActivityDao.getInstance().deletePost(act.getPostId());
                            }
                        }
                    } catch (Exception e){
                        Thread.currentThread().interrupt();
                        Log.e("Error", e.toString());
                    }
                }

            }

        }, 1000, 10000);

    }


    /** Load posts from data instances  */
    public static void loadPostsfromDataInstances(Context ctx) {
        List<Post> posts = new ArrayList<>();
        List<String> fileTypes = new ArrayList<>(Arrays.asList("csv", "txt", "json"));

        for (String type : fileTypes) {
            AssetHandlerFactory assetHandlerFty = new AssetHandlerFactory();
            AssetHandler assetHandler = assetHandlerFty.createHandler(type);
            List<Post> result = assetHandler.postsFromAssets(ctx);
            if (result!=null && result.size()!=0) {
                posts.addAll(result);
            }
        }

        UserActivityDao.getInstance().loadPosts(posts);
    }



    /** Load quotes from asset and displayed on the main page */
    public static void loadQuotes(Context ctx, TextView quoteTv) {
        BespokeHandler handler = new BespokeHandler();
        String quote = handler.quoteFromAssets(ctx);
        quoteTv.setText(quote);
    }



}
