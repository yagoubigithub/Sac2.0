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
import android.widget.Toast;

import com.aek.yagoubi.sac20.Adapters.ClientAdapter;
import com.aek.yagoubi.sac20.Adapters.DemandeAdapter;
import com.aek.yagoubi.sac20.Object.Client;
import com.aek.yagoubi.sac20.Object.Demande;

import java.util.ArrayList;

public class ListDesDemandesActivity extends AppCompatActivity {

    ListView list_view_demandes;
    Database database;
    ArrayList<Demande> demandes;
    DemandeAdapter adapter;
    CheckBox see_all_checkbox,see_livre_checkbox,see_payee_checkbox;
    EditText serch_input_list_des_demandes,serch_input_by_client_name_list_des_demandes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_des_demandes);


        list_view_demandes  = (ListView) findViewById(R.id.list_view_demandes);




        database = new Database(this);


         demandes = new ArrayList<>();

         demandes = database.getAllExtraDeamdes();


         adapter = new DemandeAdapter(this,demandes);
         list_view_demandes.setAdapter(adapter);


         //EditText

        serch_input_by_client_name_list_des_demandes  =(EditText) findViewById(R.id.serch_input_by_client_name_list_des_demandes);
        serch_input_list_des_demandes  =(EditText) findViewById(R.id.serch_input_list_des_demandes);


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

        see_all_checkbox =(CheckBox) findViewById(R.id.see_all_checkbox);
        see_livre_checkbox = (CheckBox) findViewById(R.id.see_livre_checkbox);
        see_payee_checkbox = (CheckBox) findViewById(R.id.see_payee_checkbox);

        see_all_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    see_livre_checkbox.setChecked(false);
                    see_payee_checkbox.setChecked(false);
                    serch_input_list_des_demandes.setText("");
                    serch_input_by_client_name_list_des_demandes.setText("");

                 seeAll();

                }
            }
        });

        see_livre_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             if(isChecked){
                 see_payee_checkbox.setChecked(false);
                 see_all_checkbox.setChecked(false);
                 serch_input_list_des_demandes.setText("");
                 serch_input_by_client_name_list_des_demandes.setText("");

                 seeAll();
                 ArrayList<Demande> newArraySearch = new ArrayList<>();
                 newArraySearch.clear();
                 for (Demande d : demandes) {
                     if (d.getLivre() == 1) {


                         newArraySearch.add(d);
                     }
                 }
                 adapter = new DemandeAdapter(ListDesDemandesActivity.this, newArraySearch);
                 adapter.notifyDataSetChanged();
                 list_view_demandes.setAdapter(adapter);


             }else{
                 seeAll();
             }
            }
        });

        see_payee_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    see_livre_checkbox.setChecked(false);
                    see_all_checkbox.setChecked(false);
                    serch_input_list_des_demandes.setText("");
                    serch_input_by_client_name_list_des_demandes.setText("");

                    ArrayList<Demande> newArraySearch = new ArrayList<>();
                    newArraySearch.clear();
                    for (Demande d : demandes) {

                        if (d.getPaiement() >= (d.getPrix() * d.getQte())) {
                            Log.d("LisDesDemandeActivity", d.getPaiement() + "==="  + (d.getPrix() * d.getQte()));
                            newArraySearch.add(d);
                        }
                    }
                    adapter = new DemandeAdapter(ListDesDemandesActivity.this, newArraySearch);
                    adapter.notifyDataSetChanged();
                    list_view_demandes.setAdapter(adapter);


                }else{
                    seeAll();
                }
            }
        });







    }

    private void seeAll() {

        database = new Database(this);


        demandes = new ArrayList<>();

        demandes = database.getAllExtraDeamdes();


        adapter = new DemandeAdapter(this,demandes);
        list_view_demandes.setAdapter(adapter);
    }

    private void search() {
        ArrayList<Demande> newArraySearch = new ArrayList<>();

        String search_ariticle_name = serch_input_list_des_demandes.getText().toString();
        String search_client_name = serch_input_by_client_name_list_des_demandes.getText().toString();

        see_livre_checkbox.setChecked(false);
        see_all_checkbox.setChecked(false);
        see_payee_checkbox.setChecked(false);


        if (search_ariticle_name.equals("") &&  search_client_name.equals("")){
            adapter = new DemandeAdapter(ListDesDemandesActivity.this, demandes);
        }else{
            for (Demande d : demandes) {
                if (d.getArticle_name().toUpperCase().contains(search_ariticle_name.toString().toUpperCase())  &&
                        ( d.getClinet_name().toUpperCase().contains(search_client_name.toString().toUpperCase()) ||
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


    }

    @Override
    protected void onResume() {
        seeAll();
        super.onResume();
    }
}
