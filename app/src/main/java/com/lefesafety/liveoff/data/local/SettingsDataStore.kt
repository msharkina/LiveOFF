package com.lefesafety.liveoff.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val NIGHT_MODE = booleanPreferencesKey("night_mode")
        val ANIMATIONS_ENABLED = booleanPreferencesKey("animations_enabled")
    }

    val nightMode: Flow<Boolean> = context.dataStore.data.map { it[Keys.NIGHT_MODE] ?: true }
    val animationsEnabled: Flow<Boolean> = context.dataStore.data.map { it[Keys.ANIMATIONS_ENABLED] ?: true }

    suspend fun setNightMode(enabled: Boolean) { context.dataStore.edit { it[Keys.NIGHT_MODE] = enabled } }
    suspend fun setAnimationsEnabled(enabled: Boolean) { context.dataStore.edit { it[Keys.ANIMATIONS_ENABLED] = enabled } }
}
