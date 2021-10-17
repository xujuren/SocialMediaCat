package anu.softwaredev.socialmediacat;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;

import java.util.List;

import Tree.RBTree;
import Tree.RBTreeNode;
import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.Util.AssetHandler;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivityDao;

/** For the display of Posts in a timeline */
public class TimelineActivity extends AppCompatActivity {

    /**
     * main method, put all logic inside
     * @param savedInstanceState android unique class (Cloneable, Parcelable)saved state
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Set Up timeline view and data
        RecyclerView rvTimeline = (RecyclerView) findViewById(R.id.rv_timeline);                        // Timeline
        TimelineAdapter timelineAdapter = new TimelineAdapter(getApplicationContext(), UserActivityDao.getInstance().findAllPosts());        // Adapter to Data
        rvTimeline.setAdapter(timelineAdapter);
        rvTimeline.setLayoutManager(new LinearLayoutManager(this));                              // Linear timeline (more spaces for information)
    }


    /**
     * Function to delete a post
     * @param post
     * @param database
     * @return
     */
    public static RBTree<String> delete(Post post, RBTree<String> database) {
        database.delete(post.getTag(), post);
        return database;
    }

    /**
     * Search by post id
     * @param id
     * @param database
     * @return
     */
    public static Post search(int id, RBTree<String> database) {
        List<RBTreeNode<String>> allTags = database.findAll(database.root);
        for (RBTreeNode<String> node:allTags) {
            RBTreeNode<Post> result = node.getPostsTree().find(id);
            if (result != null)
                return result.getKey();
        }
        return null;
    }

    //Search by tag

    /**
     * Search by tag
     * @param tag
     * @param database
     * @return
     */
    public static List<Post> search(String tag, RBTree<String> database) {
        RBTreeNode<String> node = database.find(tag);
        return node.getPostsTree().treeToListInorder(node.getPostsTree().root);
    }

    //Search by tag and id

    /**
     * search by tag and id
     * @param tag
     * @param id
     * @param database
     * @return
     */
    public static Post search(String tag, int id, RBTree<String> database) {
        RBTreeNode<String> node = database.find(tag);
        return node.getPostsTree().find(id).getKey();
    }

}