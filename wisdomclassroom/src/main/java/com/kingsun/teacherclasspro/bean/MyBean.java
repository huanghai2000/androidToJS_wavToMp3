package com.kingsun.teacherclasspro.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hai.huang on 2017/9/13.
 */

public class MyBean implements Parcelable {

    /**
     * RequestID :
     * Success : true
     * Data : {"ID":"5a117901-121b-4c1b-a6bf-62b8ec502cea","FileName":"57c58PICZkI_1024","FileExtension":"jpg","FileSize":106066,"FileType":7,"FilePath":"2017\\09\\13"}
     * ErrorMsg :
     */

    private String RequestID;
    private boolean Success;
    private DataBean Data;
    private String ErrorMsg;

    public String getRequestID() {
        return RequestID;
    }

    public void setRequestID(String RequestID) {
        this.RequestID = RequestID;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String ErrorMsg) {
        this.ErrorMsg = ErrorMsg;
    }

    public static class DataBean implements Parcelable {
        /**
         * ID : 5a117901-121b-4c1b-a6bf-62b8ec502cea
         * FileName : 57c58PICZkI_1024
         * FileExtension : jpg
         * FileSize : 106066
         * FileType : 7
         * FilePath : 2017\09\13
         */

        private String ID;
        private String FileName;
        private String FileExtension;
        private int FileSize;
        private int FileType;
        private String FilePath;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getFileName() {
            return FileName;
        }

        public void setFileName(String FileName) {
            this.FileName = FileName;
        }

        public String getFileExtension() {
            return FileExtension;
        }

        public void setFileExtension(String FileExtension) {
            this.FileExtension = FileExtension;
        }

        public int getFileSize() {
            return FileSize;
        }

        public void setFileSize(int FileSize) {
            this.FileSize = FileSize;
        }

        public int getFileType() {
            return FileType;
        }

        public void setFileType(int FileType) {
            this.FileType = FileType;
        }

        public String getFilePath() {
            return FilePath;
        }

        public void setFilePath(String FilePath) {
            this.FilePath = FilePath;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.ID);
            dest.writeString(this.FileName);
            dest.writeString(this.FileExtension);
            dest.writeInt(this.FileSize);
            dest.writeInt(this.FileType);
            dest.writeString(this.FilePath);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.ID = in.readString();
            this.FileName = in.readString();
            this.FileExtension = in.readString();
            this.FileSize = in.readInt();
            this.FileType = in.readInt();
            this.FilePath = in.readString();
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.RequestID);
        dest.writeByte(this.Success ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.Data, flags);
        dest.writeString(this.ErrorMsg);
    }

    public MyBean() {
    }

    protected MyBean(Parcel in) {
        this.RequestID = in.readString();
        this.Success = in.readByte() != 0;
        this.Data = in.readParcelable(DataBean.class.getClassLoader());
        this.ErrorMsg = in.readString();
    }

    public static final Parcelable.Creator<MyBean> CREATOR = new Parcelable.Creator<MyBean>() {
        @Override
        public MyBean createFromParcel(Parcel source) {
            return new MyBean(source);
        }

        @Override
        public MyBean[] newArray(int size) {
            return new MyBean[size];
        }
    };
}
