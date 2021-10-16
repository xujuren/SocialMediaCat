package anu.softwaredev.socialmediacat;
import anu.softwaredev.socialmediacat.Classes.Post;
import anu.softwaredev.socialmediacat.dao.decorator.UserActivity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/** Adapter for the Timeline displayed to Users */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder> {
    private final Context ctx;
    private final List<Post> dataset;

    public TimelineAdapter(Context ctx, List<Post> dataset) {
        this.ctx = ctx;
        this.dataset = dataset;
    }

    // Implement Recycler Adapter methods     // TODO: add one blank Post at the bottom (can't show)
    @NonNull
    @Override
    public TimelineAdapter.TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {                 // layoutInflater for XML post
        View view = LayoutInflater.from(ctx).inflate(R.layout.activity_timeline_post, parent, false);
        return new TimelineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineAdapter.TimelineViewHolder holder, int position) {
        if (dataset != null) {
            String uId = dataset.get(position).getUId();
            //TODO n

            // setup for holder
            holder.getPostUsername().setText("@"+uId);
            holder.getPostContent().setText(dataset.get(position).getContent());
            holder.getCategoryPostId().setText(dataset.get(position).getTags()+" /"+dataset.get(position).getPostId());
            holder.getLikes().setText(String.valueOf(dataset.get(position).getLikes()) + " likes");
            // TODO: Like Button

            // Load Image
            int photoId = dataset.get(position).getPhotoId();
            Glide.with(ctx).load("https://picsum.photos/id/" + photoId + "/300/200").apply(new RequestOptions())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)       	// ≈ image loaded, dun want it cached
                    .skipMemoryCache(true)
                    .into(holder.getPostImage());
        }

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }                              // no. of items we hv


    /** Create TimelineViewHolder, add Fields and match by findViews */
    public class TimelineViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivPostImage;
        private final TextView tvPostUsername;
        private final TextView tvPostContent;
        private final TextView tvTagsAndPostId;    // NEW
        private final TextView tvLikes;

        public TimelineViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ivPostImage = (ImageView) itemView.findViewById(R.id.iv_post_image);
            this.tvPostUsername = (TextView) itemView.findViewById(R.id.tv_userName);
            this.tvPostContent = (TextView) itemView.findViewById(R.id.tv_content);
            this.tvTagsAndPostId = (TextView) itemView.findViewById(R.id.tv_CategoryPostId);   // New
            this.tvLikes = (TextView) itemView.findViewById(R.id.tv_likes);
        }

        // Getter method (for each obj)
        public ImageView getPostImage() {return ivPostImage;}
        public TextView getPostUsername() {return tvPostUsername;}
        public TextView getPostContent() {return tvPostContent;}
        public TextView getCategoryPostId() {return tvTagsAndPostId;}
        public TextView getLikes() {return tvLikes;}
    }

}
