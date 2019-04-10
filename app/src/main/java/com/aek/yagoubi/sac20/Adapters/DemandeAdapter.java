package com.aek.yagoubi.sac20.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aek.yagoubi.sac20.ArticleActivity;
import com.aek.yagoubi.sac20.Database;
import com.aek.yagoubi.sac20.DemandeActivity;
import com.aek.yagoubi.sac20.DemandeActivity2;
import com.aek.yagoubi.sac20.Object.Article;
import com.aek.yagoubi.sac20.Object.Client;
import com.aek.yagoubi.sac20.Object.Demande;
import com.aek.yagoubi.sac20.Object.Sac;
import com.aek.yagoubi.sac20.R;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;

import java.util.ArrayList;

public class DemandeAdapter extends ArrayAdapter<Demande> {

    Context mContext;
    Database database;

    public DemandeAdapter(Context context, ArrayList<Demande> demandes) {
        super(context, 0, demandes);
        this.mContext = context;
        database = new Database(mContext);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_demande, parent, false);
        }

        final Demande demande = getItem(position);

        ArrayList<Sac> sacs = new ArrayList<>();
        if(sacs.size() == 0)
         sacs = database.getSacs(demande.getId());




        Client client = database.getClient(demande.getId_client());
        Client client_final = database.getClient(demande.getId_client_final());

        TextView list_item_demande_nom_client_nom = (TextView) listItemView.findViewById(R.id.list_item_demande_nom_client_nom);
        TextView list_item_demande_nom_client_final = (TextView) listItemView.findViewById(R.id.list_item_demande_nom_client_final);
        TextView list_item_demande_description = (TextView) listItemView.findViewById(R.id.list_item_demande_description);
        final RadioButton list_item_livre_Checkbox = (RadioButton) listItemView.findViewById(R.id.list_item_livre_Checkbox);
         LinearLayout sac_container = (LinearLayout) listItemView.findViewById(R.id.sac_container);
        TextView list_item_demande_prix_total = (TextView) listItemView.findViewById(R.id.list_item_demande_prix_total);
        TextView list_item_demande_paiement = (TextView) listItemView.findViewById(R.id.list_item_demande_paiement);


        list_item_demande_nom_client_nom.setText("Client " + client.getName());
        if(client.getName().equals(client_final.getName())  &&
                client.getTele().equals(client_final.getTele())){
            list_item_demande_nom_client_final.setVisibility(View.GONE);
        }else{
            list_item_demande_nom_client_final.setText("Client finale " + client_final.getName());
        }


        if (demande.getDescription().length() >  0 )
            list_item_demande_description.setText("Descrription :" + demande.getDescription());
        else
            list_item_demande_description.setVisibility(View.GONE);

        final  boolean isLivre = database.isLivre(demande.getId());

        list_item_livre_Checkbox.setChecked(isLivre);


        list_item_livre_Checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = !database.isLivre(demande.getId());
                list_item_livre_Checkbox.setChecked(isChecked);
                boolean update = database.toggleLivre(demande.getId(),isChecked);
                if(update){
                    if(isChecked){
                        Toast.makeText(mContext, "Livré", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });





        int prixTotale  = 0;

       if(sac_container.getChildCount() > 0)
           sac_container.removeAllViews();
        for (int i = 0; i< sacs.size(); i++)
        {
            Article article = database.getArticle(sacs.get(i).getId_article());

            prixTotale += (article.getPrix() * sacs.get(i).getQte());
            TextView articleNameTextView = new TextView(mContext);

            articleNameTextView.setText(sacs.get(i).getQte() + " " + article.getName());

            articleNameTextView.setTextAppearance(mContext, android.R.style.TextAppearance_DeviceDefault_Medium);

            sac_container.addView(articleNameTextView);
        }

        list_item_demande_prix_total.setText("Prix Totale : " + prixTotale);
        list_item_demande_paiement.setText("Paiement  : " + demande.getPaiement());


        LinearLayout mySacItemLinearLyout = (LinearLayout) listItemView.findViewById(R.id.mySacItemLinearLyout);
        if (demande.getPaiement() >= prixTotale) {
            mySacItemLinearLyout.setBackgroundColor(Color.argb(50, 0, 100, 0));
        }else{
            mySacItemLinearLyout.setBackgroundColor(Color.rgb(250, 250, 250));
        }


        mySacItemLinearLyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DemandeActivity2.class);
                intent.putExtra("demande_id",demande.getId());
                mContext.startActivity(intent);
                ((Activity) mContext).finish();




                    }
        });



