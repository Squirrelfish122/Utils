package com.hyman.zhh.utils.network;

/**
 *
 */
public abstract class NetworkListener<T> {

    private boolean isCancel;

    public abstract void onSuccess(T t);

    public abstract void onFail(String reason);

    public void cancel() {
        isCancel = true;
    }

    public boolean isCancel() {
        return isCancel;
    }
}
