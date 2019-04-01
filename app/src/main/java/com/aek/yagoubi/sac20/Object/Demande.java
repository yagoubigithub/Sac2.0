package com.aek.yagoubi.sac20.Object;

public class Demande {
    // demande (ID INTEGER PRIMARY KEY AUTOINCREMENT , " +
    //                "id_client INTEGER,id_client_final INTEGER, id_article INTEGER  , description TEXT,  qte INTEGER, Paiement INTEGER,livre INTEGER)
    private int id;
    private int id_client;
    private String clinet_name;
    private int id_client_final;
    private String client_name_final;
    private int id_article;
    private String article_name;
    private String description;
    private int qte;
    private int paiement;
   private int prix;
    private int livre;
    private String codebare;
    private String codebareFormat;






    public Demande(int id, int id_client, String clinet_name, int id_client_final, String client_name_final,
                   int id_article, String article_name, String description, int qte, int livre,int paiement,int prix,
                    String codebare, String codebareFormat) {
        this.id = id;
        this.id_client = id_client;
        this.clinet_name = clinet_name;
        this.id_client_final = id_client_final;
        this.client_name_final = client_name_final;
        this.id_article = id_article;
        this.article_name = article_name;
        this.description = description;
        this.qte = qte;
        this.paiement = paiement;
        this.prix = prix;
        this.livre = livre;
        this.codebare = codebare;
        this.codebareFormat = codebareFormat;
    }

    public int getPrix() {
        return prix;
    }

    public String getClient_name_final() {
        return client_name_final;
    }

    public String getArticle_name() {
        return article_name;
    }

    public String getCodebare() {
        return codebare;
    }

    public String getCodebareFormat() {
        return codebareFormat;
    }

    public String getClinet_name() {
        return clinet_name;
    }

    public int getId() {
        return id;
    }

    public int getId_client() {
        return id_client;
    }

    public int getId_client_final() {
        return id_client_final;
    }

    public int getId_article() {
        return id_article;
    }

    public String getDescription() {
        return description;
    }

    public int getQte() {
        return qte;
    }

    public int getPaiement() {
        return paiement;
    }

    public int getLivre() {
        return livre;
    }
}
