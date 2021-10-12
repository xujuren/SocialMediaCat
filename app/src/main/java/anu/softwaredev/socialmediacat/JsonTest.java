package anu.softwaredev.socialmediacat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JsonTest extends AppCompatActivity {

    TextView TV1;
    TextView TV2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_test);

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

}