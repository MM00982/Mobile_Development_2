package ru.mirea.musin.fragmentsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StudentFragment extends Fragment {
    public StudentFragment() {
        super(R.layout.fragment_student);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int number = requireArguments().getInt("my_number_student");
        TextView tv = view.findViewById(R.id.tvStudentNumber);
        tv.setText("Мой номер по списку: " + number);
    }
}