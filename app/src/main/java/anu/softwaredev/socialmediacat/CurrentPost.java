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
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivity;
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
        CharSequence likes = "Like: " + currentPost.getLikeCount();
        likeButtonHeart = (LikeButton)findViewById(R.id.likeButtonHeart); //like button
        //initializing the LikeButton objects
        likeButtonHeart = (LikeButton)findViewById(R.id.likeButtonHeart);
        //like Button Heart OnLikeListener
        likeButtonHeart.setOnLikeListener( new OnLikeListener(  ) {
            @Override
            public void liked( LikeButton likeButton ) {
                //sowing simple Toast when liked
                Toast.makeText( CurrentPost.this, " Liked Heart : )", Toast.LENGTH_SHORT ).show(  );
            }

            @Override
            public void unLiked( LikeButton likeButton ) {
                //sowing simple Toast when unLiked
                Toast.makeText( CurrentPost.this, " UnLiked Heart : )", Toast.LENGTH_SHORT ).show(  );

            }
        } );

        ImageView image = (ImageView) findViewById(R.id.imageView);
        Glide.with(getApplicationContext()).load("https://picsum.photos/id/" + photoId + "/300/200").apply(new RequestOptions())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(image);

        // TODO - hi kyle, (if you will change the layout) can you also + postId here?
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
//                String uId = user.getUid();
//                String tag = currentPost.getTag();
//                String content = currentPost.getContent();
//                int photoId = currentPost.getPhotoId();
//                int stars = currentPost.getLikes()+1;


                boolean likeResult = Global_Data.getInstance().likePost(currentPost);
                if (likeResult){
                    long likec = currentPost.getLikeCount()+1;
                    CharSequence dolike = "Like: " + likec;
                    like.setText(dolike);
                    UserActivityDao.getInstance().likePost(user.getUid(), currentPost.getPostId());
                    Toast.makeText(CurrentPost.this, "Post Liked!", Toast.LENGTH_SHORT).show();
                }
            }
        });



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