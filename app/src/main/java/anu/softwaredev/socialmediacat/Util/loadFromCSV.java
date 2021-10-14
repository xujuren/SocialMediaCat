package anu.softwaredev.socialmediacat.Util;
import android.content.Context;
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
    public List<UserActivity> loadFromAssets(Context ctx, String fileName) {
        BufferedReader bufferedReader;
        List<UserActivity> userActs = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ctx.getAssets().open(fileName), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length!=5) {continue;}
                userActs.add(new UserActivity(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]));
            }
            bufferedReader.close();

        } catch (IOException e) {
            System.out.println("IO Exception!!");
            return null;

        } finally {
            if (userActs != null && userActs.size()>0){
                userActs.remove(0);     // remove header
            }
            return userActs;
        }
    }

}

