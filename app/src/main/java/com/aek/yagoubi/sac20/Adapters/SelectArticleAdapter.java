package com.aek.yagoubi.sac20.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.aek.yagoubi.sac20.ArticleActivity;
import com.aek.yagoubi.sac20.Object.Article;
import com.aek.yagoubi.sac20.R;
import com.aek.yagoubi.sac20.getPictureActivity;

import java.util.ArrayList;

public class SelectArticleAdapter extends ArrayAdapter<Article> {

    Context myContext;

    public SelectArticleAdapter(Context context, ArrayList<Article> articles) {
        super(context, 0, articles);
        this.myContext =context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.select_article_list_item, parent, false);
        }

        final Article article = getItem(position);


        TextView textView_name = (TextView) listItemView.findViewById(R.id.name_article_list_item);
        TextView textView_prix = (TextView) listItemView.findViewById(R.id.prix_article_list_item);

        textView_name.setText(article.getName());

        textView_prix.setText("Prix :"  + article.getPrix() + " £");


        LinearLayout myArticleLinearItem = (LinearLayout)listItemView.findViewById(R.id.myArticleLinearItem);



        myArticleLinearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myContext,ArticleActivity.class);
                intent.putExtra("article_id",article.getId());
                myContext.startActivity(intent);
            }
        });

        RadioButton radioButton = (RadioButton)listItemView.findViewById(R.id.radioButton);

        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("article_id",article.getId());
                    ((AppCompatActivity)  myContext).setResult( ((AppCompatActivity)  myContext).RESULT_OK,returnIntent);
                    ((AppCompatActivity)  myContext).finish();
                }
            }
        });
        return listItemView;

    }
}
