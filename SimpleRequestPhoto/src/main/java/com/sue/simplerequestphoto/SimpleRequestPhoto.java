package com.sue.simplerequestphoto;

import android.content.Context;

public class SimpleRequestPhoto {
    public static Builder with(Context context) {
        return new Builder(context);
    }

    public static class Builder extends SimpleRequestPhotoBuilder<Builder> {
        public Builder(Context context) {
            super(context);
        }

        public void takePhoto() {
            request(SimpleRequestPhotoActivity.TYPE_TAKE_PHOTO);
        }

        public void takePhoto(int requestMaxSize, int requestQuality, boolean isResizeThumbnail) {
            request(SimpleRequestPhotoActivity.TYPE_TAKE_PHOTO, requestMaxSize, requestQuality, isResizeThumbnail);
        }

        public void takePhoto(int requestMaxSize, int requestThumbnailSize, int requestQuality) {
            request(SimpleRequestPhotoActivity.TYPE_TAKE_PHOTO, requestMaxSize, requestThumbnailSize, requestQuality);
        }

        public void pickPhoto() {
            request(SimpleRequestPhotoActivity.TYPE_PICK_PHOTO);
        }

        public void pickPhoto(int requestMaxSize, int requestQuality, boolean isResizeThumbnail) {
            request(SimpleRequestPhotoActivity.TYPE_PICK_PHOTO, requestMaxSize, requestQuality, isResizeThumbnail);
        }

        public void pickPhoto(int requestMaxSize, int requestThumbnailSize, int requestQuality) {
            request(SimpleRequestPhotoActivity.TYPE_PICK_PHOTO, requestMaxSize, requestThumbnailSize, requestQuality);
        }
    }
}
