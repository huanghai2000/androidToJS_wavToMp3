package com.kingsun.teacherclasspro.callback;

public interface NetworkCallBack<T>
{
    public void onSuccess(T t);
    public void onFailure(String error);
}
