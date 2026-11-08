package com.tecsup.app.data.auth

interface AuthRepository {
    suspend fun login(email: String, password: String): Boolean
}