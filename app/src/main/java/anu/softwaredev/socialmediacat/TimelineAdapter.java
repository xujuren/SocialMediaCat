package anu.softwaredev.socialmediacat;

import static android.content.ContentValues.TAG;

import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.Classes.User;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/** Adapter for the Timeline displayed to Users */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder> {
    private final Context ctx;
    private final List<Post> dataset;
    private final int PHOTO_ID_LOWER=0;
    private final int PHOTO_ID_UPPER=100;

    private DatabaseReference dbRef;
    private String userName;
    private String interests;
    private String caption;

    public TimelineAdapter(Context ctx, List<Post> dataset) {
        this.ctx = ctx;
        this.dataset = dataset;
    }

    // Getter for the dataset
    public List<Post> getDataset() {return dataset; }

    /**
     * Implement Recycler Adapter methods     // TODO: add one blank Post at the bottom (can't show)
     * @param parent special view that can contain other views (called children.) The view group is the base class for layouts and views containers
     * @param viewType
     * @return TimelineViewHolder, add Fields and match by findViews
     */
    @NonNull
    @Override
    public TimelineAdapter.TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {                 // layoutInflater for XML post
        View view = LayoutInflater.from(ctx).inflate(R.layout.activity_timeline_post, parent, false);
        return new TimelineViewHolder(view);
    }

    /* Set up timeline */
    @Override
    public void onBindViewHolder(@NonNull TimelineAdapter.TimelineViewHolder holder, int position) {
        if (dataset != null) {
            holder.getPostUserId().setText("@"+dataset.get(position).getUId());
            holder.getPostId().setText(dataset.get(position).getPostId());
            holder.getTagContent().setText(dataset.get(position).getTag() + "\n" + dataset.get(position).getContent().replace("\"", ""));
            holder.getTag().setText(dataset.get(position).getLikeCount() + " likes");
            long photoId = dataset.get(position).getPhotoId();
            // Load Image
            if (photoId>=PHOTO_ID_LOWER && photoId<=PHOTO_ID_UPPER){
                Glide.with(ctx).load("https://picsum.photos/id/" + photoId + "/300/200").apply(new RequestOptions())
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .skipMemoryCache(true)
                        .into(holder.getPostImage());
            }

        }

    }

    @Override
    public int getItemCount() {
        return dataset.size();                             // no. of items we hv
    }


    /** Create TimelineViewHolder, add Fields and match by findViews */
    public class TimelineViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivPostImage;
        private final TextView tvPostUsername;
        private final TextView tvPostContent;
        private final TextView tvTagsAndPostId;
        private final TextView tvLikes;

        public TimelineViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ivPostImage = (ImageView) itemView.findViewById(R.id.iv_post_image);
            this.tvPostUsername = (TextView) itemView.findViewById(R.id.tv_userName);
            this.tvPostContent = (TextView) itemView.findViewById(R.id.tv_content);
            this.tvTagsAndPostId = (TextView) itemView.findViewById(R.id.tv_TagPostId);   // New
            this.tvLikes = (TextView) itemView.findViewById(R.id.tv_likes);
        }

        // Getter methods for the views
        public ImageView getPostImage() {return ivPostImage;}
        public TextView getPostUserId() {return tvPostUsername;}
        public TextView getTagContent() {return tvPostContent;}
        public TextView getPostId() {return tvTagsAndPostId;}
        public TextView getTag() {return tvLikes;}

    }



}