/*
        Article article = database.getArticle(demande.getId_article());

        Client client = database.getClient(demande.getId_client());
        Client client_final = database.getClient(demande.getId_client_final());

        TextView list_item_demande_article_nom = (TextView) listItemView.findViewById(R.id.list_item_demande_article_nom);
        TextView list_item_demande_nom_client = (TextView) listItemView.findViewById(R.id.list_item_demande_nom_client);
        TextView list_item_demande_nom_client_final = (TextView) listItemView.findViewById(R.id.list_item_demande_nom_client_final);
        TextView list_item_demande_type = (TextView) listItemView.findViewById(R.id.list_item_demande_type);
        TextView list_item_demande_prix = (TextView) listItemView.findViewById(R.id.list_item_demande_prix);
        TextView list_item_demande_qte = (TextView) listItemView.findViewById(R.id.list_item_demande_qte);
        TextView list_item_demande_prix_total = (TextView) listItemView.findViewById(R.id.list_item_demande_prix_total);
        TextView list_item_demande_paiement = (TextView) listItemView.findViewById(R.id.list_item_demande_paiement);
        TextView list_item_demande_description = (TextView) listItemView.findViewById(R.id.list_item_demande_description);
        final ImageView showContentBtn = (ImageView) listItemView.findViewById(R.id.showContentBtn);
        final ExpandableLinearLayout content = (ExpandableLinearLayout) listItemView.findViewById(R.id.content);

        final RadioButton list_item_livre_Checkbox = (RadioButton) listItemView.findViewById(R.id.list_item_livre_Checkbox);


       // list_item_demande_article_nom.setText(article.getName() + demande.getDate());
        list_item_demande_nom_client.setText("Client : " + client.getName());
        list_item_demande_nom_client_final.setText("Client finale: " + client_final.getName());
        list_item_demande_type.setText("Type :" + article.getType());
        list_item_demande_prix.setText("Prix :" + demande.getPrix());
        list_item_demande_qte.setText("Quantité :" + demande.getQte());
        list_item_demande_prix_total.setText("Prix Totale : " + (demande.getQte() * demande.getPrix()));
        list_item_demande_paiement.setText("Paiement  : " + demande.getPaiement());
        if (demande.getDescription().length() >  0 )
            list_item_demande_description.setText("Descrription :" + demande.getDescription());
        else
            list_item_demande_description.setVisibility(View.GONE);

        LinearLayout mySacItemLinearLyout = (LinearLayout) listItemView.findViewById(R.id.mySacItemLinearLyout);
        if (demande.getPaiement() >= (demande.getQte() * demande.getPrix())) {
            mySacItemLinearLyout.setBackgroundColor(Color.argb(50, 0, 100, 0));
        }else{
            mySacItemLinearLyout.setBackgroundColor(Color.rgb(250, 250, 250));
        }




        String article_ids_text  = demande.getArticle_ids();
        String[]  article_ids  = article_ids_text.split(",");
        for (int i = 0; i < article_ids.length;i++){
            if(article_ids[i].length() > 0){
                Article article1 = database.getArticle(Integer.parseInt(article_ids[i]));
                list_item_demande_article_nom.setText(list_item_demande_article_nom.getText().toString() + article1.getName());
            }
        }
        list_item_demande_article_nom.setPaintFlags(list_item_demande_article_nom.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        list_item_demande_article_nom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ArticleActivity.class);
                intent.putExtra("article_id",demande.getId_article());
                mContext.startActivity(intent);
            }
        });

        showContentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(content.isExpanded()){
                   showContentBtn.setImageResource(R.drawable.ic_arrow_up_black);

                }else{
                    showContentBtn.setImageResource(R.drawable.ic_arrow_down_black);

                }
                content.toggle();

            }
        });

        mySacItemLinearLyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DemandeActivity.class);
                intent.putExtra("demande_id",demande.getId());
                mContext.startActivity(intent);
            }
        });
       final  boolean isLivre = database.isLivre(demande.getId());

        list_item_livre_Checkbox.setChecked(isLivre);


        list_item_livre_Checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = !database.isLivre(demande.getId());
                list_item_livre_Checkbox.setChecked(isChecked);
                boolean update = database.toggleLivre(demande.getId(),isChecked);
                if(update){
                    if(isChecked){
                        Toast.makeText(mContext, "Livré", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

*/
        return listItemView;
    }
}
