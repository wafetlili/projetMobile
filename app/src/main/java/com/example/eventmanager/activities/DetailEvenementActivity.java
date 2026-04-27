package com.example.eventmanager.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanager.R;
import com.example.eventmanager.database.DatabaseHelper;
import com.example.eventmanager.models.Evenement;
import com.example.eventmanager.models.Inscription;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailEvenementActivity extends AppCompatActivity {

    private TextView tvTitre, tvDescription, tvDate, tvHeure, tvLieu, tvPlaces;
    private Button btnInscrire;
    private DatabaseHelper db;
    private Evenement evenement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_evenement);

        db = new DatabaseHelper(this);

        tvTitre = findViewById(R.id.tv_titre);
        tvDescription = findViewById(R.id.tv_description);
        tvDate = findViewById(R.id.tv_date);
        tvHeure = findViewById(R.id.tv_heure);
        tvLieu = findViewById(R.id.tv_lieu);
        tvPlaces = findViewById(R.id.tv_places);
        btnInscrire = findViewById(R.id.btn_inscrire);

        int evenementId = getIntent().getIntExtra("evenement_id", -1);
        evenement = db.getEvenementById(evenementId);

        if (evenement != null) {
            afficherDetails();
        }

        btnInscrire.setOnClickListener(v -> afficherDialogInscription());
    }

    private void afficherDetails() {
        tvTitre.setText(evenement.getTitre());
        tvDescription.setText(evenement.getDescription());
        tvDate.setText("📅 " + evenement.getDate());
        tvHeure.setText("🕐 " + evenement.getHeure());
        tvLieu.setText("📍 " + evenement.getLieu());
        tvPlaces.setText("Places restantes : " + evenement.getPlacesRestantes() + " / " + evenement.getPlacesMax());

        if (evenement.getPlacesRestantes() == 0) {
            btnInscrire.setEnabled(false);
            btnInscrire.setText("COMPLET");
        }
    }

    private void afficherDialogInscription() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("S'inscrire à l'événement");

        View view = getLayoutInflater().inflate(R.layout.dialog_inscription, null);
        builder.setView(view);

        EditText etNom = view.findViewById(R.id.et_nom);
        EditText etEmail = view.findViewById(R.id.et_email);

        builder.setPositiveButton("S'inscrire", (dialog, which) -> {
            String nom = etNom.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (nom.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs !", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérifier double inscription
            if (db.isDejaInscrit(evenement.getId(), email)) {
                Toast.makeText(this, "Vous êtes déjà inscrit à cet événement !", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérifier places disponibles
            if (evenement.getPlacesRestantes() <= 0) {
                Toast.makeText(this, "Plus de places disponibles !", Toast.LENGTH_SHORT).show();
                return;
            }

            String dateInscription = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    .format(new Date());

            Inscription inscription = new Inscription(evenement.getId(), nom, email, dateInscription);
            long id = db.ajouterInscription(inscription);

            if (id != -1) {
                Toast.makeText(this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                // Rafraîchir l'affichage
                evenement = db.getEvenementById(evenement.getId());
                afficherDetails();
            } else {
                Toast.makeText(this, "Erreur lors de l'inscription !", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Annuler", null);
        builder.show();
    }
}