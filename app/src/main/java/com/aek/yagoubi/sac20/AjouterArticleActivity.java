package com.aek.yagoubi.sac20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aek.yagoubi.sac20.com.google.zxing.integration.android.IntentIntegrator;
import com.aek.yagoubi.sac20.com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class AjouterArticleActivity extends AppCompatActivity {

    Button btn_save;
    String codebare = "",codebareFormat = "",prix,name,type = "aucun type";

    ImageButton btn_ajouter_images_articles;
    ArrayList<String> fileNamesArrayList ;
Database database;

    EditText input_article_prix,input_article_name,input_article_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_article);

        fileNamesArrayList = new ArrayList<>();
        database = new Database(this);
        btn_save = (Button) findViewById(R.id.btn_save);
        input_article_prix = (EditText) findViewById(R.id.input_article_prix);
        input_article_name = (EditText) findViewById(R.id.input_article_name);
        input_article_type = (EditText) findViewById(R.id.input_article_type);
        btn_ajouter_images_articles = (ImageButton) findViewById(R.id.btn_ajouter_images_articles);

        btn_ajouter_images_articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AjouterArticleActivity.this, getPictureActivity.class);


                startActivityForResult(intent1,1);
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = input_article_name.getText().toString();
                type = input_article_type.getText().toString();
                prix = input_article_prix.getText().toString();
                if(name.equals("")){
                    Toast.makeText(AjouterArticleActivity.this,"Entre Le  Nome svp",Toast.LENGTH_LONG).show();
                    return;
                }

                if(prix.equals("")){
                    Toast.makeText(AjouterArticleActivity.this,"Entre Le  prix svp",Toast.LENGTH_LONG).show();
                    return;
                }
                boolean isSave =false;

                try {
                    int prixInteger =Integer.valueOf(prix);
                    Toast.makeText(AjouterArticleActivity.this,prixInteger + " :", Toast.LENGTH_LONG).show();

                    isSave = database.AjouterArticle(name,type,Integer.valueOf(prix),codebare,codebareFormat,fileNamesArrayList);


                }catch (NumberFormatException ex){
                    Toast.makeText(AjouterArticleActivity.this,"Le  prix doit être un nombre svp",Toast.LENGTH_LONG).show();
                    return;
                }
                if(isSave){
                   // Toast.makeText(AjouterArticleActivity.this,"Article enregistré avec succès",Toast.LENGTH_LONG).show();
                    finish();
                }



            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {

            String[] fileNamesArray =  intent.getStringExtra("fileNames").split(",");
            for (int i = 0;i <fileNamesArray.length;i++){
                if(!fileNamesArray[i].equals(""))
                fileNamesArrayList.add(fileNamesArray[i]);
            }
        }else{
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
                if(scanContent != null && scanFormat != null){
                    codebare = scanContent;
                    codebareFormat = scanFormat;
                }

            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }
}
