package com.sue.simplerequestphotodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sue.simplerequestphoto.PhotoListener;
import com.sue.simplerequestphoto.SimpleRequestPhoto;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG = MainActivity.class.getSimpleName();

    Button takePhotoBtn;
    Button pickPhotoBtn;
    ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePhotoBtn = findViewById(R.id.take_photo_btn);
        pickPhotoBtn = findViewById(R.id.pick_photo_btn);
        imgView = findViewById(R.id.img_iv);

        takePhotoBtn.setOnClickListener(this);
        pickPhotoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if( view == takePhotoBtn ) {
            SimpleRequestPhoto.with(this).setPhotoListener(new PhotoListener() {
                @Override
                public void onSelectedPhoto(String path, String thumbnailPath) {
                    LogUtil.i(TAG, "path " + path);
                    LogUtil.i(TAG, "thumbnailPath " + thumbnailPath);
                }

                @Override
                public void onFailed() {

                }
            }).takePhoto(960, 250, 90);
        }else if( view == pickPhotoBtn ) {
            SimpleRequestPhoto.with(this).setPhotoListener(new PhotoListener() {
                @Override
                public void onSelectedPhoto(String path, String thumbnailPath) {
                    LogUtil.i(TAG, "path " + path);
                    LogUtil.i(TAG, "thumbnailPath " + thumbnailPath);
                }

                @Override
                public void onFailed() {

                }
            }).pickPhoto(960, 250, 90);
        }
    }
}