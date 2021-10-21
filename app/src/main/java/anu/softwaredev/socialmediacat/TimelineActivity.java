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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import Tree.Global_Data;
import anu.softwaredev.socialmediacat.Search.Parser;
import anu.softwaredev.socialmediacat.Search.Tokenizer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import Tree.RBTree;
import Tree.RBTreeNode;
import anu.softwaredev.socialmediacat.Classes.Post;

/** For the display of Posts in a timeline */
public class TimelineActivity extends AppCompatActivity {
    RBTree<String> database = new RBTree<>(); // test purpose , need to have a real tree structure to store all posts

    /**
     * main method, put all logic inside
     * @param savedInstanceState android unique class (Cloneable, Parcelable)saved state
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // 分辨是打开 my post还是 all posts
        Intent intent = getIntent();
        final Boolean MESSAGE = intent.getExtras().getBoolean("MESSAGE");
        List<Post> postList = new LinkedList<>();
        if (MESSAGE)
            postList.addAll(Global_Data.getInstance().toList());
        else
            postList.addAll(Global_Data.getInstance().searchByUser(FirebaseAuth.getInstance().getUid()));
//            postList.addAll(Global_Data.getInstance().getMyPosts());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.hide();
        }

        // Set Up timeline and data using RecyclerView
        RecyclerView rvTimeline = (RecyclerView) findViewById(R.id.rv_timeline);
        TimelineAdapter timelineAdapter = new TimelineAdapter(getApplicationContext(), postList); // show list view (timeline)
        rvTimeline.setAdapter(timelineAdapter);
        rvTimeline.setLayoutManager(new LinearLayoutManager(this));
        RBTreeNode<String> node = Global_Data.getInstance().getData().find("random");

        // TODO 添加search方法
        Button searchBt = (Button) findViewById(R.id.SearchButton);
        EditText searchEdit = (EditText) findViewById(R.id.editTextTextPersonName);
        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = searchEdit.getText().toString();
                System.out.println(search);
                List<Post> postsResult = searchAll(search);
                System.out.println();
                System.out.println("number: " + postsResult.size());
                for (Post post:postsResult) {
                    System.out.println(post);
                }
                // TODO - postsResult to display as result
                postList.clear();
                postList.addAll(postsResult);
                timelineAdapter.notifyDataSetChanged();
                if (postsResult.size() == 0)
                    Toast.makeText(getApplicationContext(), "No result", Toast.LENGTH_SHORT).show();
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
                    if (MESSAGE) {
                        Intent intent = new Intent(TimelineActivity.this, CurrentPost.class);
                        intent.putExtra("uId",ClickPost.getUId());
                        intent.putExtra("tag",ClickPost.getTag());
                        intent.putExtra("postId",ClickPost.getPostId());
                        intent.putExtra("content",ClickPost.getContent());
                        intent.putExtra("likeCount",ClickPost.getLikeCount());
                        intent.putExtra("photoId",ClickPost.getPhotoId());
                        //检查是mypost 还是 all post呼叫的currentpost
                        intent.putExtra("MESSAGE", MESSAGE);
                        startActivity(intent);
                        return true;
                    } else {
                        Intent intent = new Intent(TimelineActivity.this, MyPost.class);
                        intent.putExtra("uId",ClickPost.getUId());
                        intent.putExtra("tag",ClickPost.getTag());
                        intent.putExtra("postId",ClickPost.getPostId());
                        intent.putExtra("content",ClickPost.getContent());
                        intent.putExtra("likeCount",ClickPost.getLikeCount());
                        intent.putExtra("photoId",ClickPost.getPhotoId());
                        //检查是mypost 还是 all post呼叫的currentpost
                        intent.putExtra("MESSAGE", MESSAGE);
                        startActivity(intent);
                        return true;
                    }
                }
                return super.onSingleTapUp(e);
            }
        });

        rvTimeline.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
            // TODO - is here the method scroll up/down also considered choosing apost?
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
//    public static RBTree<String> delete(Post post, RBTree<String> database) {
//        database.delete(post.getTag(), post);
//        return database;
//    }

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

    /**
     * search by tag and id
     * @param tag
     * @param id
     * @param database
     * @return
     */
//    public static Post search(String tag, String id, RBTree<String> database) {
//        RBTreeNode<String> node = database.find(tag);
//        if (node == null)
//            return null;
//        return node.getPostsTree().findById(id).getKey();
//    }



    /**
     * integrated search , final version
     * @param search : input string
     * @return post list to show as result
     */
    public ArrayList<Post> searchAll(String search){
        ArrayList<Post> postsToShow = new ArrayList<>();
        String tagToSearch = "";
        String postIDToSearch = "";
        System.out.println(search);

        // Tokenize, Parse
        Tokenizer tokenizer = new Tokenizer(search);
        Parser parser = new Parser(tokenizer);
        if (parser.getTag()==null) {
            System.out.println("no Tag");               //show purpose
        } else {
            System.out.println(parser.getTag().show());
            tagToSearch = parser.getTag().show();
        }
        if (parser.getPostId()==null){
            System.out.println("no postId");            //show purpose
        } else {
            System.out.println(parser.getPostId().show());
            postIDToSearch = parser.getPostId().show();
        }
        if (tagToSearch.equals("") && !postIDToSearch.equals("")){
            // only postid to search
            Post result = Global_Data.instance.searchById(postIDToSearch);
            if (result != null)
                postsToShow.add(result);
        } else if (postIDToSearch.equals("") && !tagToSearch.equals("")){
            //only tag to search
            postsToShow.addAll(Global_Data.getInstance().searchByTag(tagToSearch)) ;
        } else if (tagToSearch.equals("") && postIDToSearch.equals("")){
            //empty, nothing to search
            System.out.println("nothing , Toaster throws reminder");
        } else {
            Post result = Global_Data.instance.search(tagToSearch,postIDToSearch);
            if (result != null)
                postsToShow.add(result);
//            postsToShow.add() ;
        }

        return postsToShow;

        //then we have a post list to show
    }

}