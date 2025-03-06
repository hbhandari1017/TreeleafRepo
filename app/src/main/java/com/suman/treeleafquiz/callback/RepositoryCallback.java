package com.suman.treeleafquiz.callback;


public interface RepositoryCallback<T> {
    void onSuccess(T result);
    void onError(int errorCode);
    void onFailure(Throwable throwable);

    void onOtherError(String error);
}
