package com.example.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gallery.utils.BizImageFolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * SelectImageAdapter For Activity_Photo
 */
public class ImageFolderAdapter extends RecyclerView.Adapter<ImageFolderAdapter.ImageFolderHolder> {


    private Context mContext;
    private List<BizImageFolder> mDataSource;
    private LayoutInflater mLayoutInflater;
    private DisplayImageOptions options;
    private OnItemClickListener onItemClickListener;

    public ImageFolderAdapter(Context context, List<BizImageFolder> dataSource) {

        this.mContext = context;
        this.mDataSource = dataSource == null ? new ArrayList<BizImageFolder>() : dataSource;
        this.mLayoutInflater = LayoutInflater.from(mContext);

        this.options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();

        setHasStableIds(true);
    }

    @Override
    public ImageFolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageFolderHolder(mLayoutInflater.inflate(R.layout.listitem_image_folder, parent, false));
    }

    @Override
    public long getItemId(int position) {
        return mDataSource.get(position).hashCode();
    }

    @Override
    public void onBindViewHolder(ImageFolderHolder holder, int position) {

        BizImageFolder bizImageFolder = mDataSource.get(position);

        ImageLoader.getInstance().displayImage("file://" + bizImageFolder.getFrontCover(), holder.ivFrontCover, options);
        holder.tvName.setText(bizImageFolder.getName());
        holder.tvImageCount.setText("(" + bizImageFolder.getImageCount() + ")");

    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    public void updateSource(List<BizImageFolder> source) {
        if (source != null && source.size() > 0) {
            mDataSource.clear();
            mDataSource.addAll(source);
            notifyDataSetChanged();
        }
    }

    public class ImageFolderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivFrontCover;
        TextView tvName, tvImageCount;

        public ImageFolderHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            ivFrontCover = (ImageView) itemView.findViewById(R.id.iv_front_cover);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvImageCount = (TextView) itemView.findViewById(R.id.tv_count);

        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(itemView, mDataSource.get(getPosition()));
            }
        }
    }

    // Onclick Listener
    public interface OnItemClickListener {
        void onClick(View view, BizImageFolder bizImageFolder);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

}
