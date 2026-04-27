package com.example.eventmanager.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmanager.R;
import com.example.eventmanager.adapters.EvenementAdapter;
import com.example.eventmanager.database.DatabaseHelper;
import com.example.eventmanager.models.Evenement;

import java.util.List;

public class CreateurActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EvenementAdapter adapter;
    private List<Evenement> listeEvenements;
    private DatabaseHelper db;
    private Button btnAjouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createur);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView_evenements);
        btnAjouter = findViewById(R.id.btn_ajouter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chargerListeEvenements();

        btnAjouter.setOnClickListener(v -> afficherDialogAjoutEvenement());
    }

    private void chargerListeEvenements() {
        listeEvenements = db.getAllEvenements();
        adapter = new EvenementAdapter(this, listeEvenements, evenement -> {
            // Clic long : menu d'options
            afficherMenuOptions(evenement);
        });
        recyclerView.setAdapter(adapter);
    }

    private void afficherMenuOptions(Evenement evenement) {
        String[] options = {"Voir les inscrits", "Modifier", "Supprimer"};
        new AlertDialog.Builder(this)
                .setTitle(evenement.getTitre())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Voir les inscrits
                        Intent intent = new Intent(this, ListeInscritsActivity.class);
                        intent.putExtra("evenement_id", evenement.getId());
                        intent.putExtra("evenement_titre", evenement.getTitre());
                        startActivity(intent);
                    } else if (which == 1) {
                        afficherDialogModifierEvenement(evenement);
                    } else {
                        confirmerSuppression(evenement);
                    }
                })
                .show();
    }

    private void confirmerSuppression(Evenement evenement) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer")
                .setMessage("Supprimer \"" + evenement.getTitre() + "\" ?")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    db.supprimerEvenement(evenement.getId());
                    Toast.makeText(this, "Événement supprimé", Toast.LENGTH_SHORT).show();
                    chargerListeEvenements();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void afficherDialogAjoutEvenement() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajouter un événement");

        View view = getLayoutInflater().inflate(R.layout.dialog_ajouter_evenement, null);
        builder.setView(view);

        EditText etTitre = view.findViewById(R.id.et_titre);
        EditText etDescription = view.findViewById(R.id.et_description);
        EditText etDate = view.findViewById(R.id.et_date);
        EditText etHeure = view.findViewById(R.id.et_heure);
        EditText etLieu = view.findViewById(R.id.et_lieu);
        EditText etPlaces = view.findViewById(R.id.et_places);

        builder.setPositiveButton("Enregistrer", (dialog, which) -> {
            String titre = etTitre.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String heure = etHeure.getText().toString().trim();
            String lieu = etLieu.getText().toString().trim();
            String placesStr = etPlaces.getText().toString().trim();

            if (titre.isEmpty() || date.isEmpty() || lieu.isEmpty() || placesStr.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs obligatoires !", Toast.LENGTH_SHORT).show();
                return;
            }

            int placesMax = Integer.parseInt(placesStr);
            Evenement evenement = new Evenement(titre, description, date, heure, lieu, placesMax, placesMax);
            long id = db.ajouterEvenement(evenement);

            if (id != -1) {
                Toast.makeText(this, "Événement ajouté !", Toast.LENGTH_SHORT).show();
                chargerListeEvenements();
            } else {
                Toast.makeText(this, "Erreur lors de l'ajout !", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Annuler", null);
        builder.show();
    }

    private void afficherDialogModifierEvenement(Evenement evenement) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modifier l'événement");

        View view = getLayoutInflater().inflate(R.layout.dialog_ajouter_evenement, null);
        builder.setView(view);

        EditText etTitre = view.findViewById(R.id.et_titre);
        EditText etDescription = view.findViewById(R.id.et_description);
        EditText etDate = view.findViewById(R.id.et_date);
        EditText etHeure = view.findViewById(R.id.et_heure);
        EditText etLieu = view.findViewById(R.id.et_lieu);
        EditText etPlaces = view.findViewById(R.id.et_places);

        // Pré-remplir les champs
        etTitre.setText(evenement.getTitre());
        etDescription.setText(evenement.getDescription());
        etDate.setText(evenement.getDate());
        etHeure.setText(evenement.getHeure());
        etLieu.setText(evenement.getLieu());
        etPlaces.setText(String.valueOf(evenement.getPlacesMax()));

        builder.setPositiveButton("Enregistrer", (dialog, which) -> {
            String titre = etTitre.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String heure = etHeure.getText().toString().trim();
            String lieu = etLieu.getText().toString().trim();
            String placesStr = etPlaces.getText().toString().trim();

            if (titre.isEmpty() || date.isEmpty() || lieu.isEmpty() || placesStr.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs obligatoires !", Toast.LENGTH_SHORT).show();
                return;
            }

            int placesMax = Integer.parseInt(placesStr);
            // Recalculer places restantes
            int inscrites = evenement.getPlacesMax() - evenement.getPlacesRestantes();
            int placesRestantes = Math.max(0, placesMax - inscrites);

            evenement.setTitre(titre);
            evenement.setDescription(description);
            evenement.setDate(date);
            evenement.setHeure(heure);
            evenement.setLieu(lieu);
            evenement.setPlacesMax(placesMax);
            evenement.setPlacesRestantes(placesRestantes);

            db.modifierEvenement(evenement);
            Toast.makeText(this, "Événement modifié !", Toast.LENGTH_SHORT).show();
            chargerListeEvenements();
        });

        builder.setNegativeButton("Annuler", null);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        chargerListeEvenements();
    }
}