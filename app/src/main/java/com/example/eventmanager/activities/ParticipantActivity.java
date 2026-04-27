package com.example.eventmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmanager.R;
import com.example.eventmanager.adapters.EvenementAdapter;
import com.example.eventmanager.database.DatabaseHelper;
import com.example.eventmanager.models.Evenement;

import java.util.ArrayList;
import java.util.List;

public class ParticipantActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EvenementAdapter adapter;
    private List<Evenement> tousLesEvenements;
    private DatabaseHelper db;
    private EditText etRecherche;
    private Button btnMesInscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);

        db = new DatabaseHelper(this);
        etRecherche = findViewById(R.id.et_recherche);
        btnMesInscriptions = findViewById(R.id.btn_mes_inscriptions);
        recyclerView = findViewById(R.id.recyclerView_evenements);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chargerEvenements();

        // Recherche en temps réel
        etRecherche.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrerEvenements(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Mes inscriptions
        btnMesInscriptions.setOnClickListener(v -> {
            Intent intent = new Intent(this, MesInscriptionsActivity.class);
            startActivity(intent);
        });
    }

    private void chargerEvenements() {
        tousLesEvenements = db.getAllEvenements();
        adapter = new EvenementAdapter(this, new ArrayList<>(tousLesEvenements), evenement -> {
            Intent intent = new Intent(this, DetailEvenementActivity.class);
            intent.putExtra("evenement_id", evenement.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void filtrerEvenements(String query) {
        List<Evenement> filtre = new ArrayList<>();
        for (Evenement e : tousLesEvenements) {
            if (e.getTitre().toLowerCase().contains(query.toLowerCase())
                    || e.getLieu().toLowerCase().contains(query.toLowerCase())) {
                filtre.add(e);
            }
        }
        adapter = new EvenementAdapter(this, filtre, evenement -> {
            Intent intent = new Intent(this, DetailEvenementActivity.class);
            intent.putExtra("evenement_id", evenement.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        chargerEvenements();
    }
}