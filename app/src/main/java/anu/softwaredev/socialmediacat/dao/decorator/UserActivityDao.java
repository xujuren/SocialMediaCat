package anu.softwaredev.socialmediacat.dao.decorator;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;


public class UserActivityDao implements IUserActivityDao {

    // Singleton
    private static UserActivityDao instance;
    private static Integer idCount=0;
    private static File file;                       // *csv file to store
    static {
        try {
            file = File.createTempFile("user-action", ".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private UserActivityDao() {};

    @Override           // called: [UserActivityDao.getInstance()].createPost("@woofie", "owuowu, owuowuowu, owu! OWU!");
    public UserActivity createPost(String username, String content) {           // alt: only content (username: below)
        try {
            idCount++;
            String action = "create-post";
            String text = username + ";" + action + ";" + content + ";" + idCount + "\n";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
            }
            System.out.println("anu.softwaredev.socialmediacat.Post saved in " + file.getAbsolutePath());
            UserActivity userAct = new UserActivity(username, action, content, idCount);
            return userAct;

        } catch (IOException e) { e.printStackTrace(); }

        return null;
    }

    @Override
    public UserActivity likePost(String username, Integer idPost) {
        try{
            String action = "like-post";
            String content = "+1";
            String text = username + ";" + action + ";" + content + ";" + idPost + "\n";
            Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
            System.out.println("Like saved in " + file.getAbsolutePath());
            UserActivity userActivity = new UserActivity(username, action, content, idPost);
            return userActivity;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }


    // @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<UserActivity> findAllPosts() {
        List<UserActivity> userActList = new ArrayList<>();
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            String fileContent = new String(bytes);
            String[] lines = fileContent.split("\n");

            if (lines != null) {
                for (String line : lines) {
                    String[] strings = line.split(";");
                    if (strings != null && strings.length == 4) {
                        String uname = strings[0];
                        String action = strings[1];
                        String content = strings[2];
                        Integer id = Integer.parseInt(strings[3]);
                        if ("create-post".equals(action)) {
                            UserActivity userAct = new UserActivity(uname, action, content, idCount);
                            userActList.add(userAct);
                        }
                    }
                }
            }

        } catch (IOException e) { e.printStackTrace(); }

        return userActList;                            // return all posts (by e.g. List<UserActivity> allPosts = UserActivityDao.findAllPosts();)
    }


    @Override
    public String getFilePath() {
        return file.getAbsolutePath();
    }

    @Override
    public void deleteAll() {
        try {
            if (file.exists()){
                file.delete();
            }
            file = File.createTempFile("user-action", ".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // e.g. [UserActivityDao.getInstance()].createPost("@woofie", "owuowu, owuowuowu, owu! OWU!");
    public static UserActivityDao getInstance(){
        if (instance == null) { instance = new UserActivityDao(); }
        return instance;
    }

}

