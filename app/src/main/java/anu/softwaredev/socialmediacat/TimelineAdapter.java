package anu.softwaredev.socialmediacat;

import anu.softwaredev.socialmediacat.dao.decorator.UserActivity;
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


// create own's Adapter for the Timeline (Recycler): Imple Methods
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder> {
    private final Context ctx;
    private final List<UserActivity> dataset;

    public TimelineAdapter(Context ctx, List<UserActivity> dataset) {
        this.ctx = ctx;
        this.dataset = dataset;
    }

    // imple Recycler Adapter methods
    @NonNull
    @Override   // Uses layoutInflater for XML post
    public TimelineAdapter.TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.activity_timeline_post, parent, false);
        return new TimelineViewHolder(view);
    }

    @Override      // associates View with Data (dataset)
    public void onBindViewHolder(@NonNull TimelineAdapter.TimelineViewHolder holder, int position) {

        // action, uname, category, content, postId

        holder.getPostUsername().setText(dataset.get(position).getUId());
        holder.getPostContent().setText(dataset.get(position).getContent());
        holder.getCategoryPostId().setText(dataset.get(position).getCategory()+"["+dataset.get(position).getPostId()+"]");
        // TODO - likes?

        // IMAGE: load rand image from external source using Glide (code from ref) (*install Glide?)
        int id = (int) (Math.random() *((100-20)+1) + 20); 	// gen rand id (use for URL below), max=100 min=(20)
        // Glide ** [Image]
        Glide.with(ctx).load("https://picsum.photos/id/" + id + "/300/200").apply(new RequestOptions())
                .diskCacheStrategy(DiskCacheStrategy.NONE)       	// ≈ image loaded, dun want it cached
                .skipMemoryCache(true)
              .into(holder.getPostImage());
    }

    @Override             // no. of items we hv
    public int getItemCount() {
        return dataset.size();
    }

    /** Create [TimelineViewHolder] class (extends RecyclerView.ViewHolder)
     * TimelineViewHolder: add Fields (objs in a post) and findViews (from XML file) */
    public class TimelineViewHolder extends RecyclerView.ViewHolder {

        // Posts' Fields
        private final ImageView ivPostImage;
        private final TextView tvPostUsername;
        private final TextView tvPostContent;
        private final TextView tvCategoryPostId;    // NEW
        private final TextView tvLikes;

        public TimelineViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ivPostImage = (ImageView) itemView.findViewById(R.id.iv_post_image);
            this.tvPostUsername = (TextView) itemView.findViewById(R.id.tv_userName);
            this.tvPostContent = (TextView) itemView.findViewById(R.id.tv_content);
            this.tvCategoryPostId = (TextView) itemView.findViewById(R.id.tv_CategoryPostId);   // New
            this.tvLikes = (TextView) itemView.findViewById(R.id.tv_likes);
        }

        // Getter method (for each obj)
        public ImageView getPostImage() {return ivPostImage;}
        public TextView getPostUsername() {return tvPostUsername;}
        public TextView getPostContent() {return tvPostContent;}
        public TextView getCategoryPostId() {return tvCategoryPostId;}
        public TextView getLikes() {return tvLikes;}
    }

}
