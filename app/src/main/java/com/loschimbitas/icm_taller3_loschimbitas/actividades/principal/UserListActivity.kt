package com.loschimbitas.icm_taller3_loschimbitas.actividades.principal

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.loschimbitas.icm_taller3_loschimbitas.R
import com.loschimbitas.icm_taller3_loschimbitas.globales.UsuarioAcual
import com.loschimbitas.icm_taller3_loschimbitas.globales.UsuariosConectados
import com.loschimbitas.icm_taller3_loschimbitas.modelo.Usuario
import com.loschimbitas.icm_taller3_loschimbitas.util.TrackerLocation

class UserListActivity : AppCompatActivity(), UsuariosConectados.UsuariosConectadosObserver{

    private var usuariosConectados = UsuariosConectados.getUsuarios()
    private val listViewUsers: ListView = findViewById(R.id.listViewUsers)
    private lateinit var trackerLocation: TrackerLocation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        // Configurar un adaptador personalizado para la ListView
        val adapter = UserListAdapter(this, usuariosConectados)
        listViewUsers.adapter = adapter

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        } else {
            trackUser()
        }
    }

    private fun trackUser(){
        trackerLocation = ViewModelProvider(this)[TrackerLocation::class.java]

        trackerLocation.requestLocationUpdates()
        trackerLocation.getLocationLiveData().observe(this) { location ->
            // Actualizar en el mapa la posición del usuario
//            updateLocation(location)
            UsuarioAcual.setLatitudLongitud(location.latitude, location.longitude)
        }
    }

    override fun onUsuariosActualizados() {
        usuariosConectados = UsuariosConectados.getUsuarios()

        if (usuariosConectados.isNotEmpty())
            Toast.makeText(
                this,
                "Usuario conectado ${usuariosConectados.last().nombreUsuario}",
                Toast.LENGTH_SHORT
            ).show()

        val adapter = UserListAdapter(this, usuariosConectados)
        listViewUsers.adapter = adapter
    }
}