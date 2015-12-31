package com.example.gallery.utils;

/**
 * 图片文件夹
 */
public class BizImageFolder {

    private String Name;
    private String FrontCover;
    private String ImageCount;
    private String FolderPath;
    private String MemoryCard;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFrontCover() {
        return FrontCover;
    }

    public void setFrontCover(String frontCover) {
        FrontCover = frontCover;
    }

    public String getImageCount() {
        return ImageCount;
    }

    public void setImageCount(String imageCount) {
        ImageCount = imageCount;
    }

    public String getFolderPath() {
        return FolderPath;
    }

    public void setFolderPath(String folderPath) {
        FolderPath = folderPath;
    }

    public String getMemoryCard() {
        return MemoryCard;
    }

    public void setMemoryCard(String memoryCard) {
        MemoryCard = memoryCard;
    }
}
