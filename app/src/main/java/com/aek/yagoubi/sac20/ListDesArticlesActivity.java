package com.aek.yagoubi.sac20;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.aek.yagoubi.sac20.Adapters.ArticleAdapter;
import com.aek.yagoubi.sac20.Adapters.ClientAdapter;
import com.aek.yagoubi.sac20.Object.Article;
import com.aek.yagoubi.sac20.Object.Client;
import com.aek.yagoubi.sac20.com.google.zxing.integration.android.IntentIntegrator;
import com.aek.yagoubi.sac20.com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ListDesArticlesActivity extends AppCompatActivity {
    ListView list_view_articles_in_ListDesArticlesActivity;
    ArticleAdapter adapter;
    ArrayList<Article> articles;
    Database database;
    CheckBox see_all_checkbox;
    EditText serch_input_list_des_articles;
    FloatingActionButton showAjouterArticleBtn;

    Button codeBareBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_des_articles);

        see_all_checkbox = (CheckBox) findViewById(R.id.see_all_checkbox);
        serch_input_list_des_articles = (EditText) findViewById(R.id.serch_input_list_des_articles);

        database = new Database(this);
        articles = new ArrayList<>();
        articles = database.getAllArticles();

        list_view_articles_in_ListDesArticlesActivity = (ListView) findViewById(R.id.list_view_articles_in_ListDesArticlesActivity);
        showAjouterArticleBtn = (FloatingActionButton) findViewById(R.id.showAjouterArticleBtn);

        adapter = new ArticleAdapter(this, articles);

        list_view_articles_in_ListDesArticlesActivity.setAdapter(adapter);

        see_all_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seeAll();
                }
            }
        });

        serch_input_list_des_articles.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Article> newArraySearch = new ArrayList<>();

                if (s == null || s.length() == 0) {
                    adapter = new ArticleAdapter(ListDesArticlesActivity.this, articles);
                } else {
                    see_all_checkbox.setChecked(false);
                    for (Article a : articles) {
                        if (a.getName().toUpperCase().contains(s.toString().toUpperCase())) {
                            Log.i("Filter", a.getName());

                            newArraySearch.add(a);
                        }
                    }
                    adapter = new ArticleAdapter(ListDesArticlesActivity.this, newArraySearch);
                }
                adapter.notifyDataSetChanged();
                list_view_articles_in_ListDesArticlesActivity.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        showAjouterArticleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDesArticlesActivity.this, AjouterArticleActivity.class);
                startActivity(intent);
                finish();
            }
        });


        codeBareBtn = (Button) findViewById(R.id.codeBareBtn);

        codeBareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(ListDesArticlesActivity.this);
                scanIntegrator.initiateScan();
            }
        });


    }

    private void seeAll() {

        list_view_articles_in_ListDesArticlesActivity = (ListView) findViewById(R.id.list_view_articles_in_ListDesArticlesActivity);

        adapter = new ArticleAdapter(this, articles);

        list_view_articles_in_ListDesArticlesActivity.setAdapter(adapter);

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            if (scanContent != null && scanFormat != null) {

                ArrayList<Article> newArraySearch = new ArrayList<>();

                see_all_checkbox.setChecked(false);
                for (Article a : articles) {
                    if (a.getCodebare().equals(scanContent) && a.getCodebareFormat().equals(scanFormat)) {
                        Log.i("Filter", a.getName());

                        newArraySearch.add(a);
                    }
                }


                adapter = null;
                adapter = new ArticleAdapter(this, newArraySearch);
                list_view_articles_in_ListDesArticlesActivity = (ListView) findViewById(R.id.list_view_articles_in_ListDesArticlesActivity);


                list_view_articles_in_ListDesArticlesActivity.setAdapter(adapter);

            }

        } else {
            Toast toast = Toast.makeText(this,
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}
