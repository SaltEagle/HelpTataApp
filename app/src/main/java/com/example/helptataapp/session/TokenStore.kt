package com.example.helptataapp.session

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object TokenStore {
    private const val PREFS_FILE = "helptata_secure_prefs"
    private const val KEY_TOKEN  = "jwt_token"

    private fun prefs(ctx: Context) = EncryptedSharedPreferences.create(
        ctx,
        PREFS_FILE,
        MasterKey.Builder(ctx)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun save(ctx: Context, token: String) {
        prefs(ctx).edit().putString(KEY_TOKEN, token).apply()
    }

    fun load(ctx: Context): String =
        prefs(ctx).getString(KEY_TOKEN, "") ?: ""

    fun clear(ctx: Context) {
        prefs(ctx).edit().remove(KEY_TOKEN).apply()
    }
}
