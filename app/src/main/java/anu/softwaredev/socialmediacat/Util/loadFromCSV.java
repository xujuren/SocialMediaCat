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
import anu.softwaredev.socialmediacat.dao.decorator.UserActivity;

public class loadFromCSV extends loadFromAssets {

    @Override
    public List<UserActivity> actionsFromAssets(Context ctx) {
        BufferedReader bufferedReader;
        List<UserActivity> userActs = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ctx.getAssets().open("userActions.csv"), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length==5) {
                    userActs.add(new UserActivity(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]));
                } else if (tokens.length==4) {
                    userActs.add(new UserActivity(tokens[0], tokens[1], tokens[2], "", tokens[3]));    // TODO - % POSTID (if CRASH?)
                } // else ignore
            }
            bufferedReader.close();

        } catch (IOException e) {
            Toast.makeText(ctx, "IO Exception while loading actions", Toast.LENGTH_SHORT).show();
            return null;

        } finally {
            if (userActs != null && userActs.size()>0){
                userActs.remove(0);     // remove header
            }
            return userActs;
        }
    }

    @Override
    public List<Post> postsFromAssets(Context ctx) {
        BufferedReader bufferedReader;
        List<Post> posts = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ctx.getAssets().open("posts.csv"), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length!=4) {continue;}
                posts.add(new Post(tokens[0], tokens[1], tokens[2], tokens[3]));        // Not yet check Format etc. TODO
            }
            bufferedReader.close();

        } catch (IOException e) {
            System.out.println("IO Exception!!");
            return null;

        } finally {
            if (posts != null && posts.size()>0){
                posts.remove(0);     // remove header
            }
            return posts;
        }
    }

}

