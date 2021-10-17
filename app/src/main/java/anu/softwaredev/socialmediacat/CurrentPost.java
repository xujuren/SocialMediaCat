package anu.softwaredev.socialmediacat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivityDao;
import anu.softwaredev.socialmediacat.ui.main.CurrentPostFragment;

public class CurrentPost extends AppCompatActivity {
    Post currentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_post_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CurrentPostFragment.newInstance())
                    .commitNow();
        }
        Intent intent = getIntent();
        currentPost =(Post) intent.getSerializableExtra("post");
        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(currentPost.getPhotoId());
        TextView content = (TextView) findViewById(R.id.titleTextView);
        content.setText(currentPost.getContent());
        TextView like = (TextView) findViewById(R.id.ContentTextView);
        like.setText(currentPost.getLikes());

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
                UserActivityDao.getInstance().createPost(uId, tag, content, photoId, stars);
                Toast.makeText(CurrentPost.this, "Post Liked!", Toast.LENGTH_SHORT).show();
            }
        });

        Button deleteBt = (Button) findViewById(R.id.DeleteButton);
        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CurrentPost.this, "Post Deleted!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}