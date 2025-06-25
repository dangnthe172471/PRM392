package com.example.prm392.Retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.prm392.model.LoginResponse;

public class AccountManager {
    private static final String PREF_NAME = "account";

    public static void saveAccount(Context context, LoginResponse res) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit()
            .putInt("userId", res.getUserId())
            .putString("email", res.getEmail())
            .putString("role", res.getRole())
            .apply();
    }

    public static int getUserId(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getInt("userId", -1);
    }

    public static String getEmail(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString("email", "");
    }

    public static String getRole(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString("role", "");
    }

    public static void logout(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }

    public static boolean isLoggedIn(Context context) {
        return getUserId(context) != -1;
    }
} 