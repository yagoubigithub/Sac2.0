package com.aek.yagoubi.sac20;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.aek.yagoubi.sac20.Adapters.DemandeAdapter;
import com.aek.yagoubi.sac20.Object.Article;
import com.aek.yagoubi.sac20.Object.Client;
import com.aek.yagoubi.sac20.Object.Demande;
import com.aek.yagoubi.sac20.Object.Sac;
import com.aek.yagoubi.sac20.com.google.zxing.integration.android.IntentIntegrator;
import com.aek.yagoubi.sac20.com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ListDesDemandesActivity2 extends AppCompatActivity {


    ListView list_view_demandes;

    Database database;
    ArrayList<Demande> demandes;
    EditText serch_input_list_des_demandes, serch_input_by_client_name_list_des_demandes;
    DemandeAdapter adapter;
    Button codeBareBtn;
     FloatingActionButton showAjouterDemandeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_des_demandes);


        list_view_demandes = (ListView) findViewById(R.id.list_view_demandes);

        database = new Database(this);
        demandes = database.getAllExtraDeamdes();
        adapter = new DemandeAdapter(this, demandes);
        list_view_demandes.setAdapter(adapter);

//EditText

        serch_input_by_client_name_list_des_demandes = (EditText) findViewById(R.id.serch_input_by_client_name_list_des_demandes);
        serch_input_list_des_demandes = (EditText) findViewById(R.id.serch_input_list_des_demandes);






        serch_input_by_client_name_list_des_demandes = (EditText) findViewById(R.id.serch_input_by_client_name_list_des_demandes);
        serch_input_list_des_demandes = (EditText) findViewById(R.id.serch_input_list_des_demandes);


        serch_input_list_des_demandes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //

                search();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        serch_input_by_client_name_list_des_demandes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //
                search();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        codeBareBtn = (Button) findViewById(R.id.codeBareBtn);

        codeBareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(ListDesDemandesActivity2.this);
                scanIntegrator.initiateScan();
            }
        });

        showAjouterDemandeBtn = (FloatingActionButton) findViewById(R.id.showAjouterDemandeBtn);

        showAjouterDemandeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDesDemandesActivity2.this, AjouterDemandeActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }

    private void search() {
        ArrayList<Demande> newArraySearch = new ArrayList<>();

        String search_ariticle_name = serch_input_list_des_demandes.getText().toString();
        String search_client_name = serch_input_by_client_name_list_des_demandes.getText().toString();



        if (search_ariticle_name.equals("") && search_client_name.equals("")) {
            adapter = new DemandeAdapter(ListDesDemandesActivity2.this, demandes);
        } else {

            for (Demande d : demandes) {
                boolean articleExist = false;
                ArrayList<Sac> sacs = database.getSacs(d.getId());
                for (Sac s : sacs) {
                    Article article = database.getArticle(s.getId_article());
                    if (article.getName().toUpperCase().contains(search_ariticle_name.toUpperCase())) {
                        articleExist = true;
                        break;
                    }
                }
                Client client = database.getClient(d.getId_client());
                Client client_final = database.getClient(d.getId_client_final());

                if (articleExist &&
                        client.getName().toUpperCase().contains(search_client_name.toString().toUpperCase()) &&
                        client_final.getName().toUpperCase().contains(search_client_name.toString().toUpperCase())) {
                    newArraySearch.add(d);
                }
            }
            adapter = new DemandeAdapter(ListDesDemandesActivity2.this, newArraySearch);
        }
        adapter.notifyDataSetChanged();
        list_view_demandes.setAdapter(adapter);

    }

    private void seeAll() {
        demandes = database.getAllExtraDeamdes();
        adapter = new DemandeAdapter(this, demandes);
        list_view_demandes.setAdapter(adapter);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            if (scanContent != null && scanFormat != null) {

                searchWithCodeBare(scanContent, scanFormat);

            }

        } else {
            Toast toast = Toast.makeText(this,
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void searchWithCodeBare(String scanContent, String scanFormat) {
        seeAll();
        ArrayList<Demande> newArraySearch = new ArrayList<>();


        for (Demande d : demandes) {

            ArrayList<Sac> sacs = database.getSacs(d.getId());
            for (Sac s : sacs) {
                Article article = database.getArticle(s.getId_article());
                if(article.getCodebare().equals(scanContent) && article.getCodebareFormat().equals(scanFormat)){
                    newArraySearch.add(d);
                    break;
                }



            }


        }


        adapter = new DemandeAdapter(ListDesDemandesActivity2.this, newArraySearch);
        adapter.notifyDataSetChanged();
        list_view_demandes.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.see_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.see_pyee: {
                seeAll();
                ArrayList<Demande> newArraySearch = new ArrayList<>();


                for (Demande d : demandes) {

                    int payement = d.getPaiement();
                    int prixTotal = 0;
                    ArrayList<Sac> sacs = database.getSacs(d.getId());
                    for (Sac s : sacs) {
                        Article article = database.getArticle(s.getId_article());
                        prixTotal += (article.getPrix() * s.getQte());


                    }

                    if (payement >= prixTotal) {
                        newArraySearch.add(d);
                    }
                }


                adapter = new DemandeAdapter(ListDesDemandesActivity2.this, newArraySearch);
                adapter.notifyDataSetChanged();
                list_view_demandes.setAdapter(adapter);
            }
            break;

            case R.id.see_non_pyee: {
                seeAll();
                ArrayList<Demande> newArraySearch = new ArrayList<>();


                for (Demande d : demandes) {

                    int payement = d.getPaiement();
                    int prixTotal = 0;
                    ArrayList<Sac> sacs = database.getSacs(d.getId());
                    for (Sac s : sacs) {
                        Article article = database.getArticle(s.getId_article());
                        prixTotal += (article.getPrix() * s.getQte());


                    }

                    if (payement < prixTotal) {
                        newArraySearch.add(d);
                    }
                }


                adapter = new DemandeAdapter(ListDesDemandesActivity2.this, newArraySearch);
                adapter.notifyDataSetChanged();
                list_view_demandes.setAdapter(adapter);
            }
            break;

            case R.id.see_lvre: {
                seeAll();
                ArrayList<Demande> newArraySearch = new ArrayList<>();


                for (Demande d : demandes) {

                    if (d.getLivre() == 1) {
                        newArraySearch.add(d);
                    }
                }


                adapter = new DemandeAdapter(ListDesDemandesActivity2.this, newArraySearch);
                adapter.notifyDataSetChanged();
                list_view_demandes.setAdapter(adapter);
            }
            break;

            case R.id.see_non_livre: {
                seeAll();
                ArrayList<Demande> newArraySearch = new ArrayList<>();


                for (Demande d : demandes) {

                    if (d.getLivre() != 1) {
                        newArraySearch.add(d);
                    }
                }


                adapter = new DemandeAdapter(ListDesDemandesActivity2.this, newArraySearch);
                adapter.notifyDataSetChanged();
                list_view_demandes.setAdapter(adapter);
            }break;

            case R.id.see_all: {
                seeAll();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
