package com.tecsup.app

import android.content.Context
import androidx.room.Room
import com.tecsup.app.data.auth.AuthRepository
import com.tecsup.app.data.auth.FirebaseAuthRepository
import com.tecsup.app.data.local.AppDatabase
import com.tecsup.app.data.remote.RetrofitClient
import com.tecsup.app.data.repository.ItemRepositoryImpl
import com.tecsup.app.notification.Notificador
import com.tecsup.app.notification.NotificadorAndroid

class AppContainer(context: Context) {

    // 1. Base de datos local Room con fallback a migración destructiva para evitar crashes por esquemas
    val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "app_database"
    )
        .fallbackToDestructiveMigration()
        .build()

    // 2. Repositorio de autenticación (Firebase Auth + Firestore)
    val authRepository: AuthRepository = FirebaseAuthRepository()

    // 3. Repositorio de ítems
    val itemRepository: ItemRepositoryImpl = ItemRepositoryImpl(
        apiService = RetrofitClient.apiService,
        itemDao = database.itemDao()
    )

    // 4. Notificador local
    val notificador: Notificador = NotificadorAndroid(context)
}
