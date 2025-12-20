package ru.mirea.musin.navigationapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Создаем View программно или через разметку. Для простоты вернем TextView.
        TextView tv = new TextView(getContext());
        tv.setText("Главная страница (Home)");
        tv.setTextSize(30);
        tv.setGravity(android.view.Gravity.CENTER);
        return tv;
    }
}