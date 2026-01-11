package com.arpit.sociostack.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class RolePreferences(private val context: Context) {

    companion object {
        val ROLE_KEY = stringPreferencesKey("user_role")
    }

    suspend fun saveRole(role: String) {
        context.dataStore.edit { prefs ->
            prefs[ROLE_KEY] = role
        }
    }

    suspend fun clearRole() {
        context.dataStore.edit { prefs ->
            prefs.remove(ROLE_KEY)
        }
    }

    val roleFlow: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[ROLE_KEY]
        }
}
