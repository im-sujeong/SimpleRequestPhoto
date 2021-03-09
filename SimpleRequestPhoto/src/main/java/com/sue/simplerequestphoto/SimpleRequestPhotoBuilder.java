package com.sue.simplerequestphoto;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public abstract class SimpleRequestPhotoBuilder<T extends SimpleRequestPhotoBuilder>{
    private PhotoListener listener;

    Context context;

    float requestMaxSize = -1;
    float requestThumbSize = 100;
    int requestQuality = -1;
    int requestThumnailQuality = -1;
    int limitSize = -1;
    boolean isResizeThumbnail = false;

    public SimpleRequestPhotoBuilder(Context context) {
        this.context = context;
    }

    protected void request(int type) {
        Intent intent = new Intent(context, SimpleRequestPhotoActivity.class);

        intent.putExtra(SimpleRequestPhotoActivity.EXTARY_REQUEST_TYPE, type);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_REQUEST_MAX_SIZE, requestMaxSize);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_REQUEST_QUAILITY, requestQuality);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_REQUEST_THUMBNAIL_QUAILITY, requestThumnailQuality);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_IS_RESIZE_THUMBNAIL, isResizeThumbnail);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_REQUEST_THUMBNAIL_SIZE, requestThumbSize);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_LIMIT_SIZE, limitSize);

        SimpleRequestPhotoActivity.startActivity(context, intent, listener);
    }

    public T setPhotoListener(PhotoListener listener) {
        this.listener = listener;
        return (T) this;
    }

    public T setRequestMaxSize(int requestMaxSize) {
        this.requestMaxSize = requestMaxSize;
        return (T) this;
    }

    public T setLimitSize(int limitSize) {
        this.limitSize = limitSize;
        return (T) this;
    }

    public T setRequestQuality(int requestQuality) {
        this.requestQuality = requestQuality;
        return (T) this;
    }

    public T setRequestThumbSize(int requestThumbSize) {
        this.requestThumbSize = requestThumbSize;
        return (T) this;
    }

    public T setResizeThumbnail(boolean resizeThumbnail) {
        isResizeThumbnail = resizeThumbnail;
        return (T) this;
    }

    public T setRequestThumnailQuality(int requestThumnailQuality) {
        this.requestThumnailQuality = requestThumnailQuality;
        return (T) this;
    }
}
