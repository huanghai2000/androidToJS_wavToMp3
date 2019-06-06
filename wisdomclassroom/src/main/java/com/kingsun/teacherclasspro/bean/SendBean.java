package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;

/**
 * Created by hai.huang on 2018/2/5.
 */

public class SendBean implements Serializable {
    private String Message;
    private boolean Success;
    private String Code;
    private JsonBean Data;

    public JsonBean getData() {
        return Data;
    }

    public void setData(JsonBean data) {
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

    public static class JsonBean{
        private String Url;
        private String Json;

        public String getJson() {
            return Json;
        }

        public void setJson(String json) {
            Json = json;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String url) {
            Url = url;
        }

    }
}
