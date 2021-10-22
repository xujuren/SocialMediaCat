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
    private static Post curPost;
    private static TextView uIdTv2;
    private static TextView captionTv2;
    private LikeButton likeButtonHeart2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);
        Intent intent = getIntent();
        final Boolean MESSAGE = intent.getExtras().getBoolean("MESSAGE");

        Post curPost = (Post) getIntent().getExtras().getSerializable("POST");
//        String uid = intent.getStringExtra("uId");
//        String tag = intent.getStringExtra("tag");
//        String postId = intent.getStringExtra("postId");
//        String content = intent.getStringExtra("content");
//        int photoId = intent.getIntExtra("photoId",0);
//        int likeCount = intent.getIntExtra("likeCount",0);
//        curPost = new Post(uid, tag, postId, content, photoId, likeCount);

        ImageView image = (ImageView) findViewById(R.id.imageView2);
        Glide.with(getApplicationContext()).load("https://picsum.photos/id/" + curPost.getPhotoId() + "/400/300").apply(new RequestOptions())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(image);


        // post id show
        TextView postID = (TextView) findViewById(R.id.postIdTextView2);
        postID.setText("Post ID" + (CharSequence) curPost.getPostId());
        postID.setTextSize(15f);
        postID.setTypeface(Typeface.DEFAULT_BOLD);

        // user id show
        uIdTv2 = (TextView) findViewById(R.id.userIdTextView2);
        uIdTv2.setText("User ID: " + (CharSequence) curPost.getUId());
        uIdTv2.setTextSize(15f);
        uIdTv2.setTypeface(Typeface.DEFAULT_BOLD);

        TextView like = (TextView) findViewById(R.id.likeTextView2);
        CharSequence likes = curPost.getLikeCount() + " likes";
        like.setText(likes);
        like.setTextSize(15f);
        like.setTypeface(Typeface.DEFAULT);

        // tag show (if any)
        TextView tagTv = (TextView) findViewById(R.id.tagTextView2);
        tagTv.setText("#" + (CharSequence) curPost.getTag());
        tagTv.setTextSize(15f);
        tagTv.setTypeface(Typeface.DEFAULT_BOLD);

        // post content
        TextView contentv = (TextView) findViewById(R.id.contentTextView2);
        contentv.setText((CharSequence) curPost.getContent());
        contentv.setTextSize(16f);
        postID.setTypeface(Typeface.DEFAULT_BOLD);

        // caption
        captionTv2 = (TextView) findViewById(R.id.captionTv2);
        UserActivityDao.getInstance().findUserProfileUid(curPost.getUId(), uIdTv2, captionTv2);


        // initializing the LikeButton objects & Heart OnLikeListener
        likeButtonHeart2 = (LikeButton)findViewById(R.id.likeButtonHeart2);
        if(curPost.getLikedBy().contains(FirebaseAuth.getInstance().getUid())){
            likeButtonHeart2.setLiked(true);
        }
        likeButtonHeart2.setOnLikeListener( new OnLikeListener(  ) {
            @Override
            public void liked( LikeButton likeButton ) {
                //sowing simple Toast when liked
//                Toast.makeText( CurrentPost.this, " Liked Heart : )", Toast.LENGTH_SHORT ).show(  );
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                boolean likeResult = Global_Data.getInstance().likePost(curPost, FirebaseAuth.getInstance().getUid());
                if (likeResult){
                    curPost.likePost(FirebaseAuth.getInstance().getUid());
                    CharSequence likesText = curPost.getLikeCount() + " likes";
                    like.setText(likesText);
//                    UserActivityDao.getInstance().likePost(currentPost2.getPostId());
                    UserActivityDao.getInstance().likePost(curPost, FirebaseAuth.getInstance().getUid());
                    TimelineActivity.hasLiked = true;
                    Toast.makeText(MyPost.this, "Post Liked!", Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void unLiked( LikeButton likeButton ) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                boolean likeResult = Global_Data.getInstance().unlikePost(curPost, FirebaseAuth.getInstance().getUid());
                if (likeResult){
                    curPost.unlikePost(FirebaseAuth.getInstance().getUid());
                    CharSequence likesText = curPost.getLikeCount() + " likes";
                    like.setText(likesText);
//                    UserActivityDao.getInstance().unlikePost(currentPost2.getPostId());
                    UserActivityDao.getInstance().unlikePost(curPost, FirebaseAuth.getInstance().getUid());

                    //showing simple Toast when unLiked
                    TimelineActivity.hasLiked = false;
                    Toast.makeText( MyPost.this, " Post unLiked  : )", Toast.LENGTH_SHORT ).show(  );}
            }
        } );

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

        Button deleteBt = (Button) findViewById(R.id.DeleteButton2);
        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth user = FirebaseAuth.getInstance();
//                Global_Data.getInstance().delete(currentPost);
//                UserActivityDao.getInstance().deletePost(currentPost.getPostId());
//                Toast.makeText(CurrentPost.this, "Post Deleted!", Toast.LENGTH_SHORT).show();

//                finish();
                if (user.getUid().equals(curPost.getUId())) {
                    Global_Data.getInstance().delete(curPost);
//                    Global_Data.getInstance().remove_My_Post(currentPost2);
                    Toast.makeText(MyPost.this, "Post Deleted!", Toast.LENGTH_SHORT).show();
//                    finish();
                }else {
                    Toast.makeText(MyPost.this, "You do not have permission to delete this", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}