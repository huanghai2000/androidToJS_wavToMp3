package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;

/**
 * Created by hai.huang on 2018/3/30.
 * 用户操作记录
 */

public class OperationRecordBean implements Serializable{
    private String UserId;
    private String userType;
    private String operId;
    private String content;
    private String cbId;
    public String getCbId() {
        return cbId;
    }

    public void setCbId(String cbId) {
        this.cbId = cbId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
