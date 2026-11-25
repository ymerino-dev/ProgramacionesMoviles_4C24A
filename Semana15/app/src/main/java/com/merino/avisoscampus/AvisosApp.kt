package com.merino.avisoscampus

import android.app.Application
import com.merino.avisoscampus.notificacion.Notificador

class AvisosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Notificador.crearCanal(this)
    }
}