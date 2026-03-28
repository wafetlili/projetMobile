package com.example.eventmanager.models;

public class Inscription {
    private int id;
    private int evenementId;
    private String participantNom;
    private String participantEmail;
    private String dateInscription;

    public Inscription() {
    }

    public Inscription(int evenementId, String participantNom,
                       String participantEmail, String dateInscription) {
        this.evenementId = evenementId;
        this.participantNom = participantNom;
        this.participantEmail = participantEmail;
        this.dateInscription = dateInscription;
    }

    // Getters
    public int getId() { return id; }
    public int getEvenementId() { return evenementId; }
    public String getParticipantNom() { return participantNom; }
    public String getParticipantEmail() { return participantEmail; }
    public String getDateInscription() { return dateInscription; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setEvenementId(int evenementId) { this.evenementId = evenementId; }
    public void setParticipantNom(String participantNom) { this.participantNom = participantNom; }
    public void setParticipantEmail(String participantEmail) { this.participantEmail = participantEmail; }
    public void setDateInscription(String dateInscription) { this.dateInscription = dateInscription; }
}