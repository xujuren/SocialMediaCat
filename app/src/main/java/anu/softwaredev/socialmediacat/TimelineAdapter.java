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
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>{

    /* add fields & a Constructor (Context, the dataset: List<UserActivity>) */
    private final Context ctx;
    private final List<UserActivity> dataset;

    public TimelineAdapter(Context ctx, List<UserActivity> dataset) {
        this.ctx = ctx;
        this.dataset = dataset;
    }

    /** imple Methods (Recycler Adapter) */
    @NonNull
    @Override       // Uses "layoutInflator": send xxx to XML we’re to create now
    public TimelineAdapter.TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.activity_timeline, parent, false);
        return new TimelineViewHolder(view);
    }

    @Override      // associates the View with Data we hv (this.dataset): using getter methods above*
    public void onBindViewHolder(@NonNull TimelineAdapter.TimelineViewHolder holder, int position) {
        holder.getPostUsername().setText(dataset.get(position).getUsername());
        holder.getPostContent().setText(dataset.get(position).getContent());

        // M (image): load rand image from external source using Glide (code from ref) (*install Glide?)
        int id = (int) Math.random() *((100-0)+1) + 0; 	// gen rand id (use for URL below), max=100 min=0

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

    /** Create [TimelineViewHolder] class (extends RecyclerView.ViewHolder) */
    public class TimelineViewHolder extends RecyclerView.ViewHolder {

        /* Fields (within each "Post") */
        private final ImageView ivPostImage;
        private final TextView tvPostUsername;
        private final TextView tvPostContent;

        public TimelineViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ivPostImage = (ImageView) itemView.findViewById(R.id.iv_post_image);
            this.tvPostUsername = (TextView) itemView.findViewById(R.id.tv_post_user);
            this.tvPostContent = (TextView) itemView.findViewById(R.id.tv_post_content);
        }

        // Getter method (for each obj)
        public ImageView getPostImage() {return ivPostImage;}
        public TextView getPostUsername() {return tvPostUsername;}
        public TextView getPostContent() {return tvPostContent;}
    }

}
