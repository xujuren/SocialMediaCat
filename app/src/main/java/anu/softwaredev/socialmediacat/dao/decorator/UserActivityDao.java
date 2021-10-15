package anu.softwaredev.socialmediacat.dao.decorator;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.CreatePost;
import anu.softwaredev.socialmediacat.actionReport.ActionCount;
import anu.softwaredev.socialmediacat.actionReport.CsvActionReport;

/** DAO for UserActivity */
public class UserActivityDao implements IUserActivityDao {
    private FirebaseUser user;
    private DatabaseReference dbRef;

    private static UserActivityDao instance;        /** defined as Singleton */
    private static Integer idCount=0;               // TODO TBC
    private static File file;                       // temporary fire
    static {
        try {
            file = File.createTempFile("user-action", ".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private UserActivityDao() {};


    @Override           /** TODO (userName, Content) ... */
    public UserActivity createPost(String uId, String category, String postId, String content) {           // alt: only content (userName: below)
        try {
            boolean fromLocal = (postId=="" || postId.startsWith("l"));
            // postId ("P-1": dummy, "" for loaded "createPost") TODO - how set (with data)
            if (postId==null || postId=="P-1" || postId==""){
                idCount++;
                postId = "temp"+idCount.toString();
            }

            String action = "create-post";
            String text = action + ";" + uId + ";" + category + ";" + postId + ";" + content + "\n";            //TODO > userName
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
            }

            System.out.println("Post saved in " + file.getAbsolutePath());


            /** TODO - Testing */
            UserActivity userAct = new UserActivity(action, uId, category, postId, content);
            if (!fromLocal) {
                // Add to DB
                Post newPost = userAct.getPost();
                dbRef = FirebaseDatabase.getInstance().getReference();            // path to DB
                postId = dbRef.child("Posts").push().getKey();                  // unique Key for Posts
                Map<String, Object> postValues = newPost.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/Posts/" + postId, postValues);
                // childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

                dbRef.updateChildren(childUpdates);
            }

            return new UserActivity(action, uId, category, postId, content);

        } catch (IOException e) { e.printStackTrace(); }

        return null;
    }



    // TODO [@Kyle] see here for likePost
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
//        String path = "userActions.csv";
//        // String path = userActivityDao.getFilePath();
//        List<ActionCount> actionCountList = csvActionReport.generateReport(path);
//        return actionCountList;
//    }


}

