package anu.softwaredev.socialmediacat.Util;
import android.content.Context;

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
        List<UserActivity> userActs = actionsFromDataInstances(ctx);
        streamOfData(userActs);
    }

    // load activities
    public static List<UserActivity> actionsFromDataInstances(Context ctx) {
        List<String> fileTypes = new ArrayList<>(Arrays.asList("csv", "txt")); // "dummy" for testing

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

    /* A Stream of activities (with data instances) */
    public static void streamOfData(List<UserActivity> data) {
//
//        // M2
//        Runnable runnableTask = () -> {
//            try {
//                TimeUnit.SECONDS.sleep(3);
//                System.out.println("in Task - data.size(): " + data.size());
//                if (data.size()>0) {
//                    UserActivity act = data.get(0);
//                    data.remove(0);
//                    System.out.println(act.toString());
//                    if (act!=null) {
//                        if (act.getAction().equals("CP")){
//                            UserActivityDao.getInstance().createPost(act.getUId(), act.getTag(), act.getContent(), act.getPhotoId());
//                        } else if (act.getAction().equals("LP")){
//                            UserActivityDao.getInstance().likePost(act.getPostId());
//                        } else if (act.getAction().equals("DP")){
//                            UserActivityDao.getInstance().deletePost(act.getPostId());
//                        }
//                    }
//                }
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        };
//        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//        ScheduledFuture<?> resultFuture = executorService.scheduleAtFixedRate(runnableTask, 2, 10, TimeUnit.SECONDS);


        // M1 - scheduleAtFixedRate (public {synchronized} void run())
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            private int i=0;
            private int size = data.size();

            @Override
            public synchronized void run() {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                 }

                // TODO
                System.out.println("run() - i: " + i);
                if (i <size) {
                    UserActivity act = data.get(i);
                    System.out.println(act.toString());
                    if (act!=null) {
                        if (act.getAction().equals("CP")){
                            UserActivityDao.getInstance().createPost(act.getUId(), act.getTag(), act.getContent(), act.getPhotoId());
                        } else if (act.getAction().equals("LP")){
                            UserActivityDao.getInstance().likePost(act.getPostId());
                        } else if (act.getAction().equals("DP")){
                            UserActivityDao.getInstance().deletePost(act.getPostId());
                        }
                    }
                }
                i++;
            }


        }, 1000, 20000);

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

        System.out.println(" === loadPostsfromDataInstances === === ");
        for (Post post : posts) {
            System.out.println(post);
        }

        UserActivityDao.getInstance().storePost(posts);
    }


}
