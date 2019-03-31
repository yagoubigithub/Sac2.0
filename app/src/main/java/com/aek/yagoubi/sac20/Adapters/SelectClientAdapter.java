package com.aek.yagoubi.sac20.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.aek.yagoubi.sac20.ClientActivity;
import com.aek.yagoubi.sac20.Object.Client;
import com.aek.yagoubi.sac20.R;

import java.util.ArrayList;

public class SelectClientAdapter extends ArrayAdapter<Client> {


    Context myContext;
    public SelectClientAdapter(Context context, ArrayList<Client> clients) {
        super(context, 0,clients);
        this.myContext = context;
    }


    @SuppressLint("RestrictedApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_select_client, parent, false);
        }

        final Client client = getItem(position);

        TextView list_item_nom_client = (TextView) listItemView.findViewById(R.id.list_item_nom_client);
        TextView list_item_num_client = (TextView) listItemView.findViewById(R.id.list_item_num_client);

        list_item_nom_client.setText(client.getName());
        list_item_num_client.setText(client.getTele());

        RadioButton radioButton_1 = (RadioButton)listItemView.findViewById(R.id.radioButton_1);

        radioButton_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("client_id",client.getId());
                    ((Activity)  myContext).setResult( ((Activity)  myContext).RESULT_OK,returnIntent);
                    ((Activity)  myContext).finish();
                }
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
