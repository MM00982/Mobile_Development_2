package ru.mirea.musin.fragmentsapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ClickFragment extends Fragment {
    private FragmentListener listener;

    // Ссылаемся на созданный выше XML файл
    public ClickFragment() { super(R.layout.fragment_click); }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Проверяем, умеет ли Activity слушать события (реализует ли интерфейс)
        if (context instanceof FragmentListener) {
            listener = (FragmentListener) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.btnClickMe).setOnClickListener(v -> {
            if (listener != null) listener.sendResult("Кнопка нажата во фрагменте!");
        });
    }
}