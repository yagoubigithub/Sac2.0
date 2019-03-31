package com.aek.yagoubi.sac20;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import com.aek.yagoubi.sac20.Adapters.ArticleAdapter;
import com.aek.yagoubi.sac20.Adapters.SelectArticleAdapter;
import com.aek.yagoubi.sac20.Object.Article;

import java.util.ArrayList;

public class SelectFromListDesArticlesActivity extends AppCompatActivity {
    ListView list_view_articles_in_ListDesArticlesActivity;
    SelectArticleAdapter adapter;
    ArrayList<Article> articles;
    Database database;
    CheckBox see_all_checkbox;
    EditText serch_input_list_des_articles;
    FloatingActionButton showAjouterArticleBtn;

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

        adapter = new SelectArticleAdapter(this, articles);

        list_view_articles_in_ListDesArticlesActivity.setAdapter(adapter);

        see_all_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
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
                    adapter = new SelectArticleAdapter(SelectFromListDesArticlesActivity.this, articles);
                } else {
                    see_all_checkbox.setChecked(false);
                    for (Article a : articles) {
                        if (a.getName().toUpperCase().contains(s.toString().toUpperCase())) {
                            Log.i("Filter", a.getName());

                            newArraySearch.add(a);
                        }
                    }
                    adapter = new SelectArticleAdapter(SelectFromListDesArticlesActivity.this, newArraySearch);
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
                Intent intent = new Intent(SelectFromListDesArticlesActivity.this, AjouterArticleActivity.class);
                startActivity(intent);
            }
        });

    }


    private void seeAll() {

        list_view_articles_in_ListDesArticlesActivity = (ListView) findViewById(R.id.list_view_articles_in_ListDesArticlesActivity);

        adapter = new SelectArticleAdapter(this, articles);

        list_view_articles_in_ListDesArticlesActivity.setAdapter(adapter);

    }


    @Override
    protected void onResume() {
        seeAll();
        super.onResume();
    }
}