package anu.softwaredev.socialmediacat.Util;
import android.content.Context;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivity;

public abstract class loadFromAssets {

    // public abstract List<Post> getDataFromAsset(Context ctx, String fileName);
    /** Template Method */
    public abstract List<UserActivity> loadFromAssets(Context ctx, String fileName);


    /** Method: TODO Testing - now Create Post only  */
    public List<Post> postsToCreate(Context ctx, String fileName) {

        // 1) Get Data (List<UserActivity>):        loadFromAssets()
        List<UserActivity> userActsFromCsv = loadFromAssets(ctx, fileName);

        // 2) Check Activity Type, (Filter?)
        List<Post> postsCreated = new ArrayList<>();

        for (UserActivity userAct : userActsFromCsv) {             // TODO (1) to POSTS
            if (userAct.getAction().equals("create-post")) {
                String uId = userAct.getUId();                      // TODO to NAME
                String category = userAct.getCategory();
                String postId = userAct.getPostId();
                String content = userAct.getContent();
                postsCreated.add(new Post(uId, category, postId, content));
            }
        }

        return postsCreated;
    }


    /** get JSON (TODO - CREATE post only) (current .json's structure) */
    public static List<Post> getDataFromJson(Context ctx, String fileName) {
        String jsonStr = getJsonFromAssets(ctx, fileName);
        Gson gson = new Gson();
        Type listPostType = new TypeToken<List<Post>>() {}.getType();

        return gson.fromJson(jsonStr, listPostType);
    }

    // JSON > String
    public static String getJsonFromAssets(Context ctx, String fileName){
        String jsonStr = null;
        try {
            InputStream is = ctx.getAssets().open(fileName);
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
