package com.example.gallery.utils;

/**
 * 已选择的图片
 */
public class BizImage {

    private String Name;
    private String ImagePath;
    private long DateAdded;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public long getDateAdded() {
        return DateAdded;
    }

    public void setDateAdded(long dateAdded) {
        DateAdded = dateAdded;
    }
}
