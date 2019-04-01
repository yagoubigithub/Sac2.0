package com.aek.yagoubi.sac20.Adapters;

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
import android.widget.TextView;
import android.widget.Toast;

import com.aek.yagoubi.sac20.ArticleActivity;
import com.aek.yagoubi.sac20.Database;
import com.aek.yagoubi.sac20.DemandeActivity;
import com.aek.yagoubi.sac20.Object.Article;
import com.aek.yagoubi.sac20.Object.Client;
import com.aek.yagoubi.sac20.Object.Demande;
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

        final CheckBox list_item_livre_Checkbox = (CheckBox) listItemView.findViewById(R.id.list_item_livre_Checkbox);


        list_item_demande_article_nom.setText(article.getName());
        list_item_demande_nom_client.setText("Client : " + client.getName());
        list_item_demande_nom_client_final.setText("Client finale: " + client_final.getName());
        list_item_demande_type.setText("Type :" + article.getType());
        list_item_demande_prix.setText("Prix :" + demande.getPrix());
        list_item_demande_qte.setText("Quantité :" + demande.getQte());
        list_item_demande_prix_total.setText("Prix Totale : " + (demande.getQte() * demande.getPrix()));
        list_item_demande_paiement.setText("Paiement  : " + demande.getPaiement());
        if (demande.getDescription() != null)
            list_item_demande_description.setText("Descrription :" + demande.getDescription());

        else
            list_item_demande_description.setVisibility(View.GONE);

        LinearLayout mySacItemLinearLyout = (LinearLayout) listItemView.findViewById(R.id.mySacItemLinearLyout);
        if (demande.getPaiement() >= (demande.getQte() * article.getPrix())) {
            mySacItemLinearLyout.setBackgroundColor(Color.argb(50, 0, 100, 0));
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
        boolean isLivre = database.isLivre(demande.getId());

        list_item_livre_Checkbox.setChecked(isLivre);

        list_item_livre_Checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    boolean update = database.toggleLivre(demande.getId(), isChecked);

                    if(!update){
                        list_item_livre_Checkbox.setChecked(!isChecked);
                    }else{
                        Toast.makeText(mContext, "Error " + update, Toast.LENGTH_SHORT).show();
                    }
            }
        });
        return listItemView;
    }
}
