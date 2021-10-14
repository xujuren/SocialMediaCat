package anu.softwaredev.socialmediacat.Util;
import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivity;

public class loadFromBespoke extends loadFromAssets {

    @Override
    public List<UserActivity> loadFromAssets(Context ctx, String fileName) {

        BufferedReader bufferedReader;
        List<UserActivity> userActs = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ctx.getAssets().open(fileName), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {    // create-post(u11, cat1, p01, hi hi hi hi)
                int startBr = line.indexOf('(');                    // action name
                int endBr = line.indexOf(')');
                if (startBr==-1 || endBr==-1) {continue;}
                String action = line.substring(0, startBr);

                String params = line.substring(startBr+1, endBr);
                String[] tokens = params.split(", ");

                userActs.add(new UserActivity(action, tokens[0], tokens[1], tokens[2], tokens[3]));

            }
            bufferedReader.close();

        } catch (IOException e) {
            Toast.makeText(ctx, "IO Exception!!!", Toast.LENGTH_SHORT).show();

        } finally {
            return userActs;
        }
    }
}
