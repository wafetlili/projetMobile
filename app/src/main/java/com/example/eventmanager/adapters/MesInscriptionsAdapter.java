package com.example.eventmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmanager.R;
import com.example.eventmanager.models.Inscription;

import java.util.List;

public class MesInscriptionsAdapter extends RecyclerView.Adapter<MesInscriptionsAdapter.ViewHolder> {

    private Context context;
    private List<Inscription> inscriptions;
    private OnDesinscrireListener listener;

    public interface OnDesinscrireListener {
        void onDesinscrire(Inscription inscription);
    }

    public MesInscriptionsAdapter(Context context, List<Inscription> inscriptions,
                                  OnDesinscrireListener listener) {
        this.context = context;
        this.inscriptions = inscriptions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mes_inscriptions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inscription inscription = inscriptions.get(position);
        holder.tvEvenementId.setText("Événement #" + inscription.getEvenementId());
        holder.tvDate.setText("Inscrit le : " + inscription.getDateInscription());
        holder.btnDesinscrire.setOnClickListener(v -> {
            if (listener != null) listener.onDesinscrire(inscription);
        });
    }

    @Override
    public int getItemCount() {
        return inscriptions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEvenementId, tvDate;
        Button btnDesinscrire;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEvenementId = itemView.findViewById(R.id.tv_evenement_id);
            tvDate = itemView.findViewById(R.id.tv_date_inscription);
            btnDesinscrire = itemView.findViewById(R.id.btn_desinscrire);
        }
    }
}