package ru.mirea.musin.fragmentsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class ListFragment extends Fragment {
    private SharedViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ListView listView = new ListView(getContext());
        String[] countries = {"Россия", "Китай", "Бразилия", "Индия", "ЮАР"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, countries);
        listView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            viewModel.selectItem(countries[position]);
        });

        return listView;
    }
}