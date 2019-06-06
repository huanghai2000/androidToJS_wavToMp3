package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;

/**
 * Created by hai.huang on 2018/3/15.
 */

public class BookBean implements Serializable {
    private String BookID ;
    private String BookName;
    private String ZipName;
    public String getBookID() {
        return BookID;
    }

    public void setBookID(String bookID) {
        BookID = bookID;
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public String getZipName() {
        return ZipName;
    }

    public void setZipName(String zipName) {
        ZipName = zipName;
    }
}
