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

public class BespokeHandler extends AssetHandler {

    @Override
    public List<UserActivity> actionsFromAssets(Context ctx) {
        BufferedReader bufferedReader;
        List<UserActivity> userActs = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ctx.getAssets().open("userActions.txt"), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {    // create-post(u11, cat1, p01, hi hi hi hi)
                int startBr = line.indexOf('(');                    // action name
                int endBr = line.indexOf(')');
                if (startBr!=0 || endBr==-1) {continue;}
                String action = line.substring(0, startBr);

                String params = line.substring(startBr+1, endBr);
                String[] tokens = params.split("; ");

                userActs.add(new UserActivity(action, tokens[0], tokens[1], tokens[2]));

            }
            bufferedReader.close();

        } catch (IOException e) {
            Toast.makeText(ctx, "IO Exception!!!", Toast.LENGTH_SHORT).show();

        } finally {
            return userActs;
        }
    }

    @Override
    public List<Post> postsFromAssets(Context ctx) {
        BufferedReader bufferedReader;
        List<Post> postsFound = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ctx.getAssets().open("posts.txt"), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                int startBr = line.indexOf('(');
                int endBr = line.indexOf(')');
                if (startBr!=0 || endBr==-1) {continue;}
                String params = line.substring(startBr+1, endBr);
                String[] tokens = params.split("; ");
                if (tokens.length==6){
                    postsFound.add(new Post(tokens[0], tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5])));
                } // else ignore
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
