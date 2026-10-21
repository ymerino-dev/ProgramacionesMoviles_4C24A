package com.tecsup.cafecampus

import android.app.Application
import com.tecsup.cafecampus.data.local.CafeDatabase
import com.tecsup.cafecampus.data.local.DatosIniciales
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CafeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val db = CafeDatabase.obtener(this)

        CoroutineScope(Dispatchers.IO).launch {
            DatosIniciales.sembrar(db)
        }
    }
}