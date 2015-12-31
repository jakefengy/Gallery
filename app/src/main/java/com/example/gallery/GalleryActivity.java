package com.example.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gallery.recycler.StickyHeadersBuilder;
import com.example.gallery.recycler.StickyHeadersItemDecoration;
import com.example.gallery.utils.BizImageFolder;
import com.example.gallery.utils.GalleryUtils;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private String GralleryType;
    private static final String TypeLine = "line";
    private static final String TypeGrid = "grid";
    private static final String TypeStaggered = "staggered";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_line:
                        GralleryType = TypeLine;
                        break;
                    case R.id.radio_grid:
                        GralleryType = TypeGrid;
                        break;
                    case R.id.radio_staggered:
                        GralleryType = TypeStaggered;
                        break;
                    default:
                        GralleryType = TypeLine;
                        break;
                }
            }
        });

        GalleryUtils.scanImage(GalleryActivity.this, new GalleryUtils.IScanImageListener() {
            @Override
            public void onSuccess() {
                initRecyclerView();
            }

            @Override
            public void onError() {
                Toast.makeText(GalleryActivity.this, "你鸡巴太短", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initRecyclerView() {

        List<BizImageFolder> folderList = GalleryUtils.getImageFolders();
        List<String> folderTitle = new ArrayList<>();
        for (BizImageFolder folder : folderList) {
            folderTitle.add(folder.getMemoryCard());
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gallery_id);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(GalleryActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ImageFolderAdapter folderAdapter = new ImageFolderAdapter(GalleryActivity.this, null);

        StickyHeadersItemDecoration itemDecoration = new StickyHeadersBuilder()
                .setAdapter(folderAdapter)
                .setRecyclerView(recyclerView)
                .setStickyHeadersAdapter(new HeaderAdapter(folderTitle))
                .build();

        recyclerView.setAdapter(folderAdapter);
        recyclerView.addItemDecoration(itemDecoration);

        folderAdapter.updateSource(folderList);

        folderAdapter.setOnItemClickListener(new ImageFolderAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, BizImageFolder bizImageFolder) {
                Intent intent = new Intent();
                if (GralleryType.equals(TypeLine)) {

                } else if (GralleryType.equals(TypeGrid)) {

                } else if (GralleryType.equals(TypeStaggered)) {

                }
            }
        });

    }


}
