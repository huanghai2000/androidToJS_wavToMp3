package com.kingsun.teacherclasspro.callback;

public interface LoadCallBack<T>
{
    public void onSuccess(int i);
    public void onFailure(int i);
    public void onLoading(final long total, long current,
                          boolean isUploading, int i);
}
