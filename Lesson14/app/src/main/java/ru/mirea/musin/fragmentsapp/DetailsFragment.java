package ru.mirea.musin.fragmentsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class DetailsFragment extends Fragment {
    public DetailsFragment() {
        super(R.layout.fragment_details); // Создай layout ниже
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView tvDetails = view.findViewById(R.id.tvDetails);
        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        viewModel.getSelectedItem().observe(getViewLifecycleOwner(), country -> {
            tvDetails.setText("Информация о стране: " + country);
        });
    }
}