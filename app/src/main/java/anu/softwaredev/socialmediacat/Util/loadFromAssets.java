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

public abstract class loadFromAssets {

    public abstract List<Post> getDataFromAsset(Context ctx, String fileName);

    /** get JSON (POSTS only) */
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


    /** load From CSV (TODO - CREATE post only) */
    public static List<Post> getDataFromCSV(Context ctx, String fileName) {
        BufferedReader bufferedReader;
        List<Post> postsFound = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ctx.getAssets().open(fileName), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (!tokens[0].equals("uId")) {                // excl Header
                    postsFound.add(new Post(tokens[0], tokens[1], tokens[2], tokens[3]));
                }
            }
            bufferedReader.close();

        } catch (IOException e) {
            System.out.println("IO Exception!!");
            return null;

        } finally {
            return postsFound;
        }
    }

    /** load from Bespoken (TODO - POSTS only) */

    /** load From Bespoke (TODO - CREATE post only) */
    public static List<Post> getDataFromBespoke(Context ctx, String fileName){
        BufferedReader bufferedReader;
        List<Post> postsFound = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ctx.getAssets().open(fileName), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {    // create-post(u11, cat1, p01, hi hi hi hi)
                int startBr = line.indexOf('(');
                int endBr = line.indexOf(')');
                if (startBr==-1 || endBr==-1) {continue;}

                // String action = line.substring(0, startBr);
                // if (action=="create-post")
                String params = line.substring(startBr+1, endBr);
                String[] tokens = params.split(", ");

                postsFound.add(new Post(tokens[0], tokens[1], tokens[2], tokens[3]));

            }
            bufferedReader.close();

        } catch (IOException e) {
            Toast.makeText(ctx, "IO Exception!!!", Toast.LENGTH_SHORT).show();
            return null;

        } finally {
            return postsFound;
        }
    }



}
