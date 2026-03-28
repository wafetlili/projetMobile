package com.example.eventmanager.models;

public class Evenement {
    private int id;
    private String titre;
    private String description;
    private String date;
    private String heure;
    private String lieu;
    private int placesMax;
    private int placesRestantes;

    // Constructeur vide (obligatoire)
    public Evenement() {
    }

    // Constructeur avec tous les paramètres (sans id)
    public Evenement(String titre, String description, String date,
                     String heure, String lieu, int placesMax, int placesRestantes) {
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.heure = heure;
        this.lieu = lieu;
        this.placesMax = placesMax;
        this.placesRestantes = placesRestantes;
    }

    // Constructeur avec id (pour récupérer depuis la base)
    public Evenement(int id, String titre, String description, String date,
                     String heure, String lieu, int placesMax, int placesRestantes) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.heure = heure;
        this.lieu = lieu;
        this.placesMax = placesMax;
        this.placesRestantes = placesRestantes;
    }

    // Getters
    public int getId() { return id; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getHeure() { return heure; }
    public String getLieu() { return lieu; }
    public int getPlacesMax() { return placesMax; }
    public int getPlacesRestantes() { return placesRestantes; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }
    public void setHeure(String heure) { this.heure = heure; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public void setPlacesMax(int placesMax) { this.placesMax = placesMax; }
    public void setPlacesRestantes(int placesRestantes) { this.placesRestantes = placesRestantes; }
}