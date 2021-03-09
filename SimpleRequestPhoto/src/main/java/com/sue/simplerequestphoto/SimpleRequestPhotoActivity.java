package com.sue.simplerequestphoto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimpleRequestPhotoActivity extends AppCompatActivity {
    final String TAG = "SimpleRequestPhoto_";

    final public static int TYPE_REQUEST_PERMISSION = 0;
    final public static int TYPE_TAKE_PHOTO = 1;
    final public static int TYPE_PICK_PHOTO = 2;

    final public static String EXTARY_REQUEST_TYPE = "type";
    final public static String EXTRA_REQUEST_MAX_SIZE = "request_width";
    final public static String EXTRA_REQUEST_THUMBNAIL_SIZE = "request_thumbnail_size";
    final public static String EXTRA_REQUEST_QUAILITY = "request_quality";
    final public static String EXTRA_REQUEST_THUMBNAIL_QUAILITY = "request_thumbnail_quality";
    final public static String EXTRA_IS_RESIZE_THUMBNAIL = "is_resize_thumbnail";
    final public static String EXTRA_LIMIT_SIZE = "limit_lize";

    private static PhotoListener photoListener;

    int type;
    boolean isPermissionGranted;

    String currentPhotoPath;
    String currentPhotoThumbnailPath;

    float reqMaxSize = -1;
    float reqThumbnailSize = -1;

    int reqQuality = 100;
    int reqThumbnailQuality = -1;
    int limitSize = -1;

    boolean isResizeThumbnail;

    public static void startActivity(Context context, Intent intent, PhotoListener listener) {
        photoListener = listener;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        init();
    }

    private void init() {
        type = getIntent().getIntExtra(EXTARY_REQUEST_TYPE, 0);

        reqMaxSize = getIntent().getFloatExtra(EXTRA_REQUEST_MAX_SIZE, -1);
        reqThumbnailSize = getIntent().getFloatExtra(EXTRA_REQUEST_THUMBNAIL_SIZE, -1);
        reqQuality = getIntent().getIntExtra(EXTRA_REQUEST_QUAILITY, 100);
        reqThumbnailQuality = getIntent().getIntExtra(EXTRA_REQUEST_THUMBNAIL_QUAILITY, -1);
        isResizeThumbnail = getIntent().getBooleanExtra(EXTRA_IS_RESIZE_THUMBNAIL, false);
        limitSize = getIntent().getIntExtra(EXTRA_LIMIT_SIZE, -1);

        if( type == TYPE_TAKE_PHOTO ) {
            requestPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void requestPermission(String... permissions) {
        List<String> needPermissions = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if( isDenied(this, permission) ) {
                    needPermissions.add(permission);
                }
            }
        }

        if( needPermissions.size() == 0 ) {
            isPermissionGranted = true;

            if( type == TYPE_TAKE_PHOTO ) {
                takePhoto();
            }else {
                pickPhoto();
            }
        }else {
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), TYPE_REQUEST_PERMISSION);
        }
    }

    private void checkPermission(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if( isDenied(this, permission) ) {
                    isPermissionGranted = false;

                    Toast.makeText(this, "모든 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();

                    finish();
                    overridePendingTransition(0, 0);

                    return;
                }
            }
        }

        isPermissionGranted = true;

        if( type == TYPE_TAKE_PHOTO ) {
            takePhoto();
        }else {
            pickPhoto();
        }
    }

    private boolean isDenied(Context context, @NonNull String permission) {
        return !isGranted(context, permission);
    }

    private boolean isGranted(Context context, @NonNull String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;

        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
            photoListener.onError(e);
        }

        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            startActivityForResult(intent, TYPE_TAKE_PHOTO);
        }else {
            finish();
        }
    }

    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);

        intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

        startActivityForResult(intent, TYPE_PICK_PHOTO);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,             //prefix
                ".jpg",             //suffix
                storageDir                //directory
        );

        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private void createImageThumbnailFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageThumbnailFileName = "JPEG_" + timeStamp + "_thumb";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File thumbnailImage = File.createTempFile(
                imageThumbnailFileName,             //prefix
                ".jpg",             //suffix
                storageDir                //directory
        );

        currentPhotoThumbnailPath = thumbnailImage.getAbsolutePath();
    }

    public boolean compressedBitmap(String origImagePath) {
        try {
            Bitmap scaledBitmap = null;
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inJustDecodeBounds = true;

            Bitmap bmp = BitmapFactory.decodeFile(origImagePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

            float imgRatio = isResizeThumbnail ? (float) actualHeight / (float) actualWidth : (float) actualWidth / (float) actualHeight;

            if (actualHeight > reqMaxSize || actualWidth > reqMaxSize) {
//               options.inSampleSize = calculateInSampleSize(options, (int) reqMaxSize, (int) reqMaxSize);

                // 가로 세로 중 짧은 쪽에 맞춰 사이즈 변경
                if (imgRatio < 1) {
                    if( actualWidth < reqMaxSize ) {
                        reqMaxSize = actualWidth;
                    }

                    imgRatio = reqMaxSize / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) reqMaxSize;
                } else if (imgRatio > 1) {
                    if( actualHeight < reqMaxSize ) {
                        reqMaxSize = actualHeight;
                    }

                    imgRatio = reqMaxSize / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) reqMaxSize;
                } else {
                    actualHeight = (int) reqMaxSize;
                    actualWidth = (int) reqMaxSize;
                }
            }

            if( limitSize > -1 ) {
                //사이즈 제한이 있을 경우, 가로 세로 중 긴 쪽을 기준으로 판단
                if( actualHeight > actualWidth ) {
                    if( actualHeight > limitSize ) {
                        photoListener.onFailed(SimpleRequestPhoto.HEIGHT_SIZE_EXCESS);
                        return false;
                    }
                }else {
                    if( actualWidth > limitSize ) {
                        photoListener.onFailed(SimpleRequestPhoto.WIDTH_SIZE_EXCESS);
                        return false;
                    }
                }
            }

            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            bmp = BitmapFactory.decodeFile(origImagePath, options);
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

            ExifInterface exif = new ExifInterface(origImagePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);

            Matrix matrix = new Matrix();

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }

            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

            if( isResizeThumbnail && (actualHeight > reqThumbnailSize || actualWidth > reqThumbnailSize) ) {
                int x = 0;
                int y = 0;

                int width = scaledBitmap.getWidth();
                int height = scaledBitmap.getHeight();

                if(width > reqMaxSize) {
                    x = (int) ((width-reqMaxSize)/2);
                }

                if(height > reqMaxSize) {
                    y = (int) ((height-reqMaxSize)/2);
                }

                if( width < reqMaxSize || height < reqMaxSize ) {
                    reqMaxSize = height > width ? width : height;
                }

                scaledBitmap = Bitmap.createBitmap(scaledBitmap, x, y, (int) reqMaxSize, (int) reqMaxSize);
            }

            if( currentPhotoPath == null ) {
                createImageFile();
            }

            FileOutputStream out = new FileOutputStream(currentPhotoPath);

            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, reqQuality, out);

            out.close();

            return reqThumbnailSize > -1 ? compressedBitmapThumbnail(currentPhotoPath) : true;
        }catch (OutOfMemoryError e) {
            e.printStackTrace();
            photoListener.onError(e);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            photoListener.onError(e);
        }catch (IOException e){
            e.printStackTrace();
            photoListener.onError(e);
        }catch (Exception e) {
            e.printStackTrace();
            photoListener.onError(e);
        }

        return false;
    }

    public boolean compressedBitmapThumbnail(String origImagePath) {
        try {
            createImageThumbnailFile();

            Bitmap cropBitmap = null;
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inJustDecodeBounds = true;

            Bitmap bmp = BitmapFactory.decodeFile(origImagePath, options);

            float actualHeight = options.outHeight;
            float actualWidth = options.outWidth;

            if( actualWidth > actualHeight ) {
                if( actualHeight > reqThumbnailSize ) {
                    float ratio = actualWidth / actualHeight;

                    actualHeight = (int) reqThumbnailSize;
                    actualWidth = (int) (ratio * reqThumbnailSize);
                }
            }else {
                if( actualWidth > reqThumbnailSize ) {
                    float ratio = actualHeight / actualWidth;

                    actualWidth = (int) reqThumbnailSize;
                    actualHeight = (int) (ratio * reqThumbnailSize);
                }
            }

            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            bmp = BitmapFactory.decodeFile(origImagePath, options);
            bmp = Bitmap.createScaledBitmap(bmp, (int) actualWidth, (int) actualHeight, false);

            if( actualHeight <= reqThumbnailSize && actualWidth <= reqThumbnailSize ) {
                cropBitmap = bmp;
            }else {
                int x = 0;
                int y = 0;

                if(actualWidth > reqThumbnailSize) {
                    x = (int) ((actualWidth-reqThumbnailSize)/2);
                }

                if(actualHeight > reqThumbnailSize) {
                    y = (int) ((actualHeight-reqThumbnailSize)/2);
                }

                if( actualHeight < reqThumbnailSize || actualWidth < reqThumbnailSize ) {
                    reqThumbnailSize = actualHeight > actualWidth ? actualWidth : actualHeight;
                }

                cropBitmap = Bitmap.createBitmap(bmp, x, y, (int) reqThumbnailSize, (int) reqThumbnailSize);
            }

            FileOutputStream out = new FileOutputStream(currentPhotoThumbnailPath);

            cropBitmap.compress(Bitmap.CompressFormat.JPEG, reqThumbnailQuality < 0 ? reqQuality : reqThumbnailQuality, out);

            out.close();

            return true;
        }catch (IOException e) {
            e.printStackTrace();
            photoListener.onError(e);
        }catch (Exception e) {
            e.printStackTrace();
            photoListener.onError(e);
        }

        return false;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getImageRealPathFromURI(Uri contentUri) {
        String path = "";

        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();

        path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));

        cursor.close();

        return path;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        checkPermission(permissions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TYPE_TAKE_PHOTO:
                if( resultCode == RESULT_OK ) {
                    if( reqMaxSize > -1 ) {
                        if( compressedBitmap(currentPhotoPath) ) {
                            if( isResizeThumbnail ) {
                                photoListener.onSelectedPhoto(null, currentPhotoPath);
                            }else {
                                photoListener.onSelectedPhoto(currentPhotoPath, currentPhotoThumbnailPath);
                            }
                        }
                    }else {
                        photoListener.onSelectedPhoto(currentPhotoPath, null);
                    }
                }

                break;

            case TYPE_PICK_PHOTO:
                if( resultCode == RESULT_OK ) {
                    String path = getImageRealPathFromURI(data.getData());

                    if( reqMaxSize > -1 ) {
                        if( compressedBitmap(path) ) {
                            if( isResizeThumbnail ) {
                                photoListener.onSelectedPhoto(null, currentPhotoPath);
                            }else {
                                photoListener.onSelectedPhoto(currentPhotoPath, currentPhotoThumbnailPath);
                            }
                        }
                    }else {
                        photoListener.onSelectedPhoto(path, null);
                    }
                }

                break;
        }

        finish();
        overridePendingTransition(0, 0);
    }
}