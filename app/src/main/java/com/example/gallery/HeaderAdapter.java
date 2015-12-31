package com.example.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gallery.recycler.StickyHeadersAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class HeaderAdapter implements StickyHeadersAdapter<HeaderAdapter.ViewHolder> {

    private List<String> items;

    public HeaderAdapter(List<String> items) {
        this.items = items == null ? new ArrayList<String>() : items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_header, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder headerViewHolder, int position) {
        headerViewHolder.title.setText(items.get(position));
    }

    @Override
    public long getHeaderId(int position) {
        return items.get(position).hashCode();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tv_header);
        }
    }

    public void updateSource(List<String> source) {
        items.clear();
        if (source == null) {
            return;
        }
        items.addAll(source);

    }

}
