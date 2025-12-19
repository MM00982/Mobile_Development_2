package ru.mirea.musin.listsapp;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.math.BigInteger;

public class ScrollActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        LinearLayout container = findViewById(R.id.container);

        // Геометрическая прогрессия со знаменателем 2 (2^n)
        BigInteger val = BigInteger.ONE;
        BigInteger two = new BigInteger("2");

        for (int i = 1; i <= 100; i++) {
            TextView tv = new TextView(this);
            tv.setText(i + ") " + val.toString());
            tv.setTextSize(18);
            tv.setPadding(0, 8, 0, 8);
            container.addView(tv);

            val = val.multiply(two);
        }
    }
}