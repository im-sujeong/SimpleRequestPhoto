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

        public void takePhoto(int requestWidth, int requestHeight, int requestQuality) {
            request(SimpleRequestPhotoActivity.TYPE_TAKE_PHOTO, requestWidth, requestHeight, requestQuality);
        }

        public void takePhoto(int requestWidth, int requestHeight, int requestThumbnailSize, int requestQuality) {
            request(SimpleRequestPhotoActivity.TYPE_TAKE_PHOTO, requestWidth, requestHeight, requestThumbnailSize, requestQuality);
        }

        public void pickPhoto() {
            request(SimpleRequestPhotoActivity.TYPE_PICK_PHOTO);
        }

        public void pickPhoto(int requestWidth, int requestHeight, int requestQuality) {
            request(SimpleRequestPhotoActivity.TYPE_PICK_PHOTO, requestWidth, requestHeight, requestQuality);
        }

        public void pickPhoto(int requestWidth, int requestHeight, int requestThumbnailSize, int requestQuality) {
            request(SimpleRequestPhotoActivity.TYPE_PICK_PHOTO, requestWidth, requestHeight, requestThumbnailSize, requestQuality);
        }
    }
}
