package com.aek.yagoubi.sac20.Object;

public class Sac {

    private int id;
    private int id_demande;
    private int id_article;
    private int qte;

    public Sac(int id, int id_demande, int id_article, int qte) {
        this.id = id;
        this.id_demande = id_demande;
        this.id_article = id_article;
        this.qte = qte;
    }

    public void setId_article(int id_article) {
        this.id_article = id_article;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public Sac(int id_article, int qte) {
        this.id = id;

        this.id_article = id_article;
        this.qte = qte;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_demande(int id_demande) {
        this.id_demande = id_demande;
    }

    public int getId() {
        return id;
    }

    public int getId_demande() {
        return id_demande;
    }

    public int getId_article() {
        return id_article;
    }

    public int getQte() {
        return qte;
    }
}
