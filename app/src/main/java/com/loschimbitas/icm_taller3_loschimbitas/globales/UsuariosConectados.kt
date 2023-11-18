package com.loschimbitas.icm_taller3_loschimbitas.globales

import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.messaging.RemoteMessage
import com.loschimbitas.icm_taller3_loschimbitas.modelo.Usuario
import com.loschimbitas.icm_taller3_loschimbitas.notificaciones.MyFirebaseMessagingService

object UsuariosConectados {
    private val databaseReference = FirebaseDatabase.getInstance().getReference("usuarios")
    private val usuarios = mutableListOf<Usuario>()
    private var childEventListener: ChildEventListener? = null

    fun getUsuarios(): List<Usuario> {
        return usuarios.toList()
    }

    init {
        usuarios.clear()
        configurarChildEventListener()
    }

    private fun configurarChildEventListener() {
        childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                // Usuario agregado
                val usuario = snapshot.getValue(Usuario::class.java)
                if (usuario?.estado == true) {
                    usuarios.add(usuario)
                    Log.i("UsuariosConectados1", "nombre usuario: ${usuario?.nombreUsuario}")
                    notificarObservadores()
                    if (usuario.numeroAutenticacion !=
                        UsuarioActual.getUsuario().numeroAutenticacion) {
                        Log.d("USUARIOSNOTIFY",
                            "Enviando notificación1")

                        // Obtener instancia de MyFirebaseMessagingService
                        val firebaseMessagingService = MyFirebaseMessagingService()

                        // Crear mensaje de notificación
                        val remoteMessage = RemoteMessage.Builder("FCM-SERVER")
                            .setMessageType("notification")
                            .addData("body", "Nuevo usuario conectado: $usuario.nombreUsuario")
                            .build()

                        // Enviar el mensaje a MyFirebaseMessagingService
                        firebaseMessagingService.onMessageReceived(remoteMessage)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val usuario = snapshot.getValue(Usuario::class.java)
                // Si el estado del usuario cambio a desconectado, eliminarlo de la lista
                var actualizo = false
                var encontrado = false

                usuario?.let { u ->
                    usuarios.find { it.numeroAutenticacion == u.numeroAutenticacion }?.let {
                            usuarioLista ->
                        if (usuarioLista.estado != u.estado) {
                            if (u.estado == false) {
                                usuarios.remove(usuarioLista)
                                Log.i("UsuariosConectados2",
                                    "Desconectado nombre usuario: ${usuario.nombreUsuario}")
                                actualizo = true
                            } else if (usuario.estado == true) {
                                usuarios.add(u)
                                Log.i("UsuariosConectados2",
                                    "Conectado nombre usuario: ${usuario.nombreUsuario}")
                                actualizo = true

                                if (usuario.numeroAutenticacion !=
                                    UsuarioActual.getUsuario().numeroAutenticacion) {
                                    Log.d("USUARIOSNOTIFY",
                                        "Enviando notificación1")
                                    // Obtener instancia de MyFirebaseMessagingService
                                    val firebaseMessagingService = MyFirebaseMessagingService()

                                    // Crear mensaje de notificación
                                    val remoteMessage = RemoteMessage.Builder("FCM-SERVER")
                                        .setMessageType("notification")
                                        .addData("body", "Nuevo usuario conectado: $usuario.nombreUsuario")
                                        .build()

                                    // Enviar el mensaje a MyFirebaseMessagingService
                                    firebaseMessagingService.onMessageReceived(remoteMessage)
                                }
                            }
                        }
                        encontrado = true
                    }
                }

                if (!encontrado) {
                    usuarios.add(usuario!!)
                    Log.i("UsuariosConectados2",
                        "Conectado nombre usuario: ${usuario.nombreUsuario}")
                    actualizo = true
                }

                if (actualizo)
                    notificarObservadores()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Usuario eliminado
                val usuario = snapshot.getValue(Usuario::class.java)
                usuarios.removeIf() { usuarioLista ->
                    usuarioLista.numeroAutenticacion == usuario?.numeroAutenticacion
                }
                notificarObservadores()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        databaseReference.addChildEventListener(childEventListener!!)
    }


    fun detenerObtencionUsuarios() {
        // Detener la escucha de eventos cuando sea necesario (por ejemplo, en onDestroy de la actividad)
        childEventListener?.let {
            databaseReference.removeEventListener(it)
        }
    }

    private val observadores = mutableListOf<UsuariosConectadosObserver>()

    interface UsuariosConectadosObserver {
        fun onUsuariosActualizados()
    }

    fun agregarObserver(observer: UsuariosConectadosObserver) {
        observadores.add(observer)
    }

    fun quitarObserver(observer: UsuariosConectadosObserver) {
        observadores.remove(observer)
    }

    private fun notificarObservadores() {
        Log.i("UsuariosConectados", "Notificando observadores")
        observadores.forEach {
            it.onUsuariosActualizados()
        }
    }
}
