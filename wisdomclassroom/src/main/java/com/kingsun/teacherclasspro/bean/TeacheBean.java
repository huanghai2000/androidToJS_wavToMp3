package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hai.huang on 2018/3/22.
 */

public class TeacheBean implements Serializable {

    private String Message;
    private boolean Success;
    private String Code;
    private ArrayList<otherBean> Data;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public ArrayList<otherBean> getData() {
        return Data;
    }

    public void setData(ArrayList<otherBean> data) {
        Data = data;
    }

    public static class otherBean{

        private String PageInitID;
        private String UserID;
        private String GradeID;
        private String ClassID;
        private String BookID;
        private String SubjectID;
        private String UnitID;
        private String PageNum;
        private String CreateTime;
        private String AspxName;
        private String EditionID;
        private String BookType;
        private String Stage;
        private String UnitName;

        public String getPageInitID() {
            return PageInitID;
        }

        public void setPageInitID(String pageInitID) {
            PageInitID = pageInitID;
        }

        public String getUserID() {
            return UserID;
        }

        public void setUserID(String userID) {
            UserID = userID;
        }

        public String getGradeID() {
            return GradeID;
        }

        public void setGradeID(String gradeID) {
            GradeID = gradeID;
        }

        public String getClassID() {
            return ClassID;
        }

        public void setClassID(String classID) {
            ClassID = classID;
        }

        public String getBookID() {
            return BookID;
        }

        public void setBookID(String bookID) {
            BookID = bookID;
        }

        public String getSubjectID() {
            return SubjectID;
        }

        public void setSubjectID(String subjectID) {
            SubjectID = subjectID;
        }

        public String getUnitID() {
            return UnitID;
        }

        public void setUnitID(String unitID) {
            UnitID = unitID;
        }

        public String getPageNum() {
            return PageNum;
        }

        public void setPageNum(String pageNum) {
            PageNum = pageNum;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String createTime) {
            CreateTime = createTime;
        }

        public String getAspxName() {
            return AspxName;
        }

        public void setAspxName(String aspxName) {
            AspxName = aspxName;
        }

        public String getEditionID() {
            return EditionID;
        }

        public void setEditionID(String editionID) {
            EditionID = editionID;
        }

        public String getBookType() {
            return BookType;
        }

        public void setBookType(String bookType) {
            BookType = bookType;
        }

        public String getStage() {
            return Stage;
        }

        public void setStage(String stage) {
            Stage = stage;
        }

        public String getUnitName() {
            return UnitName;
        }

        public void setUnitName(String unitName) {
            UnitName = unitName;
        }
    }
}
