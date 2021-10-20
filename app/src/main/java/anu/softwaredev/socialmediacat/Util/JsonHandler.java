package anu.softwaredev.socialmediacat.Util;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivity;

/** TBD: current JSON data instances only include posts*/
public class JsonHandler extends AssetHandler {

    // Not developed for the Assignment
    @Override
    public List<UserActivity> actionsFromAssets(Context ctx) {
        return null;
    }

    // TODO data
    @Override
    public List<Post> postsFromAssets(Context ctx) {
        String jsonStr = getJsonFromAssets(ctx);
        Gson gson = new Gson();
        Type listPostType = new TypeToken<List<Post>>() {}.getType();

        return gson.fromJson(jsonStr, listPostType);
    }


    // get JSON String
    public static String getJsonFromAssets(Context ctx){
        String jsonStr = null;
        try {
            InputStream is = ctx.getAssets().open("posts.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonStr = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            return jsonStr;
        }
    }


}
