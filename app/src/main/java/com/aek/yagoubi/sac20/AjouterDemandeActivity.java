package com.aek.yagoubi.sac20;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aek.yagoubi.sac20.Object.Article;
import com.aek.yagoubi.sac20.Object.Client;

import java.util.ArrayList;
import java.util.Date;

public class AjouterDemandeActivity extends AppCompatActivity {


    Button select_article_btn, select_client_btn, select_client_finale_btn, btnAjouterDemande;
    Article article;
    Client client, client_final;
    Database database;
    TextView article_name_text_view, client_tetxView, client_finale_textView;
    EditText edit_text_save_article_qte, editText_paiement, editText_description;
    ImageView add_qte_imageView, remove_qte_imageView;
    CheckBox pyeeCheckbox;
    int qte = 1;
    String article_ids_text;
    ArrayList<Article> articles;

    boolean isPayee = false;

    int id_client, id_client_finale, id_article;
    String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_demande);

        articles= new ArrayList<>();

        database = new Database(this);
        select_article_btn = (Button) findViewById(R.id.select_article_btn);
        select_client_btn = (Button) findViewById(R.id.select_client_btn);
        select_client_finale_btn = (Button) findViewById(R.id.select_client_finale_btn);
        pyeeCheckbox = (CheckBox) findViewById(R.id.pyeeCheckbox);
        article_name_text_view = (TextView) findViewById(R.id.article_name_text_view);


        edit_text_save_article_qte = (EditText) findViewById(R.id.edit_text_save_article_qte);
        editText_description = (EditText) findViewById(R.id.editText_description);
        editText_paiement = (EditText) findViewById(R.id.editText_paiement);


        client_finale_textView = (TextView) findViewById(R.id.client_finale_textView);
        client_tetxView = (TextView) findViewById(R.id.client_tetxView);


        add_qte_imageView = (ImageView) findViewById(R.id.add_qte_imageView);
        remove_qte_imageView = (ImageView) findViewById(R.id.remove_qte_imageView);

        remove_qte_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qte > 1) {
                    qte--;
                    //setText
                    edit_text_save_article_qte.setText(qte + "");
                    if (isPayee){
                        if(article != null){
                            int paiement = article.getPrix() * qte;

                            editText_paiement.setText(paiement + "");

                        }
                    }
                }
            }
        });

        add_qte_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qte++;
                //setText
                edit_text_save_article_qte.setText(qte + "");
                if(isPayee){
                    if(article != null){
                        int paiement = article.getPrix() * qte;

                        editText_paiement.setText(paiement + "");

                    }
                }


            }
        });


        select_article_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AjouterDemandeActivity.this, SelectFromListDesArticlesActivity.class);
                startActivityForResult(intent, 456);
            }
        });


        select_client_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AjouterDemandeActivity.this, SelectFromListDesClientActivity.class);
                startActivityForResult(intent, 789);
            }
        });


        select_client_finale_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AjouterDemandeActivity.this, SelectFromListDesClientActivity.class);
                startActivityForResult(intent, 123);
            }
        });


        btnAjouterDemande = (Button) findViewById(R.id.btnAjouterDemande);
        btnAjouterDemande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                description = editText_description.getText().toString();

                if (client == null) {
                    Toast.makeText(AjouterDemandeActivity.this, "Sélectonner une client svp", Toast.LENGTH_LONG).show();
                    return;
                }


                if (client_final == null) {

                    id_client_finale = id_client;
                }

                if (article == null) {
                    Toast.makeText(AjouterDemandeActivity.this, "Sélectonner un Article svp", Toast.LENGTH_LONG).show();
                    return;
                }
                //saveTheChange
                int paiement_int;
                try {

                    paiement_int = Integer.parseInt(editText_paiement.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(AjouterDemandeActivity.this, "paiement doit être un nombre svp", Toast.LENGTH_LONG).show();
                    return;
                }

                Date date = new Date();

                if(article_ids_text.length() > 0){
                    boolean isSave = database.AjouterDemande(id_client, id_client_finale, id_article, description, qte, paiement_int, date.toString(),article_ids_text);
                    if (isSave) {

                        Toast.makeText(AjouterDemandeActivity
                                .this, "Demande enregistré avec succès : " + paiement_int, Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(AjouterDemandeActivity
                                .this, "Error", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(AjouterDemandeActivity.this, "Sélectonner un Article svp", Toast.LENGTH_LONG).show();
                    return;
                }



            }
        });

        pyeeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){


                    if(article != null){
                        int paiement = article.getPrix() * qte;

                        editText_paiement.setText(paiement + "");
                        isPayee = true;
                    }else{
                        Toast.makeText(AjouterDemandeActivity.this, "Sélectonner un Article svp", Toast.LENGTH_LONG).show();
                        pyeeCheckbox.setChecked(false);
                        return;
                    }
                }else{
                    editText_paiement.setText(0 + "");
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 456) {

            try {
                ArrayList<String> article_ids = database.getArticlesIdInTemps();

                if(article_ids.size() > 0){
                    Toast.makeText(this, ".." + article_ids.size(), Toast.LENGTH_SHORT).show();
                    boolean delteTemps = database.deleteArticlesIdInTemps();
                    if(delteTemps){
                        article_name_text_view.setText("");
                      for (int i = 0; i < article_ids.size();i++){
                          try {
                              int article_id = Integer.parseInt(article_ids.get(i));
                              Article article = database.getArticle(article_id);
                              article_ids_text += "," +  article_id;
                              articles.add(article);
                              article_name_text_view.setText(article_name_text_view.getText().toString() +  " " +
                                      article.getName());

                          }catch (NumberFormatException ex){
                              Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                          }

                      }
                    }
                }
                int article_id = data.getIntExtra("article_id", -1);

                article = database.getArticle(article_id);
                id_article = article_id;

                if (article != null) {
                   // article_name_text_view.setText(article.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        if (requestCode == 789) {
            try {
                int client_id = data.getIntExtra("client_id", -1);

                client = database.getClient(client_id);

                id_client = client_id;

                if (client != null) {

                    client_tetxView.setText("Client : " + client.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (requestCode == 123) {
            try {
                int client_id = data.getIntExtra("client_id", -1);

                client_final = database.getClient(client_id);

                id_client_finale = client_id;

                if (client_final != null) {

                    client_finale_textView.setText("Client finale : " + client_final.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();

            }


        }
    }
}
