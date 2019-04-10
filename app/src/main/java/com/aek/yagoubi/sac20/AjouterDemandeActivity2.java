package com.aek.yagoubi.sac20;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aek.yagoubi.sac20.Object.Article;
import com.aek.yagoubi.sac20.Object.Client;
import com.aek.yagoubi.sac20.Object.Sac;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;

public class AjouterDemandeActivity2 extends AppCompatActivity {


    Button select_article_btn, select_client_btn, select_client_finale_btn, btnAjouterDemande;

    Database database;
    Client client_final, client;


    boolean isPayee = false;

    TextView client_tetxView, client_finale_textView;
    ArrayList<Sac> sacs;

    LinearLayout articles_container;

    int id_client_final;
    int id_client;
    private ArrayList<Article> articles;

    String description;


    EditText editText_paiement, editText_description;
     CheckBox pyeeCheckbox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_demande2);

        database = new Database(this);

        sacs = new ArrayList<>();
        select_article_btn = (Button) findViewById(R.id.select_article_btn);
        select_client_btn = (Button) findViewById(R.id.select_client_btn);
        select_client_finale_btn = (Button) findViewById(R.id.select_client_finale_btn);


        pyeeCheckbox = (CheckBox) findViewById(R.id.pyeeCheckbox);

        editText_description = (EditText) findViewById(R.id.editText_description);
        editText_paiement = (EditText) findViewById(R.id.editText_paiement);


        client_tetxView = (TextView) findViewById(R.id.client_tetxView);
        client_finale_textView = (TextView) findViewById(R.id.client_finale_textView);


        articles_container = (LinearLayout) findViewById(R.id.articles_container);


        // Ajouter Article
        select_article_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AjouterDemandeActivity2.this, SelectFromListDesArticlesActivity.class);
                startActivityForResult(intent, 456);
            }
        });


        //Ajouter Client

        select_client_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AjouterDemandeActivity2.this, SelectFromListDesClientActivity.class);
                startActivityForResult(intent, 789);
            }
        });


        //Ajouter Client  finale

        select_client_finale_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AjouterDemandeActivity2.this, SelectFromListDesClientActivity.class);
                startActivityForResult(intent, 123);
            }
        });

        pyeeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){


                    if(sacs.size() != 0){
                        int paiement = 0;
                        for (int i = 0; i < sacs.size(); i ++){
                            Article article  = database.getArticle(sacs.get(i).getId_article());

                            paiement += article.getPrix() * sacs.get(i).getQte();
                        }
                        isPayee = true;


                        editText_paiement.setText(paiement + "");

                    }else{
                        Toast.makeText(AjouterDemandeActivity2.this, "Sélectonner un Article svp", Toast.LENGTH_LONG).show();
                        pyeeCheckbox.setChecked(false);
                        return;
                    }
                }else{
                    isPayee = false;
                    editText_paiement.setText(0 + "");
                }
            }
        });
        btnAjouterDemande = (Button) findViewById(R.id.btnAjouterDemande);

        btnAjouterDemande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                description = editText_description.getText().toString();

                if (client == null) {
                    Toast.makeText(AjouterDemandeActivity2.this, "Sélectonner une client svp", Toast.LENGTH_LONG).show();
                    return;
                }


                if (client_final == null) {

                    id_client_final = id_client;
                }

                if (sacs.size() == 0) {
                    Toast.makeText(AjouterDemandeActivity2.this, "Sélectonner un Article svp", Toast.LENGTH_LONG).show();
                    return;
                }

                int paiement_int;
                try {

                    paiement_int = 0;
                    if(isPayee){
                        for (int i = 0; i < sacs.size(); i ++){
                            Article article  = database.getArticle(sacs.get(i).getId_article());

                            paiement_int += article.getPrix() * sacs.get(i).getQte();
                        }
                    }


                } catch (NumberFormatException e) {
                    Toast.makeText(AjouterDemandeActivity2.this, "paiement doit être un nombre svp", Toast.LENGTH_LONG).show();
                    return;
                }

                Date date = new Date();


                boolean isSave = database.AjouterDemande2(id_client, id_client_final ,sacs, description, paiement_int, date.toString());
                if (isSave) {

                    Toast.makeText(AjouterDemandeActivity2
                            .this, "Demande enregistré avec succès : " + paiement_int, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(AjouterDemandeActivity2
                            .this, "Error", Toast.LENGTH_LONG).show();
                }

            }});

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 456) {
            ArrayList<String> article_ids = database.getArticlesIdInTemps();

            if (article_ids.size() > 0) {
                Toast.makeText(this, ".." + article_ids.size(), Toast.LENGTH_SHORT).show();
                boolean delteTemps = database.deleteArticlesIdInTemps();
                if (delteTemps) {
                    articles = new ArrayList<>();

                    for (int i = 0; i < article_ids.size(); i++) {
                        try {
                            int article_id = Integer.parseInt(article_ids.get(i));
                            Article article = database.getArticle(article_id);
                            AddArticle(article);
                            articles.add(article);

                        } catch (NumberFormatException ex) {
                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        }

        // client result
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


        //client finale result
        if (requestCode == 123) {
            try {
                int client_id = data.getIntExtra("client_id", -1);

                client_final = database.getClient(client_id);

                id_client_final = client_id;

                if (client_final != null) {

                    client_finale_textView.setText("Client finale : " + client_final.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();

            }


        }
    }

    private void AddArticle(final Article article) {


        final Sac sac = new Sac(article.getId(),1);
        sacs.add(sac);

        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.article_item_and_close_btn, null, false);

        TextView articleNameTextView = (TextView) layout.findViewById(R.id.article_name_text_view);


        ImageView closeBtn = (ImageView) layout.findViewById(R.id.closeBtn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove sac
                ((ViewGroup) layout.getParent()).removeView(layout);

                articles.remove(article);
                 sacs.remove(sac);





            }
        });


        final EditText qte_edit_text = (EditText) layout.findViewById(R.id.edit_text_save_article_qte);
        ImageView remove_qte_imageView = (ImageView) layout.findViewById(R.id.remove_qte_imageView);
        ImageView add_qte_imageView = (ImageView) layout.findViewById(R.id.add_qte_imageView);

        remove_qte_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int qte = Integer.parseInt(qte_edit_text.getText().toString());
                    if (qte > 1) {
                        qte--;

                        addQte(sac,qte);
                        qte_edit_text.setText(qte + "");
                    }


                } catch (NumberFormatException ex) {

                    Log.d("AjouterDemandeActivity2", ex.getMessage());
                }


            }
        });

        add_qte_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int qte = Integer.parseInt(qte_edit_text.getText().toString());
                    qte++;
                    addQte(sac,qte);
                    qte_edit_text.setText(qte + "");


                } catch (NumberFormatException ex) {

                    Log.d("AjouterDemandeActivity2", ex.getMessage());
                }


            }
        });

        articleNameTextView.setText(article.getName());

        articles_container.addView(layout);


    }

    private void addQte(Sac sac, int qte) {

        for (int i = 0;i < sacs.size(); i++){
            if(sacs.get(i).getId_article() == sac.getId_article()){
                sacs.get(i).setQte(qte);
            }
        }


    }
}
