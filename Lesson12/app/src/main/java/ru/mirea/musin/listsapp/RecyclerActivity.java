package ru.mirea.musin.listsapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> events = new ArrayList<>();
        events.add("Открытие Америки (1492)");
        events.add("Первый полет человека в космос (1961)");
        events.add("Крещение Руси (988)");
        events.add("Основание Москвы (1147)");
        events.add("Бородинское сражение (1812)");
        // ... можно добавить больше

        recyclerView.setAdapter(new HistoryAdapter(events));
    }
}