package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;

/**
 * Created by hai.huang on 2018/3/15.
 */

public class BookInfoBean implements Serializable {
    private String ModifyDateTime ;
    private String CreateDateTime;
    private String Deleted;
    private String Edition;
    private String BooKName;
    private String Remark;
    private String ID;
    private String Stage;
    private String Subject;
    private String Grade;
    private String Booklet;
    private String BookType;//1是同步，2是教辅，3是特色
    private String AgeRange;
    private String imgPath;
    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    private boolean isCheck;//
    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getModifyDateTime() {
        return ModifyDateTime;
    }

    public void setModifyDateTime(String modifyDateTime) {
        ModifyDateTime = modifyDateTime;
    }

    public String getCreateDateTime() {
        return CreateDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        CreateDateTime = createDateTime;
    }

    public String getDeleted() {
        return Deleted;
    }

    public void setDeleted(String deleted) {
        Deleted = deleted;
    }

    public String getEdition() {
        return Edition;
    }

    public void setEdition(String edition) {
        Edition = edition;
    }

    public String getBooKName() {
        return BooKName;
    }

    public void setBooKName(String booKName) {
        BooKName = booKName;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getStage() {
        return Stage;
    }

    public void setStage(String stage) {
        Stage = stage;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    public String getBooklet() {
        return Booklet;
    }

    public void setBooklet(String booklet) {
        Booklet = booklet;
    }

    public String getBookType() {
        return BookType;
    }

    public void setBookType(String bookType) {
        BookType = bookType;
    }

    public String getAgeRange() {
        return AgeRange;
    }

    public void setAgeRange(String ageRange) {
        AgeRange = ageRange;
    }
}
