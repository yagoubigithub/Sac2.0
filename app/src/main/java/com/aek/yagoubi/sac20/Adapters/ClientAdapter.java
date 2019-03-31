package com.aek.yagoubi.sac20.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aek.yagoubi.sac20.ClientActivity;
import com.aek.yagoubi.sac20.Object.Client;
import com.aek.yagoubi.sac20.R;

import java.util.ArrayList;

public class ClientAdapter  extends ArrayAdapter<Client> {


     Context myContext;
    public ClientAdapter(Context context, ArrayList<Client> clients) {
        super(context, 0,clients);
        this.myContext = context;
    }


    @SuppressLint("RestrictedApi")
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {


        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_client, parent, false);
        }

        final Client client = getItem(position);

        TextView list_item_nom_client = (TextView) listItemView.findViewById(R.id.list_item_nom_client);
        TextView list_item_num_client = (TextView) listItemView.findViewById(R.id.list_item_num_client);
        FloatingActionButton list_item_btn_call = (FloatingActionButton) listItemView.findViewById(R.id.list_item_btn_call);
        FloatingActionButton list_item_btn_maps = (FloatingActionButton) listItemView.findViewById(R.id.list_item_btn_maps);

        list_item_nom_client.setText(client.getName());
        list_item_num_client.setText(client.getTele());

        if(client.getTele().equals("")){
            list_item_btn_call.setVisibility(View.GONE);
        }else{
            list_item_btn_call.setVisibility(View.VISIBLE);
        }



        if(client.getLat().equals("") || client.getLon().equals("")){
            list_item_btn_maps.setVisibility(View.GONE);
        }else{
            list_item_btn_maps.setVisibility(View.VISIBLE);
        }

        list_item_btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the intent.

                String phoneNumber = String.format("tel: %s",
                        client.getTele());
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                // Set the data for the intent as the phone number.
                dialIntent.setData(Uri.parse(phoneNumber));
                // If package resolves to an app, send intent.
                if (dialIntent.resolveActivity(myContext.getPackageManager()) != null) {
                    myContext.startActivity(dialIntent);
                } else {
                    Log.e("Tag", "Can't resolve app for ACTION_DIAL Intent.");
                }


            }
        });
        list_item_btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + client.getLat() + "," + client.getLon() + " (" + client.getName() + ")";


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
               myContext.startActivity(intent);
            }
        });


        LinearLayout myClientItemLinearLayout = (LinearLayout)listItemView.findViewById(R.id.myClientItemLinearLayout);



        myClientItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myContext,ClientActivity.class);
                intent.putExtra("client_id",client.getId());
                myContext.startActivity(intent);
            }
        });


        return listItemView;


    }
}
