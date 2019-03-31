package com.aek.yagoubi.sac20;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aek.yagoubi.sac20.Object.Client;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;

public class ClientActivity extends AppCompatActivity {

    EditText edit_text_modifier_le_nom_client,edit_text_modifier_le_tele_client;
    ImageView btnClose;

    boolean isSave = true;
    Database database;
    String lat,lon;
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);


        TextView text_view_modiefier_geo = (TextView) findViewById(R.id.text_view_modiefier_geo);
        ImageView image_view_modifier_le_geo_client = (ImageView) findViewById(R.id.image_view_modifier_le_geo_client);
        FloatingActionButton map_client_btn = (FloatingActionButton) findViewById(R.id.map_client_btn);


        Intent intent = getIntent();
        final int client_id = intent.getIntExtra("client_id",-1);


        database = new Database(this);
        edit_text_modifier_le_nom_client = (EditText)findViewById(R.id.edit_text_modifier_le_nom_client);
        edit_text_modifier_le_tele_client = (EditText)findViewById(R.id.edit_text_modifier_le_tele_client);


        final Client client = getClient(client_id);

        lat = client.getLat();
        lon = client.getLon();
        if (client != null){


            if(!isServivesOk()  ){
                text_view_modiefier_geo.setVisibility(View.GONE);
                image_view_modifier_le_geo_client.setVisibility(View.GONE);
                map_client_btn.setVisibility(View.GONE);
            }

            if( lat.equals("")  && lon.equals("") ||( lat == null && lon == null)){
                map_client_btn.setVisibility(View.GONE);
            }
            //
            edit_text_modifier_le_nom_client.setText(client.getName());
            edit_text_modifier_le_tele_client.setText(client.getTele());



        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
            finish();
        }

        btnClose = (ImageView)findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (isSave){
                   finish();
               }else{
                   final AlertDialog.Builder builder = new AlertDialog.Builder(ClientActivity.this);

                   builder.setMessage("enregistrer les Modification")
                           .setTitle("Enregistrer");
                   builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                            isSave =  saveTheChange(client_id);

                           if(isSave){
                               finish();
                           }else{
                               Toast.makeText(ClientActivity.this, "Error", Toast.LENGTH_SHORT).show();
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



        //Delete client
        FloatingActionButton delete_client_btn = (FloatingActionButton) findViewById(R.id.delete_client_btn);

        delete_client_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ClientActivity.this);

                builder.setMessage("Suprimer :" + client.getName())
                        .setTitle("Suprimer");
                builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean b = database.deleteClientByHisId(client.getId());
                        if (b)
                            finish();
                        else
                            Toast.makeText(ClientActivity.this, "Error", Toast.LENGTH_LONG).show();
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


        map_client_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lon + " (" + client.getName() + ")";


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
               startActivity(intent);
            }
        });

        image_view_modifier_le_geo_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientActivity.this, MapsActivity.class);
                startActivityForResult(intent,123);
            }
        });
        //call client

        FloatingActionButton call_client_btn = (FloatingActionButton) findViewById(R.id.call_client_btn);

        if (client.getTele().equals("")) {
            call_client_btn.setVisibility(View.GONE);
        } else {
            call_client_btn.setVisibility(View.VISIBLE);
        }

        call_client_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = String.format("tel: %s",
                        client.getTele());
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                // Set the data for the intent as the phone number.
                dialIntent.setData(Uri.parse(phoneNumber));
                // If package resolves to an app, send intent.
                if (dialIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(dialIntent);
                } else {
                    Log.e("Tag", "Can't resolve app for ACTION_DIAL Intent.");
                }
            }
        });


        //update client
        FloatingActionButton update_client_information = (FloatingActionButton) findViewById(R.id.update_client_information);

        update_client_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 isSave =  saveTheChange(client_id);
                if (isSave) {
                    //refresh
                    //refresh
                    if (android.os.Build.VERSION.SDK_INT >= 11) {

                        recreate();

                    } else {
                        finish();
                    }



                } else {
                    Toast.makeText(ClientActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private boolean saveTheChange(int id) {
        String name = edit_text_modifier_le_nom_client.getText().toString();
        String Tele = edit_text_modifier_le_tele_client.getText().toString();

        boolean b = database.updateClientInformation(id, name, Tele,lat,lon);
        return b;

    }

    private Client getClient(int client_id) {

        Database database = new Database(this);

        return database.getClient(client_id);



    }

    public boolean isServivesOk(){
        Log.d("ClientActivity","check google services is OK");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(ClientActivity.this);

        if(available == ConnectionResult.SUCCESS){
            // OK 200
            Log.d("ClientActivity","google services working");
            return true;

        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Log.d("ClientActivity", "an error occured but we can fixed");

            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(ClientActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this, "You can't make map request", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 123) {
            if(resultCode == Activity.RESULT_OK){
                String lat_r = data.getStringExtra("lat");
                String lon_r = data.getStringExtra("lon");
                if(lat_r != null && lon_r != null){
                    lat = lat_r;
                    lon = lon_r;
                    FloatingActionButton map_client_btn = (FloatingActionButton) findViewById(R.id.map_client_btn);
                    map_client_btn.setVisibility(View.GONE);

                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        isSave = true;
        return super.onKeyUp(keyCode, event);
    }
}