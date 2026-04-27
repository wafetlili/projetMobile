package com.example.eventmanager.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmanager.R;
import com.example.eventmanager.adapters.MesInscriptionsAdapter;
import com.example.eventmanager.database.DatabaseHelper;
import com.example.eventmanager.models.Inscription;

import java.util.List;

public class MesInscriptionsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_inscriptions);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView_mes_inscriptions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Demander l'email pour retrouver les inscriptions
        demanderEmail();
    }

    private void demanderEmail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mes inscriptions");
        builder.setMessage("Entrez votre email pour voir vos inscriptions :");

        EditText etEmail = new EditText(this);
        etEmail.setHint("votre@email.com");
        etEmail.setPadding(40, 20, 40, 20);
        builder.setView(etEmail);

        builder.setPositiveButton("Rechercher", (dialog, which) -> {
            String email = etEmail.getText().toString().trim();
            if (!email.isEmpty()) {
                chargerInscriptions(email);
            }
        });

        builder.setNegativeButton("Annuler", (dialog, which) -> finish());
        builder.setCancelable(false);
        builder.show();
    }

    private void chargerInscriptions(String email) {
        List<Inscription> inscriptions = db.getInscriptionsByEmail(email);

        if (inscriptions.isEmpty()) {
            Toast.makeText(this, "Aucune inscription trouvée pour cet email.", Toast.LENGTH_LONG).show();
        }

        MesInscriptionsAdapter adapter = new MesInscriptionsAdapter(this, inscriptions,
                inscription -> confirmerDesinscription(inscription, email));
        recyclerView.setAdapter(adapter);
    }

    private void confirmerDesinscription(Inscription inscription, String email) {
        new AlertDialog.Builder(this)
                .setTitle("Se désinscrire")
                .setMessage("Confirmer la désinscription ?")
                .setPositiveButton("Se désinscrire", (dialog, which) -> {
                    db.desinscrire(inscription.getId(), inscription.getEvenementId());
                    Toast.makeText(this, "Désinscription effectuée !", Toast.LENGTH_SHORT).show();
                    chargerInscriptions(email);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}