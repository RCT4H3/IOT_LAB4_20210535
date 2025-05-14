package com.carlosdev.iot_lab4_20210535;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PronosticoAdapter extends RecyclerView.Adapter<PronosticoAdapter.ViewHolder> {

    private List<ForecastDay> pronosticos;

    public PronosticoAdapter(List<ForecastDay> pronosticos) {
        this.pronosticos = pronosticos;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtMaxTemp, txtMinTemp, txtCondition;

        public ViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtMaxTemp = itemView.findViewById(R.id.txtMaxTemp);
            txtMinTemp = itemView.findViewById(R.id.txtMinTemp);
            txtCondition = itemView.findViewById(R.id.txtCondition);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForecastDay item = pronosticos.get(position);
        holder.txtDate.setText(item.date);
        holder.txtMaxTemp.setText("Max: " + item.day.maxtemp_c + "°C");
        holder.txtMinTemp.setText("Min: " + item.day.mintemp_c + "°C");
        holder.txtCondition.setText(item.day.conditionText);
    }

    @Override
    public int getItemCount() {
        return pronosticos.size();
    }

    public void updateData(List<ForecastDay> newData) {
        pronosticos = newData;
        notifyDataSetChanged();
    }
}
