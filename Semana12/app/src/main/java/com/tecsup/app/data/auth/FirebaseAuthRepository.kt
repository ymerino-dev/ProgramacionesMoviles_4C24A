package com.tecsup.app.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.app.domain.model.Rol
import com.tecsup.app.domain.model.Sesion
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> = try {
        val authResult = auth.signInWithEmailAndPassword(correo, clave).await()
        val uid = authResult.user?.uid ?: throw Exception("Usuario no encontrado en Firebase Auth")
        val sesion = leerSesion(uid, correo)
        Result.success(sesion)
    } catch (e: Exception) {
        println("FirebaseAuthRepository Error al iniciar sesión: ${e.message}")
        Result.failure(e)
    }

    override suspend fun sesionActual(): Sesion? {
        val user = auth.currentUser ?: return null
        return try {
            leerSesion(user.uid, user.email ?: "")
        } catch (e: Exception) {
            println("FirebaseAuthRepository Error al obtener sesión actual: ${e.message}")
            val rol = if (user.email?.contains("admin", ignoreCase = true) == true) Rol.ADMIN else Rol.USUARIO
            Sesion(user.uid, user.email ?: "", rol)
        }
    }

    override fun cerrarSesion() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            println("FirebaseAuthRepository Error al cerrar sesión: ${e.message}")
        }
    }

    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        return try {
            val docRef = db.collection("usuarios").document(uid)
            val doc = docRef.get().await()
            if (doc != null && doc.exists()) {
                val rolStr = doc.getString("rol")
                val rol = if (rolStr?.lowercase() == "admin") Rol.ADMIN else Rol.USUARIO
                Sesion(uid, correo, rol)
            } else {
                val rol = if (correo.contains("admin", ignoreCase = true)) Rol.ADMIN else Rol.USUARIO
                Sesion(uid, correo, rol)
            }
        } catch (e: Exception) {
            println("FirebaseAuthRepository Excepción al consultar Firestore para UID $uid: ${e.message}. Asignando rol por defecto.")
            val rol = if (correo.contains("admin", ignoreCase = true)) Rol.ADMIN else Rol.USUARIO
            Sesion(uid, correo, rol)
        }
    }
}
