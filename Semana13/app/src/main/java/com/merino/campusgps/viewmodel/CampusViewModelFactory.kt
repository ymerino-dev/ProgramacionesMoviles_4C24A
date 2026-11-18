package com.merino.campusgps.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.merino.campusgps.data.LugaresRepositoryLocal
import com.merino.campusgps.data.PreferenciaRepositoryDataStore
import com.merino.campusgps.data.UbicacionRepositoryGoogle

// COMENTARIO: Factory para inyectar Repositorios (Ubicación, Lugares y DataStore) en MapaViewModel
class CampusViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val appContext = context.applicationContext
        return MapaViewModel(
            ubicacionRepo = UbicacionRepositoryGoogle(appContext),
            lugaresRepo = LugaresRepositoryLocal(),
            preferenciaRepo = PreferenciaRepositoryDataStore(appContext)
        ) as T
    }
}
