package com.tecsup.app.data.auth

class FirebaseAuthRepository : AuthRepository {
    override suspend fun login(email: String, password: String): Boolean {
        // Simulación de autenticación (puedes conectarlo con Firebase Auth si lo requieres)
        return email.isNotBlank() && password.isNotBlank()
    }
}