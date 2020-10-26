package com.sue.simplerequestphoto;

import android.content.Context;
import android.content.Intent;

public abstract class SimpleRequestPhotoBuilder<T extends SimpleRequestPhotoBuilder>{
    private PhotoListener listener;

    Context context;

    public SimpleRequestPhotoBuilder(Context context) {
        this.context = context;
    }

    /*
    * 원본
    * */
    protected void request(int type) {
        Intent intent = new Intent(context, SimpleRequestPhotoActivity.class);

        intent.putExtra(SimpleRequestPhotoActivity.EXTARY_REQUEST_TYPE, type);

        SimpleRequestPhotoActivity.startActivity(context, intent, listener);
    }

    /*
    * 이미지 변형. 변형된 이미지 생성 됨 (isResizeThumbnail가 true 일 경우, 썸네일 모양의 이미지가 생성됨)
    *
    * requestMaxSize : 긴 면의 최대 사이즈 (isResizeThumbnail가 true 일 경우 짧은 면의 최대 사이즈가 됨)
    * isResizeThumbnail : 썸네일 모양으로 변형 할 지 여부 (true 일 경우, 정사각형으로 크롭됨)
    * */
    protected void request(int type, float requestMaxSize, int requestQuality, boolean isResizeThumbnail) {
        Intent intent = new Intent(context, SimpleRequestPhotoActivity.class);

        intent.putExtra(SimpleRequestPhotoActivity.EXTARY_REQUEST_TYPE, type);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_REQUEST_MAX_SIZE, requestMaxSize);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_REQUEST_QUAILITY, requestQuality);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_IS_RESIZE_THUMBNAIL, isResizeThumbnail);

        SimpleRequestPhotoActivity.startActivity(context, intent, listener);
    }

    /*
     * 이미지 변형. 변형된 이미지와 썸네일이 생성 됨.
     *
     * requestMaxSize : 긴 면의 최대 사이즈
     * requestThumbSize : 썸네일 사이즈
     * */
    protected void request(int type, float requestMaxSize, float requestThumbSize, int requestQuality) {
        Intent intent = new Intent(context, SimpleRequestPhotoActivity.class);

        intent.putExtra(SimpleRequestPhotoActivity.EXTARY_REQUEST_TYPE, type);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_REQUEST_MAX_SIZE, requestMaxSize);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_REQUEST_THUMBNAIL_SIZE, requestThumbSize);
        intent.putExtra(SimpleRequestPhotoActivity.EXTRA_REQUEST_QUAILITY, requestQuality);

        SimpleRequestPhotoActivity.startActivity(context, intent, listener);
    }

    public T setPhotoListener(PhotoListener listener) {
        this.listener = listener;
        return (T) this;
    }
}
