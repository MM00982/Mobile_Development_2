package ru.mirea.musin.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private final SharedPreferences prefs;

    public UserPreferences(Context context) {
        prefs = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE);
    }

    public void saveLastEmail(String email) {
        prefs.edit().putString("last_email", email).apply();
    }

    public String getLastEmail() {
        return prefs.getString("last_email", "Гость");
    }
}