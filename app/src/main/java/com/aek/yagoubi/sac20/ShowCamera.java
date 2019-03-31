package com.aek.yagoubi.sac20;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {

    Camera camera;
    SurfaceHolder holder;

    Context mcontext;
    public ShowCamera(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        this.mcontext = context;
        holder = getHolder();
        holder.addCallback(this);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {




       Camera.Parameters params = camera.getParameters();

       List<Camera.Size> sizes = params.getSupportedPictureSizes();
        Camera.Size mSize = null;

        for(Camera.Size size :  sizes){
            mSize = size;
        }



       if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            params.set("orientation", "portrait");
            camera.setDisplayOrientation(90);
            params.setRotation(90);
        } else {
            params.set("orientation", "landscape");
            camera.setDisplayOrientation(0);
            params.setRotation(0);
        }
        mSize = getOptimalSize(sizes,2048,1232);



        Toast.makeText(mcontext,"width :" + mSize.width + " ,height : "+ mSize.height,Toast.LENGTH_LONG).show();
      params.setPictureSize(mSize.width,mSize.height);
        camera.setParameters(params);

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
    }


    private Camera.Size getOptimalSize(List<Camera.Size> sizes, int w, int h) {

        final double ASPECT_TOLERANCE = 0.2;
        double targetRatio = (double) w / h;
        if (sizes == null)
            return null;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes)
        {
//          Log.d("CameraActivity", "Checking size " + size.width + "w " + size.height + "h");
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff)
            {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        // Cannot find the one match the aspect ratio, ignore the requirement

        if (optimalSize == null)
        {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff)
                {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        SharedPreferences previewSizePref;
       // if (Camera. == Camera.CameraInfo.CAMERA_FACING_BACK) {
            previewSizePref = mcontext.getSharedPreferences("PREVIEW_PREF",mcontext.MODE_PRIVATE);
      /*  } else {
            previewSizePref = mcontext.getSharedPreferences("FRONT_PREVIEW_PREF",mcontext.MODE_PRIVATE);
        }*/

        SharedPreferences.Editor prefEditor = previewSizePref.edit();
        prefEditor.putInt("width", optimalSize.width);
        prefEditor.putInt("height", optimalSize.height);

        prefEditor.commit();

//      Log.d("CameraActivity", "Using size: " + optimalSize.width + "w " + optimalSize.height + "h");
        return optimalSize;
    }
}
