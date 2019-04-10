package com.aek.yagoubi.sac20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.aek.yagoubi.sac20.Object.Article;
import com.aek.yagoubi.sac20.Object.Client;
import com.aek.yagoubi.sac20.Object.Demande;
import com.aek.yagoubi.sac20.Object.Sac;

import java.io.File;
import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {


    public Database(Context context) {
        super(context, "sac_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table client (id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "name TEXT , tele TEXT,lat TEXT , lon TEXT)");

        //article
        db.execSQL("create table  article (id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "name TEXT ,type TEXT, codebare TEXT ,codeBareFormat TEXT, prix INTEGER)");


        //demandee

        db.execSQL("create table  demande (ID INTEGER PRIMARY KEY AUTOINCREMENT ,id_client INTEGER, " +
                "id_client_final INTEGER , description TEXT, " +
                "  Paiement INTEGER,livre INTEGER,date TEXT)");



        db.execSQL("create table  sac (id INTEGER PRIMARY KEY AUTOINCREMENT , id_demande INTEGER," +
                "id_article INTEGER, qte INTEGER )");

        db.execSQL("create table  images (id INTEGER PRIMARY KEY AUTOINCREMENT , id_article INTEGER," +
                "filename TEXT )");

        db.execSQL("create table  temps (id INTEGER PRIMARY KEY AUTOINCREMENT , tempText TEXT)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "client");
        db.execSQL("DROP TABLE IF EXISTS " + "article");
        db.execSQL("DROP TABLE IF EXISTS " + "demande");
        db.execSQL("DROP TABLE IF EXISTS " + "images");
        onCreate(db);
    }


    public boolean AjouterArticle(String name, String type, int prix, String codebare, String codeBareFormat, ArrayList<String> images) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("type", type);

        contentValues.put("codebare", codebare);
        contentValues.put("codeBareFormat", codeBareFormat);
        contentValues.put("prix", prix);


        long result = db.insert("article", null, contentValues);

        if (result != -1) {
            for (int i = 0; i < images.size(); i++) {
                ContentValues newContent = new ContentValues();

                newContent.put("id_article", result);
                newContent.put("filename", images.get(i));

                db.insert("images", null, newContent);
            }
            return true;
        } else {
            return false;
        }


    }

    public ArrayList<Article> getAllArticles() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Article> articles = new ArrayList<>();
        Cursor res = db.rawQuery("SELECT * FROM article", null);
        while (res.moveToNext()) {
            articles.add(new Article(res.getInt(0), res.getString(1), res.getString(2), res.getString(3),
                    res.getString(4),
                    res.getInt(5)));


        }
        return articles;
    }


    public boolean AjouterClient(String name, String tele, String lat, String lon) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("tele", tele);
        contentValues.put("lat", lat);
        contentValues.put("lon", lon);


        long result = db.insert("client", null, contentValues);

        return result != -1;

    }


    public ArrayList<Client> getAllClients() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Client> clients = new ArrayList<>();
        Cursor res = db.rawQuery("SELECT * FROM client", null);
        while (res.moveToNext()) {
            clients.add(new Client(res.getInt(0), res.getString(1), res.getString(2),
                    res.getString(3), res.getString(4)));


        }
        return clients;
    }

    public Client getClient(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Client client = null;
        Cursor res = db.rawQuery("SELECT * FROM client WHERE id=" + id, null);
        while (res.moveToNext()) {
            client = new Client(res.getInt(0), res.getString(1), res.getString(2),
                    res.getString(3), res.getString(4));


        }
        return client;
    }

    public boolean updateClientInformation(int id, String name, String tele, String lat, String lon) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("tele", tele);
        cv.put("lat", lat);
        cv.put("lon", lon);
        return db.update("client", cv, "id=" + id, null) > 0;


    }

    public boolean deleteClientByHisId(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("client", "id=?", new String[]{String.valueOf(id)}) > 0
                || db.delete("demande", "id_client=?", new String[]{String.valueOf(id)}) > 0;


    }

    public ArrayList<String> getImagesByArticleId(int id_article) {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<String> images = new ArrayList<>();
        Cursor res = db.rawQuery("SELECT filename FROM images WHERE id_article=" + id_article, null);
        while (res.moveToNext()) {
            images.add(res.getString(0));
        }
        return images;
    }

    //deleteArticleyHisId
    public boolean deleteArticleyHisId(int id) {

        SQLiteDatabase db = this.getWritableDatabase();


        ArrayList<String> fileNames = getImagesByArticleId(id);
        for (int i = 0; i < fileNames.size(); i++) {
            File file = new File(Environment.getExternalStorageDirectory()
                    + "/dir", fileNames.get(i));
            file.delete();
        }


        return db.delete("article", "id=?", new String[]{String.valueOf(id)}) > 0
                || db.delete("images", "id_article=?", new String[]{String.valueOf(id)}) > 0;


    }

    public Article getArticle(int id) {
        if (id == -1)
            return null;
        SQLiteDatabase db = this.getWritableDatabase();
        Article article = null;
        Cursor res = db.rawQuery("SELECT * FROM article WHERE id=" + id, null);
        while (res.moveToNext()) {
            article = new Article(res.getInt(0), res.getString(1), res.getString(2),
                    res.getString(3), res.getString(4), res.getInt(5));


        }
        return article;
    }

    public boolean updateArticleInformation(int id, String name, String type, String codebare, String codebareFormat, int prix, ArrayList<String> fileNames) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("type", type);
        cv.put("codebare", codebare);
        cv.put("codeBareFormat", codebareFormat);
        cv.put("prix", prix);

        long result = db.update("article", cv, "id=" + id, null);
        if (result > 0) {
            for (int i = 0; i < fileNames.size(); i++) {
                String filename = fileNames.get(i);
                ContentValues newContent = new ContentValues();

                newContent.put("id_article", id);
                newContent.put("filename", filename);

                db.insert("images", null, newContent);
            }
        }
        return result > 0;


    }


    public boolean AjouterDemande(int id_client, int id_client_finale, int id_article, String description, int qte, int paiement_int, String date, String  article_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("id_client", id_client);
        contentValues.put("id_client_final", id_client_finale);
        contentValues.put("id_article", id_article);
        contentValues.put("description", description);
        contentValues.put("qte", qte);
        contentValues.put("Paiement", paiement_int);
        contentValues.put("livre", 0);
        contentValues.put("date", date);
        contentValues.put("article_ids", article_ids);


        long result = db.insert("demande", null, contentValues);

        return result != -1;

    }



    public ArrayList<Demande> getAllExtraDeamdes() {
        ArrayList<Demande> demandes = new ArrayList<>();


        SQLiteDatabase db = this.getWritableDatabase();


        //D INTEGER PRIMARY KEY AUTOINCREMENT ,id_client INTEGER, " +
        //                "id_client_final INTEGER , description TEXT, " +
        //                "  Paiement INTEGER,livre INTEGER,date TEXT
        Cursor res = db.rawQuery("SELECT * FROM demande", null);
        while (res.moveToNext()) {
            demandes.add(new Demande(
                    res.getInt(0),
                    res.getInt(1),
                    res.getInt(2),
                    res.getString(3),
                    res.getInt(4),
                    res.getInt(5),
                    res.getString(6)


            ));


        }
        return demandes;
    }



    public Demande getDemande(int id) {
        Demande demande = null;

        SQLiteDatabase db = this.getWritableDatabase();


        //D INTEGER PRIMARY KEY AUTOINCREMENT ,id_client INTEGER, " +
        //                "id_client_final INTEGER , description TEXT, " +
        //                "  Paiement INTEGER,livre INTEGER,date TEXT
        Cursor res = db.rawQuery("SELECT * FROM demande WHERE id="+id, null);
        while (res.moveToNext()) {
            demande = new Demande(
                    res.getInt(0),
                    res.getInt(1),
                    res.getInt(2),
                    res.getString(3),
                    res.getInt(4),
                    res.getInt(5),
                    res.getString(6)


            );


        }
        return demande;
    }

    public boolean isLivre(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int l = 0;
        Cursor res = db.rawQuery("SELECT livre FROM demande WHERE id=" + id, null);
        while (res.moveToNext()) {
            l = res.getInt(0);
        }
        return l != 0;
    }


    public boolean toggleLivre(int id, boolean isChecked) {
        if (isChecked) {
            //1

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("livre", 1);

            return db.update("demande", cv, "id=" + id, null) > 0;
        } else {
            //0
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("livre", 0);

            return db.update("demande", cv, "id=" + id, null) > 0;
        }


    }





    public ArrayList<Demande> getAllExtraDeamdesByClientId(int client_id) {
        ArrayList<Demande> demandes = new ArrayList<>();


        SQLiteDatabase db = this.getWritableDatabase();


        //D INTEGER PRIMARY KEY AUTOINCREMENT ,id_client INTEGER, " +
        //                "id_client_final INTEGER , description TEXT, " +
        //                "  Paiement INTEGER,livre INTEGER,date TEXT
        Cursor res = db.rawQuery("SELECT * FROM demande WHERE id_client=" + client_id, null);
        while (res.moveToNext()) {
            demandes.add(new Demande(
                    res.getInt(0),
                    res.getInt(1),
                    res.getInt(2),
                    res.getString(3),
                    res.getInt(4),
                    res.getInt(5),
                    res.getString(6)


            ));


        }
        return demandes;
    }


    public boolean deleteDemandeByHisId(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete("demande", "id=?", new String[]{String.valueOf(id)}) > 0;


    }

    public boolean setArticleIdInTemps(int article_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("tempText", article_id);



        long result = db.insert("temps", null, contentValues);

        return result != -1;
    }


    public ArrayList<String> getArticlesIdInTemps() {


        SQLiteDatabase db = this.getWritableDatabase();
       ArrayList arrayList  = new ArrayList();
        Cursor res = db.rawQuery("SELECT * FROM temps " , null);
        while (res.moveToNext()) {
            arrayList.add(res.getString(1));
        }
        return arrayList;
    }

    public boolean deleteArticlesIdInTemps() {
        SQLiteDatabase db = this.getWritableDatabase();
        return   db.delete("temps", null, null) > 0;
    }


    public boolean AjouterDemande2(int id_client, int id_client_final, ArrayList<Sac> sacs, String description, int paiement_int,
                                   String date) {


        //,id_client INTEGER, " +
        //                "id_client_final INTEGER, description TEXT, " +
        //                " qte INTEGER, Paiement INTEGER,livre INTEGER,date TEXT
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("id_client", id_client);
        contentValues.put("id_client_final", id_client_final);

        contentValues.put("description", description);
        contentValues.put("Paiement", paiement_int);
        contentValues.put("livre", 0);
        contentValues.put("date", date);


        long result = db.insert("demande", null, contentValues);


        //(id INTEGER PRIMARY KEY AUTOINCREMENT , id_demande INTEGER," +
        //                "id_article INTEGER, qte INTEGER )");
        if (result != -1) {
            for (int i = 0; i < sacs.size(); i++) {
                ContentValues newContent = new ContentValues();

                newContent.put("id_demande", result);
                newContent.put("id_article", sacs.get(i).getId_article());
                newContent.put("qte", sacs.get(i).getQte());

                db.insert("sac", null, newContent);
            }
            return true;
        } else {
            return false;
        }

    }

    public ArrayList<Sac> getSacs(int id) {
        ArrayList<Sac> sacs = new ArrayList<>();


        SQLiteDatabase db = this.getWritableDatabase();


        Cursor res = db.rawQuery("SELECT * FROM sac  WHERE id_demande=" + id, null);
        while (res.moveToNext()) {
           sacs.add(new Sac(
                   res.getInt(0),
                   res.getInt(1),
                   res.getInt(2),
                   res.getInt(3)
                   ));


        }
        return sacs;

    }

    public boolean  deleteSac(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("sac", "id_demande=?", new String[]{String.valueOf(id)}) > 0;

    }

    public boolean updateDemande(int id , int id_client, int id_client_final, ArrayList<Sac> sacs, String description, int paiement_int) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //ID INTEGER PRIMARY KEY AUTOINCREMENT ,id_client INTEGER, " +
        //                "id_client_final INTEGER , description TEXT, " +
        //                "  Paiement INTEGER,livre INTEGER,date TEXT)"


        boolean delete = deleteSac(id);
        if(delete){
            cv.put("id_client", id_client);
            cv.put("id_client_final", id_client_final);
            cv.put("description", description);
            cv.put("Paiement", paiement_int);
           long result =  db.update("demande", cv, "id=" + id, null);

            if (result != -1) {
                for (int i = 0; i < sacs.size(); i++) {
                    ContentValues newContent = new ContentValues();

                    newContent.put("id_demande", result);
                    newContent.put("id_article", sacs.get(i).getId_article());
                    newContent.put("qte", sacs.get(i).getQte());

                    db.insert("sac", null, newContent);
                }
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }




    }
}
