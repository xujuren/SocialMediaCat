package anu.softwaredev.socialmediacat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.GestureDetector;
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
import Tree.Global_Data;
import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivityDao;
//like button library
import com.like.LikeButton;
import com.like.OnLikeListener;

public class CurrentPost extends AppCompatActivity {
    private static Post currentPost;
    private static TextView uIdTv;
    private static TextView captionTv;
    private LikeButton likeButtonHeart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_post_activity);
        Intent intent = getIntent();
        final Boolean MESSAGE = intent.getExtras().getBoolean("MESSAGE");

        String uid = intent.getStringExtra("uId");
        String tag = intent.getStringExtra("tag");
        String postId = intent.getStringExtra("postId");
        String content = intent.getStringExtra("content");
        int photoId = intent.getIntExtra("photoId",0);
        int likeCount = intent.getIntExtra("likeCount",0);
        currentPost = new Post(uid, tag, postId, content, photoId, likeCount);

        ImageView image = (ImageView) findViewById(R.id.imageView);
        Glide.with(getApplicationContext()).load("https://picsum.photos/id/" + photoId + "/400/300").apply(new RequestOptions())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(image);


        // post id show
        TextView postID = (TextView) findViewById(R.id.postIdTextView);
        postID.setText((CharSequence)currentPost.getPostId());
        postID.setTextSize(15f);
        postID.setTypeface(Typeface.DEFAULT_BOLD);

        // user id show
        uIdTv = (TextView) findViewById(R.id.userIdTextView);
        uIdTv.setText("@"+(CharSequence)currentPost.getUId());
        uIdTv.setTextSize(15f);
        uIdTv.setTypeface(Typeface.DEFAULT_BOLD);

        // like show
        TextView like = (TextView) findViewById(R.id.likeTextView);
        CharSequence likes = currentPost.getLikeCount() + " likes";
        like.setText(likes);
        like.setTextSize(15f);
        like.setTypeface(Typeface.DEFAULT);

        // tag show (if any)
        TextView tagTv = (TextView) findViewById(R.id.tagTextView);
        tagTv.setText((CharSequence)currentPost.getTag());
        tagTv.setTextSize(15f);
        tagTv.setTypeface(Typeface.DEFAULT_BOLD);

        // post content
        TextView contentv = (TextView) findViewById(R.id.contentTextView);
        contentv.setText((CharSequence)currentPost.getContent());
        contentv.setTextSize(16f);
        postID.setTypeface(Typeface.DEFAULT_BOLD);

        // caption
        captionTv = (TextView) findViewById(R.id.captionTv);
        UserActivityDao.getInstance().findUserProfileUid(uid, uIdTv, captionTv);

//        Button likeBt = (Button) findViewById(R.id.LikeButton);
//        likeBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean likeResult = Global_Data.getInstance().likePost(currentPost);
//                if (likeResult){
//                    long likec = currentPost.getLikeCount()+1;
//                    like.setText("Like: " + likec);
//                    UserActivityDao.getInstance().likePost(user.getUid(), currentPost.getPostId());
//                }
//            }
//        });

        // initializing the LikeButton objects & Heart OnLikeListener
        likeButtonHeart = (LikeButton)findViewById(R.id.likeButtonHeart);
        if(TimelineActivity.hasLiked){
            likeButtonHeart.setLiked(true);
        }
        likeButtonHeart.setOnLikeListener( new OnLikeListener(  ) {
            @Override
            public void liked( LikeButton likeButton ) {
                //sowing simple Toast when liked
//                Toast.makeText( CurrentPost.this, " Liked Heart : )", Toast.LENGTH_SHORT ).show(  );
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                boolean likeResult = Global_Data.getInstance().likePost(currentPost);
                if (likeResult){
                    currentPost.likePost();
                    CharSequence likesText = currentPost.getLikeCount() + " likes";
                    like.setText(likesText);
                    UserActivityDao.getInstance().likePost(currentPost.getPostId());
                    TimelineActivity.hasLiked = true;
                    Toast.makeText(CurrentPost.this, "Post Liked!", Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void unLiked( LikeButton likeButton ) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                boolean likeResult = Global_Data.getInstance().unlikePost(currentPost);
                if (likeResult){
                    currentPost.unlikePost();
                    CharSequence likesText = currentPost.getLikeCount() + " likes";
                    like.setText(likesText);
                    UserActivityDao.getInstance().unlikePost(currentPost.getPostId());
                    //showing simple Toast when unLiked
                    TimelineActivity.hasLiked = false;
                    Toast.makeText( CurrentPost.this, " Post unLiked  : )", Toast.LENGTH_SHORT ).show(  );}
            }
        } );

        Button backBt = (Button) findViewById(R.id.BackButton);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(CurrentPost.this,TimelineActivity.class);
                if (MESSAGE)
                    intent.putExtra("MESSAGE", true);
                else
                    intent.putExtra("MESSAGE", false);

                startActivity(intent);
            }
        });
    }


}

