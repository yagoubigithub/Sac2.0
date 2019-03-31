package com.aek.yagoubi.sac20;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import com.aek.yagoubi.sac20.Adapters.ArticleAdapter;
import com.aek.yagoubi.sac20.Adapters.ClientAdapter;
import com.aek.yagoubi.sac20.Object.Article;
import com.aek.yagoubi.sac20.Object.Client;

import java.util.ArrayList;

public class ListDesArticlesActivity extends AppCompatActivity {
    ListView list_view_articles_in_ListDesArticlesActivity;
    ArticleAdapter adapter;
    ArrayList<Article> articles;
    Database database;
    CheckBox see_all_checkbox;
    EditText serch_input_list_des_demandes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_des_articles);

        see_all_checkbox = (CheckBox) findViewById(R.id.see_all_checkbox);
        serch_input_list_des_demandes = (EditText) findViewById(R.id.serch_input_list_des_demandes);

        database = new Database(this);
        articles = new ArrayList<>();
        articles = database.getAllArticles();

        list_view_articles_in_ListDesArticlesActivity = (ListView) findViewById(R.id.list_view_articles_in_ListDesArticlesActivity);

        adapter = new ArticleAdapter(this, articles);

        list_view_articles_in_ListDesArticlesActivity.setAdapter(adapter);

        see_all_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    seeAll();
                }
            }
        });

        serch_input_list_des_demandes.addTextChangedListener(new TextWatcher() {
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



    }

    private void seeAll() {

        list_view_articles_in_ListDesArticlesActivity = (ListView) findViewById(R.id.list_view_articles_in_ListDesArticlesActivity);

        adapter = new ArticleAdapter(this, articles);

        list_view_articles_in_ListDesArticlesActivity.setAdapter(adapter);

    }


    @Override
    protected void onResume() {
        seeAll();
        super.onResume();
    }
}
