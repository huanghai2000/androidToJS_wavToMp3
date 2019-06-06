package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;

/**
 * Created by hai.huang on 2017/11/6.
 */

public class NewProBean implements Serializable {
    private int code;
    private String msg;

    public sendBean getData() {
        return data;
    }

    public void setData(sendBean data) {
        this.data = data;
    }

    private sendBean data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static  class sendBean implements  Serializable{
        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        private int score;

    }
}
