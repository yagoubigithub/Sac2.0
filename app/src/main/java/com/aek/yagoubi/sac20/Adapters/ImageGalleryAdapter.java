package com.aek.yagoubi.sac20.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.aek.yagoubi.sac20.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ImageGalleryAdapter extends ArrayAdapter<String> {
private Context context;


        ArrayList<String> fileNames;

public ImageGalleryAdapter(Context context, ArrayList<String> fileNames) {
        super(context, 0, fileNames);
        this.context = context;
        this.fileNames = fileNames;
        }
@Override
public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
        listItemView = LayoutInflater.from(getContext()).inflate(
        R.layout.image_item_gallerie, parent, false);
        }
        String imgPath = getItem(position);

        ImageView imageView = (ImageView)listItemView.findViewById(R.id.image_gallerie);
        try {
        File f = new File(Environment.getExternalStorageDirectory()
        + "/dir",imgPath);
        if(f != null){
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                imageView.setImageBitmap(b);
        }

        } catch (FileNotFoundException e) {

        e.printStackTrace();
        }

        return listItemView;
        }
        }
