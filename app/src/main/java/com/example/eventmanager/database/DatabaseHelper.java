package com.example.eventmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.eventmanager.models.Evenement;
import com.example.eventmanager.models.Inscription;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "eventmanager.db";
    private static final int DATABASE_VERSION = 1;

    // Table Evenement
    public static final String TABLE_EVENEMENT = "evenement";
    public static final String COL_ID = "id";
    public static final String COL_TITRE = "titre";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_DATE = "date";
    public static final String COL_HEURE = "heure";
    public static final String COL_LIEU = "lieu";
    public static final String COL_PLACES_MAX = "places_max";
    public static final String COL_PLACES_RESTANTES = "places_restantes";

    // Table Inscription
    public static final String TABLE_INSCRIPTION = "inscription";
    public static final String COL_EVENEMENT_ID = "evenement_id";
    public static final String COL_PARTICIPANT_NOM = "participant_nom";
    public static final String COL_PARTICIPANT_EMAIL = "participant_email";
    public static final String COL_DATE_INSCRIPTION = "date_inscription";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENEMENT_TABLE = "CREATE TABLE " + TABLE_EVENEMENT + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITRE + " TEXT NOT NULL,"
                + COL_DESCRIPTION + " TEXT,"
                + COL_DATE + " TEXT,"
                + COL_HEURE + " TEXT,"
                + COL_LIEU + " TEXT,"
                + COL_PLACES_MAX + " INTEGER,"
                + COL_PLACES_RESTANTES + " INTEGER"
                + ")";
        db.execSQL(CREATE_EVENEMENT_TABLE);

        String CREATE_INSCRIPTION_TABLE = "CREATE TABLE " + TABLE_INSCRIPTION + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_EVENEMENT_ID + " INTEGER,"
                + COL_PARTICIPANT_NOM + " TEXT,"
                + COL_PARTICIPANT_EMAIL + " TEXT,"
                + COL_DATE_INSCRIPTION + " TEXT,"
                + "FOREIGN KEY(" + COL_EVENEMENT_ID + ") REFERENCES " + TABLE_EVENEMENT + "(" + COL_ID + ")"
                + ")";
        db.execSQL(CREATE_INSCRIPTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENEMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSCRIPTION);
        onCreate(db);
    }

    // ─── ÉVÉNEMENTS ───────────────────────────────────────────────────────────

    public long ajouterEvenement(Evenement evenement) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITRE, evenement.getTitre());
        values.put(COL_DESCRIPTION, evenement.getDescription());
        values.put(COL_DATE, evenement.getDate());
        values.put(COL_HEURE, evenement.getHeure());
        values.put(COL_LIEU, evenement.getLieu());
        values.put(COL_PLACES_MAX, evenement.getPlacesMax());
        values.put(COL_PLACES_RESTANTES, evenement.getPlacesRestantes());
        long id = db.insert(TABLE_EVENEMENT, null, values);
        db.close();
        return id;
    }

    public int modifierEvenement(Evenement evenement) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITRE, evenement.getTitre());
        values.put(COL_DESCRIPTION, evenement.getDescription());
        values.put(COL_DATE, evenement.getDate());
        values.put(COL_HEURE, evenement.getHeure());
        values.put(COL_LIEU, evenement.getLieu());
        values.put(COL_PLACES_MAX, evenement.getPlacesMax());
        values.put(COL_PLACES_RESTANTES, evenement.getPlacesRestantes());
        int rows = db.update(TABLE_EVENEMENT, values, COL_ID + " = ?",
                new String[]{String.valueOf(evenement.getId())});
        db.close();
        return rows;
    }

    public void supprimerEvenement(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Supprimer d'abord les inscriptions liées
        db.delete(TABLE_INSCRIPTION, COL_EVENEMENT_ID + " = ?", new String[]{String.valueOf(id)});
        db.delete(TABLE_EVENEMENT, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Evenement> getAllEvenements() {
        List<Evenement> evenements = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENEMENT, null, null, null, null, null, COL_DATE + " ASC");
        if (cursor.moveToFirst()) {
            do {
                evenements.add(cursorToEvenement(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return evenements;
    }

    public Evenement getEvenementById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENEMENT, null, COL_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        Evenement evenement = null;
        if (cursor.moveToFirst()) {
            evenement = cursorToEvenement(cursor);
        }
        cursor.close();
        db.close();
        return evenement;
    }

    private Evenement cursorToEvenement(Cursor cursor) {
        Evenement e = new Evenement();
        e.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
        e.setTitre(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITRE)));
        e.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)));
        e.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)));
        e.setHeure(cursor.getString(cursor.getColumnIndexOrThrow(COL_HEURE)));
        e.setLieu(cursor.getString(cursor.getColumnIndexOrThrow(COL_LIEU)));
        e.setPlacesMax(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PLACES_MAX)));
        e.setPlacesRestantes(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PLACES_RESTANTES)));
        return e;
    }

    // ─── INSCRIPTIONS ─────────────────────────────────────────────────────────

    public long ajouterInscription(Inscription inscription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_EVENEMENT_ID, inscription.getEvenementId());
        values.put(COL_PARTICIPANT_NOM, inscription.getParticipantNom());
        values.put(COL_PARTICIPANT_EMAIL, inscription.getParticipantEmail());
        values.put(COL_DATE_INSCRIPTION, inscription.getDateInscription());
        long id = db.insert(TABLE_INSCRIPTION, null, values);

        // Décrémenter les places restantes
        if (id != -1) {
            db.execSQL("UPDATE " + TABLE_EVENEMENT + " SET " + COL_PLACES_RESTANTES
                    + " = " + COL_PLACES_RESTANTES + " - 1 WHERE " + COL_ID
                    + " = " + inscription.getEvenementId());
        }
        db.close();
        return id;
    }

    public List<Inscription> getInscriptionsByEvenement(int evenementId) {
        List<Inscription> inscriptions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INSCRIPTION, null, COL_EVENEMENT_ID + " = ?",
                new String[]{String.valueOf(evenementId)}, null, null, COL_DATE_INSCRIPTION + " DESC");
        if (cursor.moveToFirst()) {
            do {
                inscriptions.add(cursorToInscription(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return inscriptions;
    }

    public List<Inscription> getInscriptionsByEmail(String email) {
        List<Inscription> inscriptions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INSCRIPTION, null, COL_PARTICIPANT_EMAIL + " = ?",
                new String[]{email}, null, null, COL_DATE_INSCRIPTION + " DESC");
        if (cursor.moveToFirst()) {
            do {
                inscriptions.add(cursorToInscription(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return inscriptions;
    }

    public boolean isDejaInscrit(int evenementId, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INSCRIPTION, new String[]{COL_ID},
                COL_EVENEMENT_ID + " = ? AND " + COL_PARTICIPANT_EMAIL + " = ?",
                new String[]{String.valueOf(evenementId), email}, null, null, null);
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return existe;
    }

    public void desinscrire(int inscriptionId, int evenementId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INSCRIPTION, COL_ID + " = ?", new String[]{String.valueOf(inscriptionId)});
        // Incrémenter les places restantes
        db.execSQL("UPDATE " + TABLE_EVENEMENT + " SET " + COL_PLACES_RESTANTES
                + " = " + COL_PLACES_RESTANTES + " + 1 WHERE " + COL_ID + " = " + evenementId);
        db.close();
    }

    private Inscription cursorToInscription(Cursor cursor) {
        Inscription i = new Inscription();
        i.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
        i.setEvenementId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_EVENEMENT_ID)));
        i.setParticipantNom(cursor.getString(cursor.getColumnIndexOrThrow(COL_PARTICIPANT_NOM)));
        i.setParticipantEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_PARTICIPANT_EMAIL)));
        i.setDateInscription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE_INSCRIPTION)));
        return i;
    }
}