package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;

/**
 * Created by hai.huang on 2018/3/27.
 */

public class SocketBean  implements Serializable{
    private String cbId;
    private String data;

    public SocketBean(String cb, String da){
        this.cbId = cb;
        this.data = da;
    }
    public String getCbId() {
        return cbId;
    }

    public void setCbId(String cbId) {
        this.cbId = cbId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
