package marsplay.com.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cloudinary.Url;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.ResponsiveUrl;
import com.squareup.picasso.Picasso;

import java.util.List;

import marsplay.com.model.Resource;
import marsplay.com.views.R;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MessageViewHolder> {

    private List<Resource> imageArrayList;
    private Context context;
    private final ImageClickedListener listener;
    public ImageListAdapter(List<Resource> imageList, Context context,ImageClickedListener listener) {
        this.imageArrayList = imageList;
        this.context = context;
        this.listener = listener;

    }

    @Override
    public int getItemCount() {
        return imageArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return  super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, final int position) {

        final MessageViewHolder messageViewHolder = (MessageViewHolder) holder;

        Resource resource = imageArrayList.get(position);
        holder.itemView.setTag(resource);
        String publicId = resource.getCloudinaryPublicId();
        Url url = MediaManager.get().url().publicId(publicId).resourceType(resource.getResourceType()).format("webp");
        MediaManager.get().responsiveUrl(ResponsiveUrl.Preset.AUTO_FILL)
                .generate(url, messageViewHolder.thumbnailsView, new ResponsiveUrl.Callback() {
                    @Override
                    public void onUrlReady(Url url) {
                        Picasso.get().load(url.generate()).placeholder(R.drawable.placeholder).into(messageViewHolder.thumbnailsView);
                    }
                });

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnailsView;

        private MessageViewHolder(View view) {
            super(view);
            thumbnailsView = (ImageView) view.findViewById(R.id.thumbnails);
        }
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_item, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    Resource resource = (Resource) v.getTag();
                    listener.onImageClicked(resource);
                }
            }
        });
        return new MessageViewHolder(itemView);
    }

    public interface ImageClickedListener {
        void onImageClicked(Resource resource);
    }
}
