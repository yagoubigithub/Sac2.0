package com.aek.yagoubi.sac20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.aek.yagoubi.sac20.Adapters.ImageGalleryAdapter;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    Database database;
    ArrayList<String> images ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);


        Intent intent = getIntent();
        final int article_id = intent.getIntExtra("article_id",-1);



        database  = new Database(this);
        images = new ArrayList<>();
        images = database.getImagesByArticleId(article_id);



        GridView gridView = (GridView)findViewById(R.id.gridView);
        final ImageGalleryAdapter adapter = new ImageGalleryAdapter(this, images);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GalleryActivity.this,FullImageGalleryActivity.class);
                intent.putExtra("fileName",adapter.getItem(position));
                startActivity(intent);
            }
        });
        gridView.setAdapter(adapter);

    }
}
