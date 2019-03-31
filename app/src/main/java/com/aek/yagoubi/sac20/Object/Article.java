package com.aek.yagoubi.sac20.Object;

public class Article {
   private int id;
    private String name,type;
    private String codebare,codebareFormat;
    private int prix;

    public Article(int id, String name, String type, String codebare, String codebareFormat, int prix) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.codebare = codebare;
        this.codebareFormat = codebareFormat;
        this.prix = prix;
    }

    public Article(int id, String name, String type, int prix) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.prix = prix;
    }

    public String getCodebare() {
        return codebare;
    }

    public String getCodebareFormat() {
        return codebareFormat;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getPrix() {
        return prix;
    }
}
