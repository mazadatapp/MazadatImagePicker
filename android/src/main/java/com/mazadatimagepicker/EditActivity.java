package com.mazadatimagepicker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    ConstraintLayout zoom_image_cl, crop_image_cl,lottie_cl;
    ZoomableImageView zoom_image_im;
    ImageCropper crop_image_im;
    Bitmap capturedBitmap, zoomBitmap;
    TextView title_tv;
    ImageView test_im, rotate_right_im,rotate_left_im;


    int angle = 0;
    int step = 0;
    int imageAngle = 0;

    int aspect_ratio_x = 0;
    int aspect_ratio_y = 0;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);





        title = getIntent().getExtras().getString("Title");
        aspect_ratio_x = getIntent().getExtras().getInt("AspectRatioX");
        aspect_ratio_y = getIntent().getExtras().getInt("AspectRatioY");


        if (getIntent().getExtras().getString("from").equals("camera")) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Log.i("datadata_path", getIntent().getExtras().getString("path"));
            capturedBitmap = BitmapFactory.decodeFile(getIntent().getExtras().getString("path"), bmOptions);
        }else{
            Log.i("datadata_path", getIntent().getExtras().getString("path"));
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            capturedBitmap = BitmapFactory.decodeFile(getIntent().getExtras().getString("path"), bmOptions);

        }
        zoom_image_cl = findViewById(R.id.zoom_image_cl);
        crop_image_cl = findViewById(R.id.crop_image_cl);
        lottie_cl = findViewById(R.id.lottie_cl);
        zoom_image_im = findViewById(R.id.zoomImageView);
        crop_image_im = findViewById(R.id.cropImageView);
        test_im = findViewById(R.id.test_im);
        title_tv = findViewById(R.id.title_tv);

        rotate_right_im = findViewById(R.id.rotate_right_im);
        rotate_left_im = findViewById(R.id.rotate_left_im);

        title_tv.setText(title);
        crop_image_im.setImageBitmap(capturedBitmap);

        findViewById(R.id.next_tv).setOnClickListener(v -> nextPressed());
        findViewById(R.id.reset_tv).setOnClickListener(v -> resetPhoto());
        findViewById(R.id.reset2_tv).setOnClickListener(v -> resetPhoto());
        rotate_right_im.setOnClickListener(v -> rotatePhotoR());
        rotate_left_im.setOnClickListener(v -> rotatePhotoL());
        findViewById(R.id.rotate_right2_im).setOnClickListener(v -> rotatePhotoR());
        findViewById(R.id.rotate_left2_im).setOnClickListener(v -> rotatePhotoL());
        findViewById(R.id.check_im).setOnClickListener(v -> donePressed());
        findViewById(R.id.zoom_out_im).setOnClickListener(v -> createScale());
        findViewById(R.id.remove_background_im).setOnClickListener(v -> removeBackground());
        findViewById(R.id.background_rm_im).setOnClickListener(v -> removeBackground());

        crop_image_im.setAspectRatio(aspect_ratio_x, aspect_ratio_y);



        if (getIntent().getExtras().getString("from").equals("camera"))
            rotatePhotoR();



        // captured_image_im.setOnTouchListener(new ImageMatrixTouchHandler(this));
    }


    public void nextPressed() {
        step = 1;
        lottie_cl.setVisibility(View.VISIBLE);
        crop_image_cl.setVisibility(View.GONE);
        zoom_image_cl.setVisibility(View.VISIBLE);
        zoomBitmap = crop_image_im.Crop();
        zoom_image_im.setImageBitmap(zoomBitmap);

        ViewGroup.LayoutParams lottie_params = lottie_cl.getLayoutParams();
        lottie_params.height = zoom_image_cl.getWidth() * aspect_ratio_y / aspect_ratio_x;
        lottie_cl.setLayoutParams(lottie_params);

        ViewGroup.LayoutParams params = zoom_image_im.getLayoutParams();
        params.height = zoom_image_cl.getWidth() * aspect_ratio_y / aspect_ratio_x;
        zoom_image_im.setLayoutParams(params);
        Log.i("datadata_height", zoom_image_im.getHeight() + "");
        new Handler().postDelayed(() -> lottie_cl.setVisibility(View.GONE),3000);
    }

    public void removeBackground() {
        //CutOut.activity().start(this);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    public void resetPhoto() {
        step = 0;
        crop_image_cl.setVisibility(View.VISIBLE);
        zoom_image_cl.setVisibility(View.INVISIBLE);
        crop_image_im.setImageBitmap(capturedBitmap);
    }

    public void rotatePhotoR() {
        angle = 90;
        imageAngle += 90;
        if (step == 0) {
            capturedBitmap = rotateBitmap(capturedBitmap, angle);
            crop_image_im.setImageBitmap(capturedBitmap);
        } else {
            zoomBitmap = rotateBitmap(zoomBitmap, angle);
            zoom_image_im.setImageBitmap(zoomBitmap);
        }
    }

    public void rotatePhotoL() {
        angle = -90;
        imageAngle -= 90;
        if (step == 0) {
            capturedBitmap = rotateBitmap(capturedBitmap, angle);
            crop_image_im.setImageBitmap(capturedBitmap);
        } else {
            zoomBitmap = rotateBitmap(zoomBitmap, angle);
            zoom_image_im.setImageBitmap(zoomBitmap);
        }
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.TRANSPARENT);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    public void donePressed() {
        Log.i("datadata_height", zoom_image_im.getHeight() + "");
        try {
            Bitmap bitmap = getBitmapFromView(zoom_image_im);
            File f = new File(getCacheDir(), new Date().getTime() + ".png");
            f.createNewFile();

//Convert bitmap to byte array

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = null;

            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            Intent intent = new Intent();
            intent.putExtra("path", f.getPath());
            intent.setAction("data");
            sendBroadcast(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createScale() {
        rotate_right_im.setVisibility(View.GONE);
        rotate_left_im.setVisibility(View.GONE);
        Bitmap returnedBitmap = Bitmap.createBitmap(crop_image_cl.getWidth(), crop_image_cl.getWidth() * aspect_ratio_y / aspect_ratio_x, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE);
        Bitmap bitmap;
        if (capturedBitmap.getHeight() > capturedBitmap.getWidth()) {
            bitmap = Bitmap.createScaledBitmap(capturedBitmap, (int) ((float) returnedBitmap.getHeight() / (float) capturedBitmap.getHeight() * capturedBitmap.getWidth()), returnedBitmap.getHeight(), false);
            canvas.drawBitmap(bitmap, returnedBitmap.getWidth() / 2 - bitmap.getWidth() / 2, 0, new Paint());
        } else {
            bitmap = Bitmap.createScaledBitmap(capturedBitmap, returnedBitmap.getWidth(), (int) ((float) returnedBitmap.getWidth() / (float) capturedBitmap.getWidth() * capturedBitmap.getHeight()), false);
            canvas.drawBitmap(bitmap, 0, returnedBitmap.getHeight() / 2 - bitmap.getHeight() / 2, new Paint());
            Log.i("datadata", "" + (crop_image_im.getWidth()) + " " + (crop_image_im.getHeight()));
        }
        capturedBitmap=returnedBitmap;
        crop_image_im.setImageBitmap(capturedBitmap);
    }

    public Bitmap rotateBitmap(Bitmap bitmap, int angle) {
        Matrix matrix = new Matrix();
        Log.i("datadata", angle + "");
        matrix.postRotate(angle);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

        return rotatedBitmap;
    }
}
