package com.sue.simplerequestphoto;

public interface PhotoListener {
    void onSelectedPhoto(String path, String thumbnailPath);
    void onFailed(int failType);
    void onError(Throwable throwable);
}
