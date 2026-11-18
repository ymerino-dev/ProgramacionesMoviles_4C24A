package com.merino.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.merino.campusgps.model.Posicion
import kotlinx.coroutines.tasks.await

class GpsApagadoException : Exception("Activa el GPS e intenta de nuevo")

class UbicacionRepositoryGoogle(
    private val context: Context
) : UbicacionRepository {

    private val cliente = LocationServices.getFusedLocationProviderClient(context)

    override fun playServicesDisponible(): Boolean {
        val estado = GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(context)
        return estado == ConnectionResult.SUCCESS
    }

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = runCatching {
        val token = CancellationTokenSource()
        val loc = cliente.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            token.token
        ).await() ?: throw GpsApagadoException()

        Posicion(loc.latitude, loc.longitude)
    }
}