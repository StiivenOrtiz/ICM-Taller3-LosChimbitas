package com.loschimbitas.icm_taller3_loschimbitas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.loschimbitas.icm_taller3_loschimbitas.actividades.autenticacion.IniciarSesion
import com.loschimbitas.icm_taller3_loschimbitas.actividades.autenticacion.Registro
import com.loschimbitas.icm_taller3_loschimbitas.actividades.principal.MapsActivity
import com.loschimbitas.icm_taller3_loschimbitas.databinding.ActivityMainBinding
import com.loschimbitas.icm_taller3_loschimbitas.globales.UsuarioActual
import com.loschimbitas.icm_taller3_loschimbitas.modelo.Usuario
import com.google.firebase.FirebaseApp
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.util.Log
import com.loschimbitas.icm_taller3_loschimbitas.globales.UsuariosConectados

class MainActivity : AppCompatActivity(), UsuariosConectados.UsuariosConectadosObserver{

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    // Permisos para enviarle notificaciones al usuario
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(
                this,
                "FCM can't post notifications without POST_NOTIFICATIONS permission",
                Toast.LENGTH_LONG,
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UsuarioActual.setUsuario(Usuario())
        auth = FirebaseAuth.getInstance()
        FirebaseApp.initializeApp(this)
        askNotificacionsPermission()
        UsuariosConectados.agregarObserver(this)

        // Create channel to show notifications.
        val channelId = "default_notification_channel_id"
        val channelName = "users_channel"
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(
            NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW,
            ),
        )

        inicializar()
    }

    private fun inicializar() {
        iniciarListeners()
    }

    private fun iniciarListeners() {
        iniciarListenerBtnIniciarSesion()
        iniciarListenerBtnRegistrarse()
    }

    private fun iniciarListenerBtnIniciarSesion() {
        binding.btnIniciarSesion.setOnClickListener {
            startActivity(Intent(this, IniciarSesion::class.java))
        }
    }

    private fun iniciarListenerBtnRegistrarse() {
        binding.btnRegistrarse.setOnClickListener {
            startActivity(Intent(this, Registro::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null)
            updateUI(auth.currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(this, MapsActivity::class.java)

            UsuarioActual.obtenerInformacionUsuarioActual(currentUser.uid, baseContext)

            intent.putExtra("user", currentUser)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }


    private fun askNotificacionsPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("MainActivity", "No tiene permiso para enviar notificaciones")
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        else{
            Log.i("MainActivity", "Tiene permiso para enviar notificaciones")
        }
    }

    override fun onUsuariosActualizados() {
        val lista = UsuariosConectados.getUsuarios()
        if (lista.isNotEmpty()) {
            val ultimoUsuario = lista.last()
            if (UsuarioActual.getUsuario().numeroAutenticacion !=
                ultimoUsuario.numeroAutenticacion) {
                Toast.makeText(
                    this,
                    "Se ha conectado ${ultimoUsuario.nombreUsuario}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}