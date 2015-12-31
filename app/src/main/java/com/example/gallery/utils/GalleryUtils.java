package com.example.gallery.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lvxia on 2015-12-31.
 */
public class GalleryUtils {

    private static List<BizImageFolder> imageFolders;
    private static HashMap<String, List<BizImage>> folderImages;

    public static void scanImage(Context context, IScanImageListener listener) {
        ScanImageTask scanImageTask = new ScanImageTask(context, listener);
        scanImageTask.execute();
    }

    public static List<BizImageFolder> getImageFolders() {
        return imageFolders;
    }

    public static List<BizImage> getImageByFolder(String folder) {
        if (TextUtils.isEmpty(folder)) {
            return null;
        }
        return folderImages.get(folder);
    }

    private static class ScanImageTask extends AsyncTask<Void, Void, Boolean> {

        private Context mContext;
        private IScanImageListener listener;

        public ScanImageTask(Context mContext, IScanImageListener listener) {
            this.mContext = mContext;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageFolders = new ArrayList<>();
            folderImages = new HashMap<>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // 内部存储
                List<BizImageFolder> inImageFolders = new ArrayList<>();
                inImageFolders.addAll(scanFolderFromMediaStore(mContext, MediaStore.Images.Media.INTERNAL_CONTENT_URI));
                if (inImageFolders.size() > 0) {
                    imageFolders.addAll(inImageFolders);
                    for (BizImageFolder imageFolder : inImageFolders) {
                        List<BizImage> images = new ArrayList<>();
                        images.addAll(scanImageFromMediaStore(mContext, MediaStore.Images.Media.INTERNAL_CONTENT_URI, imageFolder.getName(), Integer.MAX_VALUE));
                        if (images.size() > 0) {
                            folderImages.put(imageFolder.getFolderPath(), images);
                        }
                    }
                }

                // 外部存储
                List<BizImageFolder> exImageFolders = new ArrayList<>();
                exImageFolders.addAll(scanFolderFromMediaStore(mContext, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                if (exImageFolders.size() > 0) {
                    imageFolders.addAll(exImageFolders);
                    for (BizImageFolder imageFolder : exImageFolders) {
                        List<BizImage> images = new ArrayList<>();
                        images.addAll(scanImageFromMediaStore(mContext, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageFolder.getName(), Integer.MAX_VALUE));
                        if (images.size() > 0) {
                            folderImages.put(imageFolder.getFolderPath(), images);
                        }
                    }
                }

                // 搜索出最近添加的照片

                List<BizImage> recentImages = new ArrayList<>();
                recentImages.addAll(scanImageFromMediaStore(mContext, MediaStore.Images.Media.INTERNAL_CONTENT_URI, null, 50));
                recentImages.addAll(scanImageFromMediaStore(mContext, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, 50));

                if (recentImages.size() == 0) {
                    BizImageFolder imageFolder = new BizImageFolder();
                    imageFolder.setMemoryCard("最近照片");
                    imageFolder.setFolderPath("recent_image");
                    imageFolder.setImageCount("0");
                    imageFolder.setFrontCover("");
                    imageFolder.setName("最近照片");
                    imageFolders.add(0, imageFolder);
                    folderImages.put("recent_image", recentImages);
                } else {

                    Comparator<BizImage> comparator = new Comparator<BizImage>() {
                        public int compare(BizImage o1, BizImage o2) {
                            return o1.getDateAdded() < o2.getDateAdded() ? 1 : -1;
                        }
                    };
                    Collections.sort(recentImages, comparator);

                    int realMax = Math.min(50, recentImages.size());
                    List<BizImage> realImages = recentImages.subList(0, realMax);
                    BizImageFolder imageFolder = new BizImageFolder();
                    imageFolder.setMemoryCard("最近照片");
                    imageFolder.setFolderPath("recent_image");
                    imageFolder.setImageCount(String.valueOf(realImages.size()));
                    imageFolder.setFrontCover(realImages.get(0).getImagePath());
                    imageFolder.setName("最近照片");
                    imageFolders.add(0, imageFolder);
                    folderImages.put("recent_image", realImages);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            if (listener == null) {
                return;
            }

            if (aVoid) {
                listener.onSuccess();
            } else {
                listener.onError();
            }
        }

    }

    public interface IScanImageListener {
        void onSuccess();

        void onError();

    }

    private static List<BizImageFolder> scanFolderFromMediaStore(Context context, Uri tableName) {

        List<BizImageFolder> imageFolders = new ArrayList<>();

        String[] colums = {"count(*) as image_count", MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.DATA};

        Cursor folderCursor = context.getContentResolver().query(
                tableName, colums, " 1=1 ) group by ( " + MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                null, MediaStore.MediaColumns.DATE_ADDED + " DESC");

        try {

            if (folderCursor != null) {
                while (folderCursor.moveToNext()) {
                    String folderName = folderCursor.getString(folderCursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
                    String imagePath = folderCursor.getString(folderCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                    String imageCount = folderCursor.getString(folderCursor.getColumnIndex("image_count"));

                    BizImageFolder imageFolder = new BizImageFolder();
                    imageFolder.setName(folderName);
                    imageFolder.setFrontCover(imagePath);
                    imageFolder.setImageCount(imageCount);
                    imageFolder.setMemoryCard(tableName.equals(MediaStore.Images.Media.EXTERNAL_CONTENT_URI) ? "sd卡" : "手机");
                    imageFolder.setFolderPath((tableName.equals(MediaStore.Images.Media.EXTERNAL_CONTENT_URI) ? "external" : "internal") + folderName);
                    imageFolders.add(imageFolder);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            folderCursor.close();
        }

        return imageFolders;

    }

    private static List<BizImage> scanImageFromMediaStore(Context context, Uri tableName, String folderName, int pageSize) {

        List<BizImage> images = new ArrayList<>();

        String[] imageColums = {
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.DATE_ADDED
        };

        String selection = TextUtils.isEmpty(folderName) ? null : MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + " = '" + folderName + "'";
        String sortOrder = MediaStore.MediaColumns.DATE_ADDED + " DESC";

        Cursor imageCursor = context.getContentResolver().query(tableName, imageColums, selection, null, sortOrder);

        int realSize = Math.min(imageCursor.getCount(), pageSize);

        try {

            while (imageCursor.moveToNext() && images.size() < realSize) {

                String name = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
                String path = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                long dateAdded = imageCursor.getLong(imageCursor.getColumnIndex(MediaStore.MediaColumns.DATE_ADDED));

                BizImage image = new BizImage();
                image.setName(name);
                image.setImagePath(path);
                image.setDateAdded(dateAdded);

                images.add(image);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            imageCursor.close();
        }

        return images;
    }

}
