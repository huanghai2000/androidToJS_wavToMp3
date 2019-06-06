package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hai.huang on 2018/3/15.
 */

public class LoginBean implements Serializable {
    private String Id;
    private String Token;
    private String Type;
    private String Account;
    private String  Name;
    private String  Subject;
    private String  AvatarUrl;
    private String  IsFirstLogin;

    public ArrayList<ClassBean> getClasses() {
        return Classes;
    }

    public void setClasses(ArrayList<ClassBean> classes) {
        Classes = classes;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getAvatarUrl() {
        return AvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        AvatarUrl = avatarUrl;
    }

    public String getIsFirstLogin() {
        return IsFirstLogin;
    }

    public void setIsFirstLogin(String isFirstLogin) {
        IsFirstLogin = isFirstLogin;
    }

    private ArrayList<ClassBean> Classes;
    public static class ClassBean implements  Serializable{
        private String SubjectID;
        private String SubjectName;
        private String  ClassID;
        private String ClassName;
        private String GradeID;
        private boolean isUpdata;//是否需要更新
        /*********************下载列表使用字段*******************/
        private String BookID;
        private String BookName;
        private String ZipName;
        private String PreTime;
        public boolean isUpdata() {
            return isUpdata;
        }

        public void setUpdata(boolean updata) {
            isUpdata = updata;
        }
        public String getPreTime() {
            return PreTime;
        }

        public void setPreTime(String preTime) {
            PreTime = preTime;
        }

        public String getZipName() {
            return ZipName;
        }

        public void setZipName(String zipName) {
            ZipName = zipName;
        }

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

        /******************下载列表使用字段**********************/

        public String getGradeName() {
            return GradeName;
        }

        public void setGradeName(String gradeName) {
            GradeName = gradeName;
        }

        public String getSubjectID() {
            return SubjectID;
        }

        public void setSubjectID(String subjectID) {
            SubjectID = subjectID;
        }

        public String getSubjectName() {
            return SubjectName;
        }

        public void setSubjectName(String subjectName) {
            SubjectName = subjectName;
        }

        public String getClassID() {
            return ClassID;
        }

        public void setClassID(String classID) {
            ClassID = classID;
        }

        public String getClassName() {
            return ClassName;
        }

        public void setClassName(String className) {
            ClassName = className;
        }

        public String getGradeID() {
            return GradeID;
        }

        public void setGradeID(String gradeID) {
            GradeID = gradeID;
        }

        private String GradeName;
    }
}
