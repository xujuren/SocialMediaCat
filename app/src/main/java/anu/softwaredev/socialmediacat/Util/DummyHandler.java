package anu.softwaredev.socialmediacat.Util;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivity;

/** For testing only*/
public class DummyHandler extends AssetHandler {
    @Override
    public List<UserActivity> actionsFromAssets(Context ctx) {

        // create posts
        List<UserActivity> actions = new ArrayList<>();
        actions.add(new UserActivity("create-post", "dummy1", "#test", "just created, photoiD -20", -20));
        actions.add(new UserActivity("create-post", "dummy2", "#test", "just created, photoiD 101", 101));
        actions.add(new UserActivity("create-post", "dummy3", "#test", "just created, photoiD 50", 30));
        return actions;
    }

    @Override
    public List<Post> postsFromAssets(Context ctx) {
        // Post post1 = new Post("dummy1", "#test", "post here, photoiD30 & 100 likes", 30, 100);
        return null;
    }
}
