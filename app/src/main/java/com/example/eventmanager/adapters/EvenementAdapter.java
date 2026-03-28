package com.example.eventmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmanager.R;
import com.example.eventmanager.models.Evenement;

import java.util.List;

public class EvenementAdapter extends RecyclerView.Adapter<EvenementAdapter.ViewHolder> {

    private Context context;
    private List<Evenement> evenements;
    private OnItemClickListener listener;

    // Interface pour gérer les clics
    public interface OnItemClickListener {
        void onItemClick(Evenement evenement);
    }

    public EvenementAdapter(Context context, List<Evenement> evenements, OnItemClickListener listener) {
        this.context = context;
        this.evenements = evenements;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_evenement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Evenement evenement = evenements.get(position);

        holder.tvTitre.setText(evenement.getTitre());
        holder.tvDateLieu.setText(evenement.getDate() + " - " + evenement.getHeure() + " - " + evenement.getLieu());
        holder.tvPlaces.setText("Places restantes : " + evenement.getPlacesRestantes() + " / " + evenement.getPlacesMax());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(evenement);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return evenements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitre, tvDateLieu, tvPlaces;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitre = itemView.findViewById(R.id.tv_titre);
            tvDateLieu = itemView.findViewById(R.id.tv_date_lieu);
            tvPlaces = itemView.findViewById(R.id.tv_places);
        }
    }
}