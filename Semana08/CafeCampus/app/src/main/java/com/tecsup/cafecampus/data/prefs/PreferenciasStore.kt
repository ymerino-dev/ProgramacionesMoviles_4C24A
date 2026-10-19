package com.tecsup.cafecampus.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1.Conexion unica al archivo de configuraciones
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ajustes")

class PreferenciasStore(private val context: Context) {

    // 2. Definimos los nombres de las "notas" (las llaves)
    companion object {
        val PERFIL_ACTIVO = stringPreferencesKey("perfilActivo")
        val MODO_OSCURO = booleanPreferencesKey("modoOscuro")
        val RECIBIR_PEDIDOS = booleanPreferencesKey("recibirPedidos")
    }

    // 3. LECTURA (Flow): Leer las notas y avisar al instante si cambian
    val perfilActivo: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PERFIL_ACTIVO] ?: "" // Si no hay nada, devuelve un texto vacío
    }

    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[MODO_OSCURO] ?: false // Por defecto es falso (modo claro)
    }

    val recibirPedidos: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[RECIBIR_PEDIDOS] ?: true // Por defecto la cafetería sí recibe pedidos
    }
    // Agrega esto dentro de tu PreferenciasStore.kt si no lo tienes:
    val notificaciones: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[NOTIFICACIONES_KEY] ?: true }

    suspend fun guardarNotificaciones(valor: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICACIONES_KEY] = valor
        }
    }

    // Y define la clave arriba junto a las demás:
    private val NOTIFICACIONES_KEY = booleanPreferencesKey("notificaciones_key")
    // 4. ESCRITURA (suspend): Escribir, modificar o borrar cosas en las notas
    suspend fun guardarPerfil(rol: String) {
        context.dataStore.edit { prefs ->
            prefs[PERFIL_ACTIVO] = rol
        }
    }

    suspend fun guardarModoOscuro(b: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[MODO_OSCURO] = b
        }
    }

    suspend fun guardarRecibirPedidos(b: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[RECIBIR_PEDIDOS] = b
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { prefs ->
            prefs[PERFIL_ACTIVO] = "" // Borra el texto de la nota para cerrar sesión
        }
    }
}