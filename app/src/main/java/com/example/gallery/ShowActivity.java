package com.example.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.gallery.utils.GalleryUtils;

/**
 * show gallery images
 */
public class ShowActivity extends AppCompatActivity {

    // key
    private String galleryType, folderName;

    // view
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        galleryType = getIntent().getStringExtra("GalleryType");
        folderName = getIntent().getStringExtra("FolderName");

        setContentView(R.layout.activity_show);

        // init view
        recyclerView = (RecyclerView) findViewById(R.id.show);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (galleryType.equals(GalleryActivity.TypeLine)) {
            recyclerView.setLayoutManager(new LinearLayoutManager(ShowActivity.this, LinearLayoutManager.VERTICAL, false));
        } else if (galleryType.equals(GalleryActivity.TypeGrid)) {
            recyclerView.setLayoutManager(new GridLayoutManager(ShowActivity.this, 2));
        } else if (galleryType.equals(GalleryActivity.TypeStaggered)) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }

        imageAdapter = new ImageAdapter(ShowActivity.this, GalleryUtils.getImageByFolder(folderName));
        recyclerView.setAdapter(imageAdapter);

    }




}
