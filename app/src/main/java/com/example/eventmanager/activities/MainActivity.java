package com.example.eventmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.eventmanager.R;

public class MainActivity extends AppCompatActivity {

    private Button btnOrganisateur;
    private Button btnParticipant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupérer les références des boutons
        btnOrganisateur = findViewById(R.id.btn_organisateur);
        btnParticipant = findViewById(R.id.btn_participant);

        // Gestion du clic sur le bouton Organisateur
        btnOrganisateur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent explicite pour démarrer l'activité Organisateur
                Intent intent = new Intent(MainActivity.this, CreateurActivity.class);
                startActivity(intent);
            }
        });

        // Gestion du clic sur le bouton Participant (à faire plus tard)
        btnParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Temporaire : afficher un message
                android.widget.Toast.makeText(MainActivity.this,
                        "Bientôt disponible !", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }
}
