package anu.softwaredev.socialmediacat;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import Tree.Global_Data;
import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.UserActivity.UserActivityDao;
//like button library
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

public class CurrentPost extends AppCompatActivity {
    private static Post currentPost;
    private static TextView uIdTv;
    private static TextView captionTv;
    private LikeButton likeButtonHeart;
    private DatabaseReference dbRef;
    private String userName;
    private String caption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_post_activity);
        Intent intent = getIntent();
        final Boolean MESSAGE = intent.getExtras().getBoolean("MESSAGE");

        String uid = intent.getStringExtra("uId");
        userProfile(uid);
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

        TextView contentv = (TextView) findViewById(R.id.contentTextView);
        contentv.setText((CharSequence)currentPost.getContent());
        contentv.setTextSize(16f);
        postID.setTypeface(Typeface.DEFAULT_BOLD);

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
                    UserActivityDao.getInstance().likePost(user.getUid(), currentPost.getPostId());
                    Toast.makeText(CurrentPost.this, "Post Liked!", Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void unLiked( LikeButton likeButton ) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                boolean likeResult = Global_Data.getInstance().unlikePost(currentPost);
                if (likeResult){
                    currentPost.dislikePost();
                    CharSequence likesText = currentPost.getLikeCount() + " likes";
                    like.setText(likesText);
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


    /**
     * Find the Profiles of the Post's Author on Firebase,
     * and display the corresponding information
     */
    public void userProfile(String userId) {
        dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("snapshot: "+snapshot.getKey() + ", value: " + snapshot.getValue());
                // Try to match Firebase Records
                if (snapshot.exists() && snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String k = ds.getKey();
                        switch (k) {
                            case "userName" :
                                userName = (String) ds.getValue();
                                if (userName!=null && userName.length()>0 && !userName.equals("null")){
                                    uIdTv.setText("@"+userName);
                                    System.out.println("uIdTv: " + userName);
                                } else {
                                    uIdTv.setText("@"+currentPost.getUId());
                                }
                                continue;
                            case "caption":
                                captionTv = (TextView) findViewById(R.id.captionTv);
                                caption = (String) ds.getValue();
                                if (caption!=null && caption.length()>0 && !caption.equals("null")){
                                    captionTv.setText(caption);
                                } else {
                                    captionTv.setText("");
                                }
                                continue;
                            default:
                                continue;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Firebase Read Fail", error.toException());
            }
        };
        dbRef.addValueEventListener(listener);

    }

}

