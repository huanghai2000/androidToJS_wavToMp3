package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hai.huang on 2018/2/5.
 */

public class SendBeanByH5_PageData implements Serializable {
    private String Message;
    private boolean Success;
    private String Code;
    private ArrayList<String> Data;

    public ArrayList<String> getData() {
        return Data;
    }

    public void setData(ArrayList<String> data) {
        Data = data;
    }

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
}
