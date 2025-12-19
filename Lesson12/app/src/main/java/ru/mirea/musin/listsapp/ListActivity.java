package ru.mirea.musin.listsapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = findViewById(R.id.listView);
        List<String> books = new ArrayList<>();

        // Генерируем 30+ книг
        for (int i = 1; i <= 40; i++) {
            books.add("Книга №" + i + " - Автор " + i);
        }

        // Используем стандартный адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, books);

        listView.setAdapter(adapter);
    }
}