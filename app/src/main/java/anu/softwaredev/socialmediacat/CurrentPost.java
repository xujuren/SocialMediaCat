package anu.softwaredev.socialmediacat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import anu.softwaredev.socialmediacat.Classes.Post;
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
    }
}