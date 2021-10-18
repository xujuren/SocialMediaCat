package anu.softwaredev.socialmediacat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import Tree.Global_Data;
import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivityDao;
import anu.softwaredev.socialmediacat.ui.main.CurrentPostFragment;

public class CurrentPost extends AppCompatActivity {
    Post currentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_post_activity);
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uId");
        String tag = intent.getStringExtra("tag");
        String postId = intent.getStringExtra("postId");
        String content = intent.getStringExtra("content");
        int photoId = intent.getIntExtra("photoId",0);
        int likeCount = intent.getIntExtra("likeCount",0);
        currentPost = new Post(uid,tag,postId,content,photoId,likeCount);



        CharSequence likes = "Like: " + currentPost.getLikes();

        ImageView image = (ImageView) findViewById(R.id.imageView);
        Glide.with(getApplicationContext()).load("https://picsum.photos/id/" + photoId + "/300/200").apply(new RequestOptions())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(image);
        TextView contentv = (TextView) findViewById(R.id.titleTextView);
        contentv.setText((CharSequence)currentPost.getContent());
        contentv.setTextSize(32f);
        contentv.setTypeface(Typeface.DEFAULT_BOLD);
        TextView like = (TextView) findViewById(R.id.ContentTextView);
        like.setText(likes);
        like.setTextSize(32f);
        like.setTypeface(Typeface.DEFAULT_BOLD);

        Button likeBt = (Button) findViewById(R.id.LikeButton);
        likeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uId = user.getUid();
                String tag = currentPost.getTag();
                String content = currentPost.getContent();
                int photoId = currentPost.getPhotoId();
                int stars = currentPost.getLikes()+1;
                //TODO 需要给creat函数添加like字段
//                UserActivityDao.getInstance().createPost(uId, tag, content, photoId, stars);
                Toast.makeText(CurrentPost.this, "Post Liked!", Toast.LENGTH_SHORT).show();
            }
        });

        Button deleteBt = (Button) findViewById(R.id.DeleteButton);
        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth user = FirebaseAuth.getInstance();
                Global_Data.getInstance().delete(currentPost);
                Toast.makeText(CurrentPost.this, "Post Deleted!", Toast.LENGTH_SHORT).show();
                intent.setClass(CurrentPost.this,TimelineActivity.class);
                startActivity(intent);
//                finish();
//                if (user.getUid().equals(currentPost.getUId())) {
//                    Global_Data.getInstance().delete(currentPost);
//                    Toast.makeText(CurrentPost.this, "Post Deleted!", Toast.LENGTH_SHORT).show();
//                    finish();
//                }else {
//                    Toast.makeText(CurrentPost.this, "You do not have permission to delete this", Toast.LENGTH_SHORT).show();
//                }

            }
        });


    }
}