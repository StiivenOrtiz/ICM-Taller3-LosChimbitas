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
import com.loschimbitas.icm_taller3_loschimbitas.databinding.ActivityUserListBinding
import com.loschimbitas.icm_taller3_loschimbitas.globales.UsuarioActual
import com.loschimbitas.icm_taller3_loschimbitas.globales.UsuariosConectados
import com.loschimbitas.icm_taller3_loschimbitas.util.TrackerLocation

class UserListActivity : AppCompatActivity(), UsuariosConectados.UsuariosConectadosObserver{

    private var usuariosConectados = UsuariosConectados.getUsuarios().toMutableList()
    private lateinit var trackerLocation: TrackerLocation
    private lateinit var binding: ActivityUserListBinding
    private var adapter: UserListAdapter? = null

    private var latitudAnterior = 0.0
    private var longitudAnterior = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        UsuariosConectados.agregarObserver(this)

        adapter = UserListAdapter(this, usuariosConectados)

        // Configurar un adaptador personalizado para la ListView
        binding.listViewUsers.adapter = adapter

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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        UsuariosConectados.quitarObserver(this)
        trackerLocation.stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        UsuariosConectados.agregarObserver(this)
        trackerLocation.requestLocationUpdates()
    }

    override fun onUsuariosActualizados() {
        val newUpdate = UsuariosConectados.getUsuarios()
        for (usuario in newUpdate){
            // Si hay un usuario que no se contenia
            if (!usuariosConectados.contains(usuario)){
                usuariosConectados.add(usuario)
            }
        }

        for (usuario in usuariosConectados){
            // Si hay un usuario que no se contenia
            if (!newUpdate.contains(usuario)){
                usuariosConectados.remove(usuario)
            }
        }

        adapter?.notifyDataSetChanged()
    }

    private fun trackUser(){
        trackerLocation = ViewModelProvider(this)[TrackerLocation::class.java]

        trackerLocation.requestLocationUpdates()
        trackerLocation.getLocationLiveData().observe(this) { location ->
            // Actualizar en el mapa la posición del usuario
            if(latitudAnterior != location.latitude || longitudAnterior != location.longitude) {
                UsuarioActual.setLatitudLongitud(location.latitude, location.longitude)
                latitudAnterior = location.latitude
                longitudAnterior = location.longitude
            }
        }
    }
}