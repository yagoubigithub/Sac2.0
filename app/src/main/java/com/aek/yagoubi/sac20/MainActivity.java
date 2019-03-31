package com.aek.yagoubi.sac20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LinearLayout btn_list_des_articles,ajouter_articles_btn,btn_ajouter_client,btn_list_des_clients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_list_des_articles = (LinearLayout) findViewById(R.id.btn_list_des_articles);


        btn_list_des_articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ListDesArticlesActivity.class);
                startActivity(intent);
            }
        });


        ajouter_articles_btn = (LinearLayout) findViewById(R.id.ajouter_articles_btn);

        ajouter_articles_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AjouterArticleActivity.class);
                startActivity(intent);
            }
        });

        btn_ajouter_client = (LinearLayout) findViewById(R.id.btn_ajouter_client);

        btn_ajouter_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AjouterClientActivity.class);
                startActivity(intent);
            }
        });

        btn_list_des_clients = (LinearLayout) findViewById(R.id.btn_list_des_clients);

        btn_list_des_clients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ListDesClientActivity.class);
                startActivity(intent);
            }
        });

    }
}
