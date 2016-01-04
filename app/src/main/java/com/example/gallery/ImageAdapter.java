/*
 * Copyright (C) 2014 VenomVendor <info@VenomVendor.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gallery.utils.BizImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final Context mActivity;
    private List<BizImage> mDataSource;

    private Bitmap failBitmap;

    private DisplayImageOptions options;

    public ImageAdapter(Context context, List<BizImage> images) {
        this.mActivity = context;
        this.mDataSource = images == null ? new ArrayList<BizImage>() : images;
        this.failBitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.ic_launcher);

        this.options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(mActivity);
        final View sView = mInflater.inflate(R.layout.listitem_image, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BizImage image = mDataSource.get(position);

        ImageLoader.getInstance().displayImage("file://" + image.getImagePath(), holder.vAvatar, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.vAvatar.setImageWidth(failBitmap.getWidth());
                holder.vAvatar.setImageHeight(failBitmap.getHeight());
                holder.vAvatar.setImageBitmap(failBitmap);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.vAvatar.setImageWidth(failBitmap.getWidth());
                holder.vAvatar.setImageHeight(failBitmap.getHeight());
                holder.vAvatar.setImageBitmap(failBitmap);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.vAvatar.setImageWidth(loadedImage.getWidth());
                holder.vAvatar.setImageHeight(loadedImage.getHeight());
                holder.vAvatar.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    @Override
    public long getItemId(int position) {
        return mDataSource.get(position).hashCode();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ScaleImageView vAvatar;

        public ViewHolder(View view) {
            super(view);
            vAvatar = (ScaleImageView) view.findViewById(R.id.list_item);
        }

    }

}
