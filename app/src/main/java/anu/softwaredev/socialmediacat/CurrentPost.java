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
import Tree.Global_Data;
import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivityDao;
//like button library
import com.like.LikeButton;
import com.like.OnLikeListener;

public class CurrentPost extends AppCompatActivity {
    private static Post curPost;
    private static TextView uIdTv;
    private static TextView captionTv;
    private LikeButton likeButtonHeart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_post_activity);
        Intent intent = getIntent();
        final Boolean MESSAGE = intent.getExtras().getBoolean("MESSAGE");

        Post curPost = (Post) getIntent().getExtras().getSerializable("POST");
//        curPost.addLikedBy(FirebaseAuth.getInstance().getUid());
//
//        String uid = intent.getStringExtra("uId");
//        String tag = intent.getStringExtra("tag");
//        String postId = intent.getStringExtra("postId");
//        String content = intent.getStringExtra("content");
//        int photoId = intent.getIntExtra("photoId",0);
//        int likeCount = intent.getIntExtra("likeCount",0);
//        curPost = new Post(uid, tag, postId, content, photoId, likeCount);
//        String postId = curPost.getPostId();
//        String Uid = curPost.getUId();


        ImageView image = (ImageView) findViewById(R.id.imageView);
        Glide.with(getApplicationContext()).load("https://picsum.photos/id/" + curPost.getPhotoId() + "/400/300").apply(new RequestOptions())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(image);


        // post id show
        TextView postID = (TextView) findViewById(R.id.postIdTextView);
//        postID.setText((CharSequence)currentPost.getPostId());
        postID.setText((CharSequence)curPost.getPostId());
        postID.setTextSize(15f);
        postID.setTypeface(Typeface.DEFAULT_BOLD);

        // user id show
        uIdTv = (TextView) findViewById(R.id.userIdTextView);
//        uIdTv.setText("@"+(CharSequence)currentPost.getUId());
        uIdTv.setText("@"+(CharSequence)curPost.getUId());
        uIdTv.setTextSize(15f);
        uIdTv.setTypeface(Typeface.DEFAULT_BOLD);

        // like show
        TextView like = (TextView) findViewById(R.id.likeTextView);
//        CharSequence likes = currentPost.getLikeCount() + " likes";
        CharSequence likes = curPost.getLikeCount() + " likes";
        like.setText(likes);
        like.setTextSize(15f);
        like.setTypeface(Typeface.DEFAULT);

        // tag show (if any)
        TextView tagTv = (TextView) findViewById(R.id.tagTextView);
//        tagTv.setText((CharSequence)currentPost.getTag());
        tagTv.setText((CharSequence)curPost.getTag());
        tagTv.setTextSize(15f);
        tagTv.setTypeface(Typeface.DEFAULT_BOLD);

        // post content
        TextView contentv = (TextView) findViewById(R.id.contentTextView);
//        contentv.setText((CharSequence)currentPost.getContent());
        contentv.setText((CharSequence)curPost.getContent());
        contentv.setTextSize(16f);
        postID.setTypeface(Typeface.DEFAULT_BOLD);

        // caption
        captionTv = (TextView) findViewById(R.id.captionTv);
        UserActivityDao.getInstance().findUserProfileUid(curPost.getUId(), uIdTv, captionTv);

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

        if(curPost.getLikedBy().contains(FirebaseAuth.getInstance().getUid())){
            likeButtonHeart.setLiked(true);
        }
        likeButtonHeart.setOnLikeListener( new OnLikeListener(  ) {
            @Override
            public void liked( LikeButton likeButton ) {
                //sowing simple Toast when liked
//                Toast.makeText( CurrentPost.this, " Liked Heart : )", Toast.LENGTH_SHORT ).show(  );
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                boolean likeResult = Global_Data.getInstance().likePost(curPost, FirebaseAuth.getInstance().getUid());
                if (likeResult){
//                    currentPost.likePost(FirebaseAuth.getInstance().getUid());
//                    CharSequence likesText = currentPost.getLikeCount() + " likes";
                    curPost.likePost(FirebaseAuth.getInstance().getUid());
                    CharSequence likesText = curPost.getLikeCount() + " likes";
                    like.setText(likesText);
//                    UserActivityDao.getInstance().likePost(currentPost.getPostId());
                    UserActivityDao.getInstance().likePost(curPost, FirebaseAuth.getInstance().getUid());
                    TimelineActivity.hasLiked = true;
                    Toast.makeText(CurrentPost.this, "Post Liked!", Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void unLiked( LikeButton likeButton ) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                boolean likeResult = Global_Data.getInstance().unlikePost(curPost, FirebaseAuth.getInstance().getUid());
//                boolean likeResult = curPost.getLikedBy().contains(FirebaseAuth.getInstance().getUid());
                System.out.println(user.getUid() + " " + curPost.getLikedBy().contains(user.getUid()));
                if (likeResult){
//                    currentPost.unlikePost(FirebaseAuth.getInstance().getUid());
//                    CharSequence likesText = currentPost.getLikeCount() + " likes";
                    curPost.unlikePost(FirebaseAuth.getInstance().getUid());
                    CharSequence likesText = curPost.getLikeCount() + " likes";

                    like.setText(likesText);
//                    UserActivityDao.getInstance().unlikePost(currentPost.getPostId());
                    UserActivityDao.getInstance().unlikePost(curPost, FirebaseAuth.getInstance().getUid());
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

