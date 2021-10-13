package anu.softwaredev.socialmediacat.Util;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import anu.softwaredev.socialmediacat.R;
import anu.softwaredev.socialmediacat.dao.decorator.User;

public class JsonTest extends AppCompatActivity {

    TextView TV1;
    TextView TV2;
    TextView TV3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_test);


    }

    // Trivial Test
    public static User createUser() {

        // Trash data
        String uId = "1";
        String userName = "user1";
        String emailAddress = "user1@a.com";
        String proPicLink = "https://google.com";
        List<String> followings = new ArrayList<>(Arrays.asList("2", "3"));

        return new User(uId, userName, emailAddress, proPicLink, followings);

    }

    /** read files stored in the Assets folder. TODO: Test */
    public String readJSONAsString(String fileName) {
        BufferedReader bufferedReader;              // Declare a buffered reader
        StringBuilder stringBuilder = new StringBuilder();
        Integer id = null;
        try {
            // Create a new instance of a BufferedReader & provide InputStreamReader with file to read (UTF_8 encoding)
            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open(fileName), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "IO Exception!!!", Toast.LENGTH_SHORT).show();

        } finally {
            return stringBuilder.toString();
        }
    }



    /** Logcat test */
    public static void logUsersFromJson(String jsonFileString) {
        Log.i("data", jsonFileString);

        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<User>>() { }.getType();
        List<User> users = gson.fromJson(jsonFileString, listUserType);
        for (int i=0; i < users.size(); i++) {
            Log.i("data", "> Item " + i + "\n" + users.get(i));
        }

    }


}