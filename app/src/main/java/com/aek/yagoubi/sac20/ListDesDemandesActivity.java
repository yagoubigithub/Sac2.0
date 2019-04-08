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
import com.aek.yagoubi.sac20.Adapters.DemandeAdapter;
import com.aek.yagoubi.sac20.Object.Article;
import com.aek.yagoubi.sac20.Object.Client;
import com.aek.yagoubi.sac20.Object.Demande;
import com.aek.yagoubi.sac20.com.google.zxing.integration.android.IntentIntegrator;
import com.aek.yagoubi.sac20.com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ListDesDemandesActivity extends AppCompatActivity {

    ListView list_view_demandes;

    Database database;
    ArrayList<Demande> demandes;
    DemandeAdapter adapter;
    CheckBox see_all_checkbox, see_livre_checkbox, see_payee_checkbox;
    EditText serch_input_list_des_demandes, serch_input_by_client_name_list_des_demandes;
    FloatingActionButton showAjouterDemandeBtn;

    Button codeBareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_des_demandes);


        list_view_demandes = (ListView) findViewById(R.id.list_view_demandes);

        showAjouterDemandeBtn = (FloatingActionButton) findViewById(R.id.showAjouterDemandeBtn);

        showAjouterDemandeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDesDemandesActivity.this, AjouterDemandeActivity.class);
                startActivity(intent);
                finish();
            }
        });


        database = new Database(this);


        demandes = new ArrayList<>();

        demandes = database.getAllExtraDeamdes();


        adapter = new DemandeAdapter(this, demandes);
        list_view_demandes.setAdapter(adapter);


        //EditText

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

        //checkboxs




        codeBareBtn = (Button) findViewById(R.id.codeBareBtn);

        codeBareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(ListDesDemandesActivity.this);
                scanIntegrator.initiateScan();
            }
        });


    }

    private void seeAll() {

        database = new Database(this);


        demandes = new ArrayList<>();

        demandes = database.getAllExtraDeamdes();


        adapter = new DemandeAdapter(this, demandes);
        list_view_demandes.setAdapter(adapter);
    }

    private void search() {
        ArrayList<Demande> newArraySearch = new ArrayList<>();

        String search_ariticle_name = serch_input_list_des_demandes.getText().toString();
        String search_client_name = serch_input_by_client_name_list_des_demandes.getText().toString();

        see_livre_checkbox.setChecked(false);
        see_all_checkbox.setChecked(false);
        see_payee_checkbox.setChecked(false);
/*
        if (search_ariticle_name.equals("") && search_client_name.equals("")) {
            adapter = new DemandeAdapter(ListDesDemandesActivity.this, demandes);
        } else {

            for (Demande d : demandes) {
                if (d.getArticle_name().toUpperCase().contains(search_ariticle_name.toString().toUpperCase()) &&
                        (d.getClinet_name().toUpperCase().contains(search_client_name.toString().toUpperCase()) ||
                                d.getClient_name_final().toUpperCase().contains(search_client_name.toString().toUpperCase())
                        )) {
                    Log.i("Filter", d.getArticle_name());

                    newArraySearch.add(d);
                }
            }
            adapter = new DemandeAdapter(ListDesDemandesActivity.this, newArraySearch);
        }
        adapter.notifyDataSetChanged();
        list_view_demandes.setAdapter(adapter);
*/

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
        ArrayList<Demande> newArraySearch = new ArrayList<>();
        list_view_demandes = (ListView) findViewById(R.id.list_view_demandes);
        see_all_checkbox.setChecked(false);
        see_livre_checkbox.setChecked(false);
        see_payee_checkbox.setChecked(false);

        serch_input_list_des_demandes.setText("");
        serch_input_by_client_name_list_des_demandes.setText("");

        for (Demande d : demandes) {
            if (d.getCodebare().equals(scanContent) && d.getCodebareFormat().equals(scanFormat)) {
                newArraySearch.add(d);
            }
        }

        adapter = new DemandeAdapter(this, newArraySearch);

        adapter.notifyDataSetChanged();
        list_view_demandes.setAdapter(adapter);

    }
}
