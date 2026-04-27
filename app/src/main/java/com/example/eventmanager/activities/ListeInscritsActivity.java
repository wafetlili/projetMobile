package com.example.eventmanager.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmanager.R;
import com.example.eventmanager.adapters.InscriptionAdapter;
import com.example.eventmanager.database.DatabaseHelper;
import com.example.eventmanager.models.Inscription;

import java.util.List;

public class ListeInscritsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvTitre, tvNbInscrits;
    private DatabaseHelper db;
    private int evenementId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_inscrits);

        db = new DatabaseHelper(this);

        tvTitre = findViewById(R.id.tv_titre_evenement);
        tvNbInscrits = findViewById(R.id.tv_nb_inscrits);
        recyclerView = findViewById(R.id.recyclerView_inscrits);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        evenementId = getIntent().getIntExtra("evenement_id", -1);
        String titrEvenement = getIntent().getStringExtra("evenement_titre");
        tvTitre.setText(titrEvenement);

        chargerInscrits();
    }

    private void chargerInscrits() {
        List<Inscription> inscrits = db.getInscriptionsByEvenement(evenementId);
        tvNbInscrits.setText(inscrits.size() + " participant(s) inscrit(s)");
        InscriptionAdapter adapter = new InscriptionAdapter(this, inscrits);
        recyclerView.setAdapter(adapter);
    }
}