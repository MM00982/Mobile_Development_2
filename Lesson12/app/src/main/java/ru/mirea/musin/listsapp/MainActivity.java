package ru.mirea.musin.listsapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnScroll).setOnClickListener(v ->
                startActivity(new Intent(this, ScrollActivity.class)));

        findViewById(R.id.btnList).setOnClickListener(v ->
                startActivity(new Intent(this, ListActivity.class)));

        findViewById(R.id.btnRecycler).setOnClickListener(v ->
                startActivity(new Intent(this, RecyclerActivity.class)));
    }
}