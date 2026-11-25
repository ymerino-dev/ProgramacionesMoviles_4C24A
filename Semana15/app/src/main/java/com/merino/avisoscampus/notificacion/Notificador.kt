package com.merino.avisoscampus.notificacion

import android.content.Context
import com.merino.avisoscampus.model.Aviso

object Notificador {
    const val CANAL = "avisos"

    fun crearCanal(context: Context) {
        // TODO Paso 1: si SDK_INT >= O, crear NotificationChannel y registrarlo
    }

    fun tienePermiso(context: Context): Boolean {
        // TODO Paso 2: si SDK_INT < 33 -> true; si no -> ContextCompat.checkSelfPermission
        return false
    }

    fun mostrar(context: Context, aviso: Aviso): Boolean {
        // TODO 1 (Paso 3): si no tienePermiso(context) -> return false
        // TODO 2 (Paso 3 y 4): construir la notificacion con NotificationCompat.Builder (y PendingIntent en Paso 4)
        // TODO 3 (Paso 3): NotificationManagerCompat.from(context).notify(aviso.id, ...)
        return true
    }
}