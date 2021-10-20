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
    Post currentPost;
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
        TextView uId = (TextView) findViewById(R.id.userIdTextView);
        uId.setText("@"+(CharSequence)currentPost.getUId());
        uId.setTextSize(15f);
        uId.setTypeface(Typeface.DEFAULT_BOLD);

        TextView like = (TextView) findViewById(R.id.likeTextView);
        CharSequence likes = currentPost.getLikeCount() + "likes";
        like.setText(likes);
        like.setTextSize(15f);
        like.setTypeface(Typeface.DEFAULT_BOLD);

        // tag show (if any)
        TextView tagTv = (TextView) findViewById(R.id.tagTextView);
        tagTv.setText((CharSequence)currentPost.getTag());
        tagTv.setTextSize(20f);
        tagTv.setTypeface(Typeface.DEFAULT_BOLD);

        TextView contentv = (TextView) findViewById(R.id.contentTextView);
        contentv.setText((CharSequence)currentPost.getContent());
        contentv.setTextSize(20f);
        postID.setTypeface(Typeface.DEFAULT);


        // Now get a handle to any View contained
        // within the main layout you are using
        View someView = findViewById(R.id.randomViewInMainLayout);

        // Find the root view
        View root = someView.getRootView();

        // Set the color
        root.setBackgroundColor(getResources().getColor(android.R.color.red));






//        Button likeBt = (Button) findViewById(R.id.LikeButton);
//        likeBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
////                String uId = user.getUid();
////                String tag = currentPost.getTag();
////                String content = currentPost.getContent();
////                int photoId = currentPost.getPhotoId();
////                int stars = currentPost.getLikes()+1;
//
//
//                boolean likeResult = Global_Data.getInstance().likePost(currentPost);
//                if (likeResult){
//                    long likec = currentPost.getLikeCount()+1;
//                    CharSequence dolike = "Like: " + likec;
//                    like.setText(dolike);
//                    UserActivityDao.getInstance().likePost(user.getUid(), currentPost.getPostId());
//                    Toast.makeText(CurrentPost.this, "Post Liked!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        likeButtonHeart = (LikeButton)findViewById(R.id.likeButtonHeart); //like button
        //initializing the LikeButton objects
        likeButtonHeart = (LikeButton)findViewById(R.id.likeButtonHeart);
        //like Button Heart OnLikeListener
        likeButtonHeart.setOnLikeListener( new OnLikeListener(  ) {
            @Override
            public void liked( LikeButton likeButton ) {
                //sowing simple Toast when liked
//                Toast.makeText( CurrentPost.this, " Liked Heart : )", Toast.LENGTH_SHORT ).show(  );
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                String uId = user.getUid();
//                String tag = currentPost.getTag();
//                String content = currentPost.getContent();
//                int photoId = currentPost.getPhotoId();
//                int stars = currentPost.getLikes()+1;
                boolean likeResult = Global_Data.getInstance().likePost(currentPost);
                if (likeResult){
                    long likec = currentPost.getLikeCount()+1;
                    CharSequence dolike = "Total Like: " + likec;
                    like.setText(dolike);
                    UserActivityDao.getInstance().likePost(user.getUid(), currentPost.getPostId());
                    Toast.makeText(CurrentPost.this, "Post Liked!", Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void unLiked( LikeButton likeButton ) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                boolean likeResult = Global_Data.getInstance().likePost(currentPost);
                if (likeResult){
                    CharSequence likes = currentPost.getLikeCount() + "likes";
                    like.setText(likes);
                    UserActivityDao.getInstance().dislikePost(user.getUid(), currentPost.getPostId());
                    //showing simple Toast when unLiked
                    Toast.makeText( CurrentPost.this, " Post UnLiked  : )", Toast.LENGTH_SHORT ).show(  );}
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