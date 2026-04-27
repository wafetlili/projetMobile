package com.example.eventmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmanager.R;
import com.example.eventmanager.models.Inscription;

import java.util.List;

public class InscriptionAdapter extends RecyclerView.Adapter<InscriptionAdapter.ViewHolder> {

    private Context context;
    private List<Inscription> inscriptions;

    public InscriptionAdapter(Context context, List<Inscription> inscriptions) {
        this.context = context;
        this.inscriptions = inscriptions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_inscription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inscription inscription = inscriptions.get(position);
        holder.tvNom.setText(inscription.getParticipantNom());
        holder.tvEmail.setText(inscription.getParticipantEmail());
        holder.tvDate.setText("Inscrit le : " + inscription.getDateInscription());
    }

    @Override
    public int getItemCount() {
        return inscriptions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNom, tvEmail, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNom = itemView.findViewById(R.id.tv_nom);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvDate = itemView.findViewById(R.id.tv_date_inscription);
        }
    }
}