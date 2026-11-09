package com.tecsup.app

import android.content.Context
import androidx.room.Room
import com.tecsup.app.data.auth.AuthRepository
import com.tecsup.app.data.auth.FirebaseAuthRepository
import com.tecsup.app.data.local.AppDatabase
import com.tecsup.app.data.remote.RetrofitClient
import com.tecsup.app.data.repository.ItemRepositoryImpl

class AppContainer(context: Context) {

    // 1. Base de datos local Room
    val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "app_database"
    ).build()

    // 2. Repositorio de autenticación (Firebase)
    val authRepository: AuthRepository = FirebaseAuthRepository()

    // 3. Repositorio de ítems (Conectando ApiService y ItemDao)
    val itemRepository: ItemRepositoryImpl = ItemRepositoryImpl(
        apiService = RetrofitClient.apiService,
        itemDao = database.itemDao()
    )
}