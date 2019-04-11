package com.rahmatsyah.simlplechatwithopenfire.model;

import java.io.File;

public class FileData {
    private String heding;
    private String fileName;
    private File file;

    public FileData(String heding,String fileName, File file) {
        this.heding = heding;
        this.fileName = fileName;
        this.file = file;
    }

    public String getHeding() {
        return heding;
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }
}
