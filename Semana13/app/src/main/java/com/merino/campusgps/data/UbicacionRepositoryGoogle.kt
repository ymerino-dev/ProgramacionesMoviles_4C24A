package com.tecsup.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.tecsup.campusgps.model.Posicion
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(private val context: Context) : UbicacionRepository {
    private val cliente = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = runCatching {
        val token = CancellationTokenSource().token
        val ubicacion = cliente.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token).await()
            ?: error("No se pudo obtener la ubicación. Activa el GPS e intenta de nuevo")
        Posicion(ubicacion.latitude, ubicacion.longitude)
    }
}