package anu.softwaredev.socialmediacat.Util;
import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivity;

/** For reading and parsing of Bespoken Files */
public class BespokeHandler extends AssetHandler {

    @Override
    public List<UserActivity> actionsFromAssets(Context ctx) {
        BufferedReader bufferedReader;
        List<UserActivity> userActs = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ctx.getAssets().open("userActions.txt"), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                int startBr = line.indexOf('(');
                int endBr = line.indexOf(')');
                if (startBr != 0 || endBr == -1) {
                    continue;
                }
                String action = line.substring(0, startBr);
                String params = line.substring(startBr + 1, endBr);
                String[] tokens = params.split("; ");

                if (tokens.length==4) {
                    userActs.add(new UserActivity(action, tokens[0], tokens[1], tokens[2], Integer.parseInt(tokens[3])));
                } else if (tokens.length==2) {
                    userActs.add(new UserActivity(action, tokens[0], tokens[1]));
                }
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


    /** Randomly return a quote from that stored as an Asset */
    public String quoteFromAssets(Context ctx) {
        BufferedReader bufferedReader;
        List<String> quotesFound = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ctx.getAssets().open("quotes.txt"), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                quotesFound.add(line);
            }
            bufferedReader.close();

        } catch (IOException e) {
            Toast.makeText(ctx, "IO Exception!!!", Toast.LENGTH_SHORT).show();
            return null;

        }

        Random r = new Random();
        int randIdx = r.nextInt(quotesFound.size());
        return quotesFound.get(randIdx);
    }

}
