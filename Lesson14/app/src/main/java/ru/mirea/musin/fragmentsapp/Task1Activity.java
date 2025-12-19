package ru.mirea.musin.fragmentsapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Task1Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task1);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt("my_number_student", 18);

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, StudentFragment.class, bundle)
                    .commit();
        }
    }
}