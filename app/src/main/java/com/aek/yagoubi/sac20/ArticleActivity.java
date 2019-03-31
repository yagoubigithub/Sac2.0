package com.aek.yagoubi.sac20;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aek.yagoubi.sac20.Object.Article;
import com.aek.yagoubi.sac20.com.google.zxing.integration.android.IntentIntegrator;
import com.aek.yagoubi.sac20.com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ArticleActivity extends AppCompatActivity {

    Database database;
    TextView text_view_change_article_codebare;
    Button change_codebare_btn,see_galerie_btn;
    EditText edit_text_change_article_name,edit_text_change_article_prix,edit_text_change_article_type;
    ImageButton change_pictures_btn;
    ImageView btnClose;
    String codebare,codebareFormat;
    ArrayList<String> fileNamesArrayList;
    FloatingActionButton save_the_change_article,delete_article_btn;
    boolean isSave = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Intent intent = getIntent();
        final int article_id = intent.getIntExtra("article_id",-1);

        database = new Database(this);


        final Article article = getArticle(article_id);
        if(article != null){

            fileNamesArrayList = new ArrayList<>();

            edit_text_change_article_name =(EditText) findViewById(R.id.edit_text_change_article_name);
            edit_text_change_article_prix =(EditText) findViewById(R.id.edit_text_change_article_prix);
            edit_text_change_article_type =(EditText) findViewById(R.id.edit_text_change_article_type);
            text_view_change_article_codebare =(TextView) findViewById(R.id.text_view_change_article_codebare);
            change_codebare_btn =(Button) findViewById(R.id.change_codebare_btn);
            save_the_change_article =(FloatingActionButton) findViewById(R.id.save_the_change_article);
            delete_article_btn =(FloatingActionButton) findViewById(R.id.delete_article_btn);
            see_galerie_btn =(Button) findViewById(R.id.see_galerie_btn);
            change_pictures_btn =(ImageButton) findViewById(R.id.change_pictures_btn);
            btnClose =(ImageView) findViewById(R.id.btnClose);

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSave){
                        finish();
                    }else{
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ArticleActivity.this);

                        builder.setMessage("enregistrer les Modification")
                                .setTitle("Enregistrer");
                        builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isSave =  saveTheChange(article_id);

                                if(isSave){
                                    finish();
                                }else{
                                    Toast.makeText(ArticleActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                finish();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            });

            edit_text_change_article_name.setText(article.getName());

            edit_text_change_article_prix.setText(article.getPrix() + "");

            edit_text_change_article_type.setText(article.getType() + "");

            text_view_change_article_codebare.setText(article.getCodebare() + "");
            change_pictures_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(ArticleActivity.this, getPictureActivity.class);


                    startActivityForResult(intent1,2);
                }
            });

            change_codebare_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentIntegrator scanIntegrator = new IntentIntegrator(ArticleActivity.this);
                    scanIntegrator.initiateScan();
                }
            });



            see_galerie_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(ArticleActivity.this, GalleryActivity.class);

                    intent1.putExtra("article_id",article_id);

                    startActivity(intent1);
                }
            });

            delete_article_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //

                    final AlertDialog.Builder builder = new AlertDialog.Builder(ArticleActivity.this);

                    builder.setMessage("Suprimer :" + article.getName())
                            .setTitle("Suprimer");
                    builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean b = database.deleteArticleyHisId(article.getId());
                            if (b)
                                finish();
                            else
                                Toast.makeText(ArticleActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            save_the_change_article.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSave =  saveTheChange(article_id);
                    if (isSave) {
                        //refresh
                        //refresh
                        if (android.os.Build.VERSION.SDK_INT >= 11) {

                            recreate();

                        } else {
                            finish();
                        }



                    } else {
                        Toast.makeText(ArticleActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }else{
            finish();
        }



    }

    private Article getArticle(int article_id) {

        return database.getArticle(article_id);
    }

    private boolean saveTheChange(int id) {
        String name = edit_text_change_article_name.getText().toString();
        String prix = edit_text_change_article_prix.getText().toString();
        String type = edit_text_change_article_type.getText().toString();

        try {
            int PrixInteger = Integer.parseInt(prix);
            boolean b = database.updateArticleInformation(id, name,type,  codebare,codebareFormat,  PrixInteger, fileNamesArrayList);
            return b;
        }catch (NumberFormatException e){

            return false;
        }


    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                String[] fileNamesArray = intent.getStringExtra("fileNames").split(",");
                fileNamesArrayList.clear();
                for (int i = 0; i < fileNamesArray.length; i++) {
                    if (!fileNamesArray[i].equals(""))
                        fileNamesArrayList.add(fileNamesArray[i]);
                }
            }

        }else{
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
                if(scanContent != null && scanFormat != null){
                    codebare = scanContent;
                    codebareFormat = scanFormat;
                    text_view_change_article_codebare.setText(codebare);
                }

            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        isSave = false;
        return super.onKeyUp(keyCode, event);
    }
}
