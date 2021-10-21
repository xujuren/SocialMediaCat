package anu.softwaredev.socialmediacat;

import anu.softwaredev.socialmediacat.Classes.Post;

import android.content.Context;
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

/** Adapter for the Timeline displayed to Users */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder> {
    private final Context ctx;
    private final List<Post> dataset;
    private final int PHOTO_ID_LOWER=0;
    private final int PHOTO_ID_UPPER=100;

    public TimelineAdapter(Context ctx, List<Post> dataset) {
        this.ctx = ctx;
        this.dataset = dataset;
    }

    // Getter for the dataset
    public List<Post> getDataset() {return dataset; }

    /**
     * Implement Recycler Adapter methods
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
            holder.getTagLike().setText(dataset.get(position).getTag() + "  " + dataset.get(position).getLikeCount() + " likes");
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
        private final TextView tvTagLike;

        public TimelineViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ivPostImage = (ImageView) itemView.findViewById(R.id.iv_post_image);
            this.tvPostUsername = (TextView) itemView.findViewById(R.id.tv_userName);
            this.tvTagLike = (TextView) itemView.findViewById(R.id.tv_tagLike);
        }

        // Getter methods for the views
        public ImageView getPostImage() {return ivPostImage;}
        public TextView getPostUserId() {return tvPostUsername;}
        public TextView getTagLike() {return tvTagLike;}

    }



}
