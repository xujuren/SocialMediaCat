package anu.softwaredev.socialmediacat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.like.LikeButton;
import com.like.OnLikeListener;

import Tree.Global_Data;
import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivityDao;

public class MyPost extends AppCompatActivity {
    Post currentPost;
    private LikeButton likeButtonHeart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);
        Intent intent = getIntent();
        final Boolean MESSAGE = intent.getExtras().getBoolean("MESSAGE");


        String uid = intent.getStringExtra("uId");
        String tag = intent.getStringExtra("tag");
        String postId = intent.getStringExtra("postId");
        String content = intent.getStringExtra("content");
        int photoId = intent.getIntExtra("photoId",0);
        int likeCount = intent.getIntExtra("likeCount",0);
        currentPost = new Post(uid, tag, postId, content, photoId, likeCount);
        CharSequence likes = "Like: " + currentPost.getLikeCount();
        likeButtonHeart = (LikeButton)findViewById(R.id.likeButtonHeart2); //like button
        //initializing the LikeButton objects
        likeButtonHeart = (LikeButton)findViewById(R.id.likeButtonHeart2);
        //like Button Heart OnLikeListener
        likeButtonHeart.setOnLikeListener( new OnLikeListener(  ) {
            @Override
            public void liked( LikeButton likeButton ) {
                //sowing simple Toast when liked
                Toast.makeText( MyPost.this, " Liked Heart : )", Toast.LENGTH_SHORT ).show(  );
            }

            @Override
            public void unLiked( LikeButton likeButton ) {
                //sowing simple Toast when unLiked
                Toast.makeText( MyPost.this, " UnLiked Heart : )", Toast.LENGTH_SHORT ).show(  );

            }
        } );

        ImageView image = (ImageView) findViewById(R.id.imageView2);
        Glide.with(getApplicationContext()).load("https://picsum.photos/id/" + photoId + "/300/200").apply(new RequestOptions())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(image);

        // TODO - hi kyle, (if you will change the layout) can you also + postId here?
        TextView contentv = (TextView) findViewById(R.id.titleTextView2);
        contentv.setText((CharSequence)currentPost.getContent());
        contentv.setTextSize(32f);
        contentv.setTypeface(Typeface.DEFAULT_BOLD);
        TextView like = (TextView) findViewById(R.id.ContentTextView2);
        like.setText(likes);
        like.setTextSize(32f);
        like.setTypeface(Typeface.DEFAULT_BOLD);
        TextView postID = (TextView) findViewById(R.id.PostIdtextView2);
        postID.setText((CharSequence)currentPost.getPostId());
        postID.setTextSize(16f);
        postID.setTypeface(Typeface.DEFAULT_BOLD);

        Button likeBt = (Button) findViewById(R.id.LikeButton2);
        likeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                String uId = user.getUid();
//                String tag = currentPost.getTag();
//                String content = currentPost.getContent();
//                int photoId = currentPost.getPhotoId();
//                int stars = currentPost.getLikes()+1;

                //TODO 需要给creat函数添加like字段
//                UserActivityDao.getInstance().createPost(uId, tag, content, photoId, stars);

                boolean likeResult = Global_Data.getInstance().likePost(currentPost);
                if (likeResult){
                    int likec = currentPost.getLikeCount()+1;
                    CharSequence dolike = "Like: " + likec;
                    like.setText(dolike);
                    UserActivityDao.getInstance().likePost(currentPost.getPostId());
                    Toast.makeText(MyPost.this, "Post Liked!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button deleteBt = (Button) findViewById(R.id.DeleteButton2);
        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth user = FirebaseAuth.getInstance();
//                Global_Data.getInstance().delete(currentPost);
//                UserActivityDao.getInstance().deletePost(currentPost.getPostId());
//                Toast.makeText(CurrentPost.this, "Post Deleted!", Toast.LENGTH_SHORT).show();

//                finish();
                if (user.getUid().equals(currentPost.getUId())) {
                    Global_Data.getInstance().delete(currentPost);
                    Global_Data.getInstance().remove_My_Post(currentPost);
                    Toast.makeText(MyPost.this, "Post Deleted!", Toast.LENGTH_SHORT).show();
//                    finish();
                }else {
                    Toast.makeText(MyPost.this, "You do not have permission to delete this", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button backBt = (Button) findViewById(R.id.BackButton2);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MyPost.this,TimelineActivity.class);
                if (MESSAGE)
                    intent.putExtra("MESSAGE", true);
                else
                    intent.putExtra("MESSAGE", false);
                startActivity(intent);
            }
        });
    }
}