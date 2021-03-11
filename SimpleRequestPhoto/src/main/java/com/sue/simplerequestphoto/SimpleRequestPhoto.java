package com.sue.simplerequestphoto;

import android.content.Context;

public class SimpleRequestPhoto {
    final public static int WIDTH_SIZE_EXCESS = 100;
    final public static int HEIGHT_SIZE_EXCESS = 101;

    final public static int RESIZE_SHORT_SIDE = 200;
    final public static int RESIZE_LONG_SIDE = 201;

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

        public void pickPhoto() {
            request(SimpleRequestPhotoActivity.TYPE_PICK_PHOTO);
        }
    }
}
