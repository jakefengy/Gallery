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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gallery.utils.BizImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final Context mActivity;
    private List<BizImage> mDataSource;

    private DisplayImageOptions options;

    public ImageAdapter(Context context, List<BizImage> images) {
        this.mActivity = context;
        this.mDataSource = images == null ? new ArrayList<BizImage>() : images;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(mActivity);
        final View sView = mInflater.inflate(R.layout.listitem_image, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BizImage image = mDataSource.get(position);

        ImageLoader.getInstance().displayImage("file://" + image.getImagePath(), holder.vAvatar, options);
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

        ImageView vAvatar;

        public ViewHolder(View view) {
            super(view);
            vAvatar = (ImageView) view.findViewById(R.id.list_item);
        }

    }

    public void updateSource(List<BizImage> data) {
        if (data != null && data.size() > 0) {
            mDataSource.clear();
            mDataSource.addAll(data);
            notifyDataSetChanged();
        }
    }
}
