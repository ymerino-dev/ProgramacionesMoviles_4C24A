package com.merino.campusgps.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferencias_campus_gps")

// COMENTARIO: Implementación de DataStore Preferences para persistir la selección del último lugar visitado
class PreferenciaRepositoryDataStore(private val context: Context) : PreferenciaRepository {

    private companion object {
        val KEY_ULTIMO_LUGAR_ID = intPreferencesKey("ultimo_lugar_id")
    }

    // COMENTARIO: Recupera el ID del último lugar guardado en DataStore como un Flow continuo
    override val ultimoLugarId: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[KEY_ULTIMO_LUGAR_ID]
    }

    // COMENTARIO: Guarda el ID del último lugar seleccionado en DataStore (o lo remueve si es null)
    override suspend fun guardarUltimoLugarId(id: Int?) {
        context.dataStore.edit { preferences ->
            if (id != null) {
                preferences[KEY_ULTIMO_LUGAR_ID] = id
            } else {
                preferences.remove(KEY_ULTIMO_LUGAR_ID)
            }
        }
    }
}
