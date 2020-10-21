package com.sue.simplerequestphoto;

import android.content.Context;
import android.content.Intent;

public abstract class SimpleRequestPhotoBuilder<T extends SimpleRequestPhotoBuilder>{
    private PhotoListener listener;

    Context context;

    public SimpleRequestPhotoBuilder(Context context) {
        this.context = context;
    }

    protected void request(int type) {
        Intent intent = new Intent(context, SimpleRequestPhotoActivity.class);

        intent.putExtra(SimpleRequestPhotoActivity.EXTARY_REQUEST_TYPE, type);

        SimpleRequestPhotoActivity.startActivity(context, intent, listener);
    }

    protected void request(int type, float requestWidth, float requestHeight, int requestQuality) {
        Intent intent = new Intent(context, SimpleRequestPhotoActivity.class);

        intent.putExtra(SimpleRequestPhotoActivity.EXTARY_REQUEST_TYPE, type);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_REQUEST_WIDTH, requestWidth);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_REQUEST_HEIGHT, requestHeight);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_REQUEST_QUAILITY, requestQuality);

        SimpleRequestPhotoActivity.startActivity(context, intent, listener);
    }

    protected void request(int type, float requestWidth, float requestHeight, float requestThumbSize, int requestQuality) {
        Intent intent = new Intent(context, SimpleRequestPhotoActivity.class);

        intent.putExtra(SimpleRequestPhotoActivity.EXTARY_REQUEST_TYPE, type);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_REQUEST_WIDTH, requestWidth);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_REQUEST_HEIGHT, requestHeight);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_REQUEST_THUMBNAIL_SIZE, requestThumbSize);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_REQUEST_QUAILITY, requestQuality);

        SimpleRequestPhotoActivity.startActivity(context, intent, listener);
    }

    public T setPhotoListener(PhotoListener listener) {
        this.listener = listener;
        return (T) this;
    }
}
