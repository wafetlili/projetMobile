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

    // Ajouter un événement
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

    // Récupérer tous les événements
    public List<Evenement> getAllEvenements() {
        List<Evenement> evenements = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EVENEMENT, null, null, null, null, null, COL_DATE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Evenement evenement = new Evenement();
                evenement.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                evenement.setTitre(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITRE)));
                evenement.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)));
                evenement.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)));
                evenement.setHeure(cursor.getString(cursor.getColumnIndexOrThrow(COL_HEURE)));
                evenement.setLieu(cursor.getString(cursor.getColumnIndexOrThrow(COL_LIEU)));
                evenement.setPlacesMax(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PLACES_MAX)));
                evenement.setPlacesRestantes(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PLACES_RESTANTES)));
                evenements.add(evenement);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return evenements;
    }

    // Récupérer un événement par son ID
    public Evenement getEvenementById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENEMENT, null, COL_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Evenement evenement = null;
        if (cursor.moveToFirst()) {
            evenement = new Evenement();
            evenement.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
            evenement.setTitre(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITRE)));
            evenement.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)));
            evenement.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)));
            evenement.setHeure(cursor.getString(cursor.getColumnIndexOrThrow(COL_HEURE)));
            evenement.setLieu(cursor.getString(cursor.getColumnIndexOrThrow(COL_LIEU)));
            evenement.setPlacesMax(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PLACES_MAX)));
            evenement.setPlacesRestantes(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PLACES_RESTANTES)));
        }
        cursor.close();
        db.close();
        return evenement;
    }
}