package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;

/**
 * Created by hai.huang on 2018/3/22.
 */

public class H5Bean implements Serializable {
    private String UserID;
    private String BookID;
    private String ClassID;
    private String AspxName;

    public String getCbId() {
        return cbId;
    }

    public void setCbId(String cbId) {
        this.cbId = cbId;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getBookID() {
        return BookID;
    }

    public void setBookID(String bookID) {
        BookID = bookID;
    }

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String classID) {
        ClassID = classID;
    }

    public String getAspxName() {
        return AspxName;
    }

    public void setAspxName(String aspxName) {
        AspxName = aspxName;
    }

    private String cbId;
}
