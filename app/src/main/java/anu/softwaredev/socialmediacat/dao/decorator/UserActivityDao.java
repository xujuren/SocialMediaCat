package anu.softwaredev.socialmediacat.dao.decorator;
import android.os.Build;
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

/** DAO for UserActivity */
public class UserActivityDao implements IUserActivityDao {
    private DatabaseReference dbRef;
//    private static Integer idCount=0;               // TODO TBC

    private static UserActivityDao instance;        /** defined as Singleton */
    private static File file;                       // temporary fire
    static {
        try {
            file = File.createTempFile("user-action", ".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Singleton
    private UserActivityDao() {};
    public static UserActivityDao getInstance(){
        if (instance == null) { instance = new UserActivityDao(); }
        return instance;
    }


    @Override           /** TODO (userName, Content) ... */
    public void createPost(String uId, String tags, String content, int photoId) {           // alt: only content (userName: below)
        try {
            // check ID for photo (*for local data instances)
            if (photoId==-1 || photoId<20 || photoId>100) {
                photoId = (int) (Math.random() *((100-20)+1) + 20); 	// gen rand id (use for URL below), max=100 min=(20)
            }

            // Update DB (new)
            dbRef = FirebaseDatabase.getInstance().getReference();            // path to DB
            String postId = dbRef.child("Posts").push().getKey();             // unique Key for Posts
            Post newPost = new Post(uId, tags, postId, content, photoId);
            Map<String, Object> postValues = newPost.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/Posts/" + postId, postValues);
            // childUpdates.put("/user-posts/" + userId + "/" + key, postValues);
            dbRef.updateChildren(childUpdates);

            // write to file (action, uId, tags, postId, content, photoId);
            String text = "create-post" + ";" + uId + ";" + tags + ";" + postId + ";" + content + ";" + photoId + ";" + "0" + "\n";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
            }
            System.out.println("Post saved in " + file.getAbsolutePath());

        } catch (IOException e) { e.printStackTrace(); }

    }

    @Override
    public void likePost(Integer idPost) {

    }


    /** TODO (n, Content) ... */
    public void loadPost(String uId, String tags, String postId, String content, int photoId, int likeCounts) {           // alt: only content (userName: below)
        try {
            // write to file
            String text = "store-post" + ";" + uId + ";" + tags + ";" + postId + ";" + content + ";" + photoId + ";" + likeCounts + "\n";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
            }
            System.out.println("Post saved in " + file.getAbsolutePath());

        } catch (IOException e) { e.printStackTrace(); }

    }


    // TODO [@Kyle] see here for likePost
    @Override
    public UserActivity likePost(String username, Integer idPost) {

        return null;
    }


    // @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<Post> findAllPosts() {
        List<Post> postsLoaded = new ArrayList<>();
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            String fileContent = new String(bytes);
            String[] lines = fileContent.split("\n");

            if (lines != null) {
                for (String line : lines) {
                    String[] items = line.split(";");
                    if (items!=null && items.length==7 && ("create-post".equals(items[0]) || "store-post".equals(items[0]))) {
                        Post post = new Post(items[1], items[2], items[3], items[4], Integer.parseInt(items[5]), Integer.parseInt(items[6]));
                        postsLoaded.add(post);
                    }
                }
            }

        } catch (IOException e) { e.printStackTrace(); }
        return postsLoaded;
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




}

