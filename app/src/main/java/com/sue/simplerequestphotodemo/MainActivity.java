package com.sue.simplerequestphotodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
            SimpleRequestPhoto.with(this)
                    .setPhotoListener(new PhotoListener() {
                        @Override
                        public void onSelectedPhoto(String path, String thumbnailPath) {
                            LogUtil.i(TAG, "path " + path);
                            LogUtil.i(TAG, "thumbnailPath " + thumbnailPath);
                        }

                        @Override
                        public void onFailed(int failType) {

                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    })
                    .setLimitSize(3000)
                    .setRequestMaxSize(960)
                    .setRequestThumbSize(250)
                    .setRequestQuality(90)
                    .takePhoto();
        }else if( view == pickPhotoBtn ) {
            SimpleRequestPhoto.with(this)
                    .setPhotoListener(new PhotoListener() {
                        @Override
                        public void onSelectedPhoto(String path, String thumbnailPath) {
                            LogUtil.i(TAG, "path " + path);
                            LogUtil.i(TAG, "thumbnailPath " + thumbnailPath);

                            Glide.with(MainActivity.this)
                                    .load(thumbnailPath)
                                    .into(imgView);
                        }

                        @Override
                        public void onFailed(int failType) {
                            if( failType == SimpleRequestPhoto.WIDTH_SIZE_EXCESS ) {
                                Toast.makeText(MainActivity.this, "가로 사이즈가 너무 길어요!", Toast.LENGTH_SHORT).show();
                            }else if( failType == SimpleRequestPhoto.HEIGHT_SIZE_EXCESS ) {
                                Toast.makeText(MainActivity.this, "세로 사이즈가 너무 길어요!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    })
                    .setLimitSize(3000)
                    .setRequestMaxSize(720)
                    .setRequestThumbSize(500)
                    .setRequestQuality(90)
                    .setResizeType(SimpleRequestPhoto.RESIZE_LONG_SIDE)
                    .pickPhoto();
        }
    }
}