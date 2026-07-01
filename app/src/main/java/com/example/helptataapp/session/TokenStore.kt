package com.example.helptataapp.session

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.KeyStore

object TokenStore {
    private const val PREFS_FILE = "helptata_secure_prefs"
    private const val KEY_TOKEN  = "jwt_token"

    private fun buildMasterKey(ctx: Context) =
        MasterKey.Builder(ctx)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

    private fun createPrefs(ctx: Context) = EncryptedSharedPreferences.create(
        ctx,
        PREFS_FILE,
        buildMasterKey(ctx),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // Si la clave del KeyStore quedó corrupta (reinstalación con firma distinta),
    // la borramos y creamos todo desde cero. El usuario tendrá que volver a iniciar sesión.
    private fun prefs(ctx: Context) = try {
        createPrefs(ctx)
    } catch (_: Exception) {
        try {
            val ks = KeyStore.getInstance("AndroidKeyStore").also { it.load(null) }
            ks.deleteEntry("_androidx_security_master_key")
        } catch (_: Exception) {}
        ctx.deleteSharedPreferences(PREFS_FILE)
        createPrefs(ctx)
    }

    fun save(ctx: Context, token: String) {
        prefs(ctx).edit().putString(KEY_TOKEN, token).apply()
    }

    fun load(ctx: Context): String =
        prefs(ctx).getString(KEY_TOKEN, "") ?: ""

    fun clear(ctx: Context) {
        prefs(ctx).edit().remove(KEY_TOKEN).apply()
    }
}