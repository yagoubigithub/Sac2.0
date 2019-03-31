package com.aek.yagoubi.sac20;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class getPictureActivity extends AppCompatActivity {

    String  fileNames  = "";
    Camera camera;
    FrameLayout frameLayout;
    ShowCamera showCamera;
    ImageView CaptureBtn;
    MediaPlayer mp;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_picture);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },REQUEST_CAMERA_PERMISSION);

        }


        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        ImageView closeCameraBtn = (ImageView) findViewById(R.id.closeCameraBtn);

        closeCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("fileNames",fileNames);
                setResult(getPictureActivity.RESULT_OK,returnIntent);
                finish();
            }
        });


        //Open the Camera

        camera = Camera.open();

        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);

        CaptureBtn = (ImageView) findViewById(R.id.CaptureBtn);

        mp = MediaPlayer.create(this, R.raw.cameara_sound_capture);

        CaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null) {
                    camera.takePicture(null, null, mPictureCalback);


                }
            }
        });

    }

    Camera.PictureCallback mPictureCalback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            //Save picture

            if (data != null) {
                mp.start();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data .length);

                if(bitmap!=null){

                    File file=new File(Environment.getExternalStorageDirectory()+"/dir");
                    if(!file.isDirectory()){
                        file.mkdir();
                    }
                    String fileName = System.currentTimeMillis()+".jpg";


                    file=new File(Environment.getExternalStorageDirectory()
                            + "/dir",fileName);
                    //  Toast.makeText(getPictureActivity.this, file.getAbsolutePath(),Toast.LENGTH_LONG).show();
                    fileNames += "," + fileName;
                    try
                    {
                        FileOutputStream fileOutputStream=new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        camera.startPreview();

                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                    catch(Exception exception)
                    {
                        exception.printStackTrace();
                    }

                }
            }



        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("fileNames",fileNames);
            setResult(getPictureActivity.RESULT_OK,returnIntent);
            mp.release();
            finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
