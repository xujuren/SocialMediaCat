package anu.softwaredev.socialmediacat.Util;
import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivity;

public class CSVHandler extends AssetHandler {

    @Override
    public List<UserActivity> actionsFromAssets(Context ctx) {
        BufferedReader bufferedReader;
        List<UserActivity> userActs = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ctx.getAssets().open("userActions.csv"), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length==5 && tokens[0].charAt(0)!='/'){
                    userActs.add(new UserActivity(tokens[0], tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4])));
                } // else ignore (or OTHER actions, if any)
            }
            bufferedReader.close();

        } catch (IOException e) {
            Toast.makeText(ctx, "IO Exception while loading CSV actions", Toast.LENGTH_SHORT).show();
            return null;

        } finally {
            return userActs;
        }
    }

    @Override
    public List<Post> postsFromAssets(Context ctx) {
        BufferedReader bufferedReader;
        List<Post> postsFound = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ctx.getAssets().open("posts.csv"), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length==6 && tokens[0].charAt(0)!='/'){
                    postsFound.add(new Post(tokens[0], tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5])));
                } // else ignore
            }
            bufferedReader.close();

        } catch (IOException e) {
            Toast.makeText(ctx, "IO Exception while loading CSV posts", Toast.LENGTH_SHORT).show();
            return null;

        } finally {
            return postsFound;
        }
    }

}

