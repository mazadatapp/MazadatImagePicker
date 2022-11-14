package com.mazadatimagepicker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.FileCallback;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Flash;

import java.io.File;
import java.util.Date;

public class MyCameraActivity extends AppCompatActivity {

    ConstraintLayout preprocessing_cl, image_cl;
    TextView title_tv;
    CameraView cameraView;
    RectangleHole rectangleHole;

    ImageView photo_im;
    boolean isBack = true;
    boolean flashOn = false;
    Bitmap capturedBitmap;

    int aspect_ratio_x = 0;
    int aspect_ratio_y = 0;
    String title;
    int angle = 0;
    int step = 0;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera);
        cameraView = findViewById(R.id.camera);
        title_tv = findViewById(R.id.title_tv);
        rectangleHole=findViewById(R.id.rectangle_hole);

        title = getIntent().getExtras().getString("Title");
        aspect_ratio_x = getIntent().getExtras().getInt("AspectRatioX");
        aspect_ratio_y = getIntent().getExtras().getInt("AspectRatioY");

        title_tv.setText(title);
        rectangleHole.setAspectRatio(aspect_ratio_x,aspect_ratio_y);

        preprocessing_cl = findViewById(R.id.preprocessing_cl);

        photo_im = findViewById(R.id.photo_im);
        image_cl = findViewById(R.id.image_cl);

        findViewById(R.id.flip_im).setOnClickListener(v -> flipCamera());
        findViewById(R.id.flash_im).setOnClickListener(v -> flashCamera());
        findViewById(R.id.capture_im).setOnClickListener(v -> capturePhoto());
        findViewById(R.id.next_tv).setOnClickListener(v -> nextPressed());
        findViewById(R.id.reset_tv).setOnClickListener(v -> resetPhoto());

        // captured_image_im.setOnTouchListener(new ImageMatrixTouchHandler(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.open();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraView.stopVideo();
        cameraView.close();
    }

    public void flipCamera() {
        isBack = !isBack;
        cameraView.setFacing(isBack ? Facing.BACK : Facing.FRONT);
    }

    public void nextPressed() {
        finish();

        Intent intent=new Intent(this, EditActivity.class);
        intent.putExtra("from", "camera");
        intent.putExtra("path", path);
        intent.putExtra("Title",title);
        intent.putExtra("AspectRatioX",aspect_ratio_x);
        intent.putExtra("AspectRatioY",aspect_ratio_y);
        startActivity(intent);
        step = 2;
    }

    public void resetPhoto() {
        step = 0;
        cameraView.open();
        preprocessing_cl.setVisibility(View.VISIBLE);
        image_cl.setVisibility(View.GONE);
    }


    public void flashCamera() {
        flashOn = !flashOn;
        cameraView.setFlash(flashOn ? Flash.TORCH : Flash.OFF);
    }

    public void capturePhoto() {
        cameraView.takePicture();
        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                File file = new File(getCacheDir(), new Date().getTime() + ".png");
                result.toFile(file, new FileCallback() {
                    @Override
                    public void onFileReady(@Nullable File file) {
                        path = file.getPath();
                        Log.i("datadata", file.getPath());
                    }
                });
                result.toBitmap(1024, 768, new BitmapCallback() {
                    @Override
                    public void onBitmapReady(@Nullable Bitmap bitmap) {
                        capturedBitmap = bitmap;
                        cameraView.stopVideo();
                        cameraView.close();
                        preprocessing_cl.setVisibility(View.GONE);
                        image_cl.setVisibility(View.VISIBLE);
                        photo_im.setImageBitmap(bitmap);
                    }
                });
            }
        });

    }


}
