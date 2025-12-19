package ru.mirea.musin.fragmentsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InfoFragment extends Fragment {
    public InfoFragment() { super(R.layout.fragment_info); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView tvResult = view.findViewById(R.id.tvResult);
        Button btnOpen = view.findViewById(R.id.btnOpen);

        // Слушаем результат от диалога
        getChildFragmentManager().setFragmentResultListener("requestKey", this, (key, bundle) -> {
            String text = bundle.getString("key");
            tvResult.setText("Получено: " + text);
        });

        btnOpen.setOnClickListener(v -> {
            new MyBottomSheetFragment().show(getChildFragmentManager(), "ModalBottomSheet");
        });
    }
}