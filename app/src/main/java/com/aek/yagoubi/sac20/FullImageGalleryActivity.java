package com.aek.yagoubi.sac20;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FullImageGalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_gallery);

        ImageView imageFullScreen = (ImageView) findViewById(R.id.imageFullScreen);

        Intent intent = getIntent();
        String fileName =  intent.getStringExtra("fileName");
        try {
            File f = new File(Environment.getExternalStorageDirectory()
                    + "/dir",fileName);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            imageFullScreen.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageFullScreen.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
