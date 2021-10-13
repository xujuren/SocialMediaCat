package anu.softwaredev.socialmediacat.dao.decorator;
import android.os.Build;
import androidx.annotation.RequiresApi;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.actionReport.ActionCount;
import anu.softwaredev.socialmediacat.actionReport.CsvActionReport;

public class UserActivityDao implements IUserActivityDao {

    // [Singleton]
    private static UserActivityDao instance;
    private static Integer idCount=0;               // TODO TBC
    private static File file;                       // *csv file to store
    static {
        try {
            file = File.createTempFile("user-action", ".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private UserActivityDao() {};


    @Override
    public UserActivity createPost(String uId, String category, String postId, String content) {           // alt: only content (userName: below)
        try {
            if (postId == ""){      // postId TODO - how to set (with data)
                idCount++;
                postId = idCount.toString();
            }

            String action = "create-post";
            String text = action + ";" + uId + ";" + category + ";" + postId + ";" + content + "\n";            //TODO > userName
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
            }

            System.out.println("Post saved in " + file.getAbsolutePath());
            return new UserActivity(action, uId, category, postId, content);

        } catch (IOException e) { e.printStackTrace(); }

        return null;
    }


    @Override
    public UserActivity likePost(String username, Integer idPost) {
//        try{
//            String action = "like-post";
//            String content = "+1";
//            String text = action + ";" + username + ";" + content + ";" + idPost + "\n";        // TODO changed tags
//            Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
//            System.out.println("Like saved in " + file.getAbsolutePath());
//            UserActivity userActivity = new UserActivity(username, action, content, idPost);
//            return userActivity;
//        } catch (IOException e){
//            e.printStackTrace();
//        }
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
                    if (strings != null && strings.length == 5) {
                        String action = strings[0];
                        String uId = strings[1];
                        String category = strings[2];
                        String postId = strings[3];
                        String content = strings[4];
                        if ("create-post".equals(action)) {
                            UserActivity userAct = new UserActivity(action, uId, category, postId, content);
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

    public static UserActivityDao getInstance(){
        // e.g. [UserActivityDao.getInstance()].createPost("@woofie", "owuowu, owuowuowu, owu! OWU!");
        if (instance == null) { instance = new UserActivityDao(); }
        return instance;
    }


//    /** reports */
//    public List<ActionCount> generateActionCountReport() {
//        CsvActionReport csvActionReport = new CsvActionReport();
//        UserActivityDao userActivityDao = UserActivityDao.getInstance();
//        String path = "posts.csv";
//        // String path = userActivityDao.getFilePath();
//        List<ActionCount> actionCountList = csvActionReport.generateReport(path);
//        return actionCountList;
//    }


}

