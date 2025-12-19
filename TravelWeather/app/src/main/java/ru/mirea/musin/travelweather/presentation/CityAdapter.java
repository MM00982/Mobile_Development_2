package ru.mirea.musin.travelweather.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.musin.travelweather.R;
import ru.mirea.musin.domain.models.City;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private List<City> cities = new ArrayList<>();
    private final OnCityClickListener clickListener;
    private final OnCityDeleteListener deleteListener;

    public interface OnCityClickListener {
        void onCityClick(City city);
    }

    public interface OnCityDeleteListener {
        void onCityDelete(City city);
    }

    public CityAdapter(OnCityClickListener clickListener, OnCityDeleteListener deleteListener) {
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    public void setItems(List<City> newCities) {
        this.cities = newCities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Используем уже существующий у вас макет item_city_removable
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_city_removable, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        holder.bind(cities.get(position));
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    class CityViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvCountry;
        private final View btnDelete;
        private final View containerInfo;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCityNameItem);
            tvCountry = itemView.findViewById(R.id.tvCountryItem);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            containerInfo = itemView.findViewById(R.id.containerInfo);
        }

        public void bind(City city) {
            tvName.setText(city.getName());
            tvCountry.setText("Сохранено");

            containerInfo.setOnClickListener(v -> clickListener.onCityClick(city));
            btnDelete.setOnClickListener(v -> deleteListener.onCityDelete(city));
        }
    }
}