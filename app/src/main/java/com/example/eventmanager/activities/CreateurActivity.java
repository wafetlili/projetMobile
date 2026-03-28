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

        // Initialiser la base de données
        db = new DatabaseHelper(this);

        // Récupérer les références des vues
        recyclerView = findViewById(R.id.recyclerView_evenements);
        btnAjouter = findViewById(R.id.btn_ajouter);

        // Configurer le RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Charger la liste des événements
        chargerListeEvenements();

        // Gestion du clic sur le bouton Ajouter
        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherDialogAjoutEvenement();
            }
        });
    }

    // Méthode pour charger la liste des événements
    private void chargerListeEvenements() {
        listeEvenements = db.getAllEvenements();
        adapter = new EvenementAdapter(this, listeEvenements, new EvenementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Evenement evenement) {
                // Afficher les détails de l'événement (à faire plus tard)
                Toast.makeText(CreateurActivity.this,
                        "Vous avez cliqué sur: " + evenement.getTitre(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    // Afficher le dialogue pour ajouter un événement
    private void afficherDialogAjoutEvenement() {
        // Créer un dialogue personnalisé
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajouter un événement");

        // Charger le layout du formulaire
        View view = getLayoutInflater().inflate(R.layout.dialog_ajouter_evenement, null);
        builder.setView(view);

        // Récupérer les champs du formulaire
        EditText etTitre = view.findViewById(R.id.et_titre);
        EditText etDescription = view.findViewById(R.id.et_description);
        EditText etDate = view.findViewById(R.id.et_date);
        EditText etHeure = view.findViewById(R.id.et_heure);
        EditText etLieu = view.findViewById(R.id.et_lieu);
        EditText etPlaces = view.findViewById(R.id.et_places);

        // Boutons
        builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Récupérer les valeurs saisies
                String titre = etTitre.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                String date = etDate.getText().toString().trim();
                String heure = etHeure.getText().toString().trim();
                String lieu = etLieu.getText().toString().trim();
                String placesStr = etPlaces.getText().toString().trim();

                // Vérifier que les champs obligatoires sont remplis
                if (titre.isEmpty() || date.isEmpty() || lieu.isEmpty() || placesStr.isEmpty()) {
                    Toast.makeText(CreateurActivity.this,
                            "Veuillez remplir tous les champs obligatoires !",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int placesMax = Integer.parseInt(placesStr);
                int placesRestantes = placesMax;

                // Créer l'objet Evenement
                Evenement evenement = new Evenement(
                        titre, description, date, heure, lieu, placesMax, placesRestantes
                );

                // Ajouter à la base de données
                long id = db.ajouterEvenement(evenement);

                if (id != -1) {
                    Toast.makeText(CreateurActivity.this,
                            "Événement ajouté avec succès !",
                            Toast.LENGTH_SHORT).show();
                    // Recharger la liste
                    chargerListeEvenements();
                } else {
                    Toast.makeText(CreateurActivity.this,
                            "Erreur lors de l'ajout !",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Annuler", null);

        // Afficher le dialogue
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recharger la liste quand on revient sur cette activité
        chargerListeEvenements();
    }
}