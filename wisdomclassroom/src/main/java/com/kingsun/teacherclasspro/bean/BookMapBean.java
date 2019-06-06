package com.kingsun.teacherclasspro.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hai.huang on 2018/3/28.
 * 教学地图实体类
 */

public class BookMapBean implements Serializable{
    private String stepName;

    public ArrayList<BookMapInfo> getLiList() {
        return liList;
    }

    public void setLiList(ArrayList<BookMapInfo> liList) {
        this.liList = liList;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    private ArrayList<BookMapInfo> liList;

    public  static  class BookMapInfo{
        private String id;
        private String pageNum;
        private String sourceSrc;
        private String sourcetype;
        private String comeFrom;
        private String randId;

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPageNum() {
            return pageNum;
        }

        public void setPageNum(String pageNum) {
            this.pageNum = pageNum;
        }

        public String getSourceSrc() {
            return sourceSrc;
        }

        public void setSourceSrc(String sourceSrc) {
            this.sourceSrc = sourceSrc;
        }

        public String getSourcetype() {
            return sourcetype;
        }

        public void setSourcetype(String sourcetype) {
            this.sourcetype = sourcetype;
        }

        public String getComeFrom() {
            return comeFrom;
        }

        public void setComeFrom(String comeFrom) {
            this.comeFrom = comeFrom;
        }

        public String getRandId() {
            return randId;
        }

        public void setRandId(String randId) {
            this.randId = randId;
        }

        private String sourceName;
    }
}
