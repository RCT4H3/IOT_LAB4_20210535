package com.carlosdev.iot_lab4_20210535;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder> {

    private List<Location> listaLocations;
    private Context context;
    private OnItemClickListener listener;

    // Interfaz para manejar clics para que nos lleve al pronóstico de cada resultado de location
    public interface OnItemClickListener {
        void onItemClick(Location location);
    }

    // Constructor
    public LocationsAdapter(Context context, List<Location> listaLocations, OnItemClickListener listener) {
        this.context = context;
        this.listaLocations = listaLocations;
        this.listener = listener;
    }

    public void actualizarLista(List<Location> nuevaLista) {
        this.listaLocations = nuevaLista;
        notifyDataSetChanged();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre, textRegion, txtPais, txtId;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.txtNombre);
            textRegion = itemView.findViewById(R.id.txtRegion);
            txtPais = itemView.findViewById(R.id.txtPais);
            txtId = itemView.findViewById(R.id.txtId);
        }

        public void bind(Location location, OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(location));
        }
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = listaLocations.get(position);
        holder.textNombre.setText(location.getName());
        holder.textRegion.setText(location.getRegion());
        holder.txtPais.setText(location.getCountry());
        holder.txtId.setText("ID: " + location.getId());

        holder.bind(location, listener);
    }

    @Override
    public int getItemCount() {
        return listaLocations != null ? listaLocations.size() : 0;
    }
}
