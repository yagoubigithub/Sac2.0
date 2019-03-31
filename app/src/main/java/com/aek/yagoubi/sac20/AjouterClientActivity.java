package com.aek.yagoubi.sac20;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aek.yagoubi.sac20.Object.Client;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class AjouterClientActivity extends AppCompatActivity {

    private static final String TAG = "AjouterClientActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    EditText input_ajouter_client_name,input_ajouter_client_tele;
    Button btn_save;
    String lat = "",lon = "";
    Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_client);

        if(isServivesOk()){

            init();
        }else{
            Button mapbtn = (Button) findViewById(R.id.mapbtn);
            mapbtn.setVisibility(View.GONE);
        }

    }
    private void init() {
        database = new Database(this);
        Button mapbtn = (Button) findViewById(R.id.mapbtn);
        mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AjouterClientActivity.this, MapsActivity.class);
                startActivityForResult(intent,123);
            }
        });


        input_ajouter_client_tele = (EditText) findViewById(R.id.input_ajouter_client_tele);
        input_ajouter_client_name = (EditText) findViewById(R.id.input_ajouter_client_name);
        btn_save = (Button) findViewById(R.id.save_btn);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = input_ajouter_client_name.getText().toString();
                String tele = input_ajouter_client_tele.getText().toString();
                if(name.length() > 0){

                    boolean isSave = database.AjouterClient(name, tele,lat,lon);
                    if(isSave){
                        Toast.makeText(AjouterClientActivity.this,"Client enregistré avec succès",Toast.LENGTH_LONG).show();
                        finish();
                    }




                }else{
                    Toast.makeText(AjouterClientActivity.this,"Le nom svp",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean isServivesOk(){
        Log.d(TAG,"check google services is OK");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(AjouterClientActivity.this);

        if(available == ConnectionResult.SUCCESS){
            // OK 200
            Log.d(TAG,"google services working");
            return true;

        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Log.d(TAG, "an error occured but we can fixed");

            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(AjouterClientActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this, "You can't make map request", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 123) {
            if(resultCode == Activity.RESULT_OK){
                String lat_r = data.getStringExtra("lat");
                String lon_r = data.getStringExtra("lon");
               if(lat_r != null && lon_r != null){
                   lat = lat_r;
                   lon = lon_r;

               }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
