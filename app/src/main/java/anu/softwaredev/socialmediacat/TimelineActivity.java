package anu.softwaredev.socialmediacat;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Tree.Global_Data;
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivityDao;

import java.util.LinkedList;
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
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        // Set Up timeline view and data
        RecyclerView rvTimeline = (RecyclerView) findViewById(R.id.rv_timeline);                        // Timeline
        TimelineAdapter timelineAdapter = new TimelineAdapter(getApplicationContext(), Global_Data.getInstance().toList());        // Adapter to Data
        rvTimeline.setAdapter(timelineAdapter);
        rvTimeline.setLayoutManager(new LinearLayoutManager(this));                              // Linear timeline (more spaces for information)

        //TODO 添加search方法
      Button searchBt = (Button) findViewById(R.id.SearchButton);
        EditText searchEdit = (EditText) findViewById(R.id.editTextTextPersonName);
        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = searchEdit.getText().toString();
            }
        });

        GestureDetector gestureDetector;
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e){
                View childView = rvTimeline.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {
                    int position = rvTimeline.getChildLayoutPosition(childView);
                    Post ClickPost = timelineAdapter.getDataset().get(position);
                    Intent intent = new Intent(TimelineActivity.this,CurrentPost.class);
                    intent.putExtra("uId",ClickPost.getUId());
                    intent.putExtra("tag",ClickPost.getTag());
                    intent.putExtra("postId",ClickPost.getPostId());
                    intent.putExtra("content",ClickPost.getContent());
                    intent.putExtra("likeCount",ClickPost.getLikes());
                    intent.putExtra("photoId",ClickPost.getPhotoId());
                    startActivity(intent);
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
    public static Post searchById(String id, RBTree<String> database) {
        List<RBTreeNode<String>> allTags = database.findAll(database.root);
        for (RBTreeNode<String> node:allTags) {
            RBTreeNode<Post> result = node.getPostsTree().findById(id);
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
    public static List<Post> searchByTag(String tag, RBTree<String> database) {
        RBTreeNode<String> node = database.find(tag);
        if (node == null)
            return new LinkedList<Post>();
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
    public static Post search(String tag, String id, RBTree<String> database) {
        RBTreeNode<String> node = database.find(tag);
        if (node == null)
            return null;
        return node.getPostsTree().findById(id).getKey();
    }

}