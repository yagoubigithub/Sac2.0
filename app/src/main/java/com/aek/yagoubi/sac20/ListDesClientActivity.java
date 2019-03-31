package com.aek.yagoubi.sac20;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.aek.yagoubi.sac20.Adapters.ClientAdapter;
import com.aek.yagoubi.sac20.Object.Client;

import java.util.ArrayList;

public class ListDesClientActivity extends AppCompatActivity {

    Database database;
    ArrayList<Client> clients;
    ClientAdapter adapter;
    ListView list_view_clients;
    EditText serchInput;
    FloatingActionButton showAjouterClientBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_des_client);
        database = new Database(this);
        clients = new ArrayList<>();

        clients = database.getAllClients();

        adapter = new ClientAdapter(this, clients);

        list_view_clients = (ListView) findViewById(R.id.list_view_clients);

        list_view_clients.setAdapter(adapter);



        //Serch
        serchInput = (EditText) findViewById(R.id.edit_text_serch_client_by_name);


        serchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ArrayList<Client> newArraySearch = new ArrayList<>();

                if (s == null || s.length() == 0) {
                    adapter = new ClientAdapter(ListDesClientActivity.this, clients);
                } else {
                    for (Client c : clients) {
                        if (c.getName().toUpperCase().contains(s.toString().toUpperCase())) {
                            Log.i("Filter", c.getName());

                            newArraySearch.add(c);
                        }
                    }
                    adapter = new ClientAdapter(ListDesClientActivity.this, newArraySearch);
                }
                adapter.notifyDataSetChanged();
                list_view_clients.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        showAjouterClientBtn = (FloatingActionButton) findViewById(R.id.showAjouterClientBtn) ;

        showAjouterClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDesClientActivity.this, AjouterClientActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        clients = new ArrayList<>();

        clients = database.getAllClients();

        adapter = new ClientAdapter(this, clients);

        list_view_clients = (ListView) findViewById(R.id.list_view_clients);

        list_view_clients.setAdapter(adapter);



        super.onResume();
    }
}
