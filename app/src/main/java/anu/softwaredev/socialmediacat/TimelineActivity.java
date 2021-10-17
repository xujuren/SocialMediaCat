package anu.softwaredev.socialmediacat;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivityDao;
import java.util.List;
import Tree.RBTree;
import Tree.RBTreeNode;
import anu.softwaredev.socialmediacat.Classes.Post;

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

        GestureDetector gestureDetector;
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e){
                View childView = rvTimeline.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {
                    int position = rvTimeline.getChildLayoutPosition(childView);
                    Toast.makeText(getApplication(), "single click:" + position, Toast.LENGTH_SHORT).show();
                    return true;
                }
                return super.onSingleTapUp(e);
            }
        });

        rvTimeline.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (gestureDetector.onTouchEvent(e)) {
                    return true;
                }
                return false;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
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