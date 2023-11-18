package com.loschimbitas.icm_taller3_loschimbitas.actividades.principal

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.loschimbitas.icm_taller3_loschimbitas.MainActivity
import com.loschimbitas.icm_taller3_loschimbitas.R
import com.loschimbitas.icm_taller3_loschimbitas.databinding.ActivityMapsBinding
import com.loschimbitas.icm_taller3_loschimbitas.globales.UsuarioAcual
import com.loschimbitas.icm_taller3_loschimbitas.globales.UsuariosConectados
import com.loschimbitas.icm_taller3_loschimbitas.modelo.Usuario
import com.loschimbitas.icm_taller3_loschimbitas.util.TrackerLocation
import org.json.JSONObject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, UsuariosConectados.UsuariosConectadosObserver {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var auth: FirebaseAuth
    var usuariosConectados = UsuariosConectados.getUsuarios()
    private lateinit var trackerLocation: TrackerLocation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
        auth = Firebase.auth
        UsuariosConectados.agregarObserver(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        supportActionBar?.title = "Los Chimbitas"
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menuCerrarSesion -> {
                auth.signOut()
                UsuarioAcual.setUsuario(Usuario())

                val intent = Intent(this, MainActivity::class.java)

                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)

                return true
            }

            R.id.menuEstado -> {
                if (UsuarioAcual.getEstadoUsuarioActual() == true) {
                    UsuarioAcual.setEstadoUsuarioActual(false)
                    Toast.makeText(
                        this,
                        "Estado cambiado a desconectado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else if (UsuarioAcual.getEstadoUsuarioActual() == false) {
                    UsuarioAcual.setEstadoUsuarioActual(true)
                    Toast.makeText(
                        this,
                        "Estado cambiado a conectado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                true
            }

            R.id.menuListarUsuarios -> {
                // Manejar la acción de la lista de usuarios disponibles
                val intent = Intent(this, UserListActivity::class.java)
                startActivity(intent)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Solicitar permisos de ubicación si no están otorgados
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

            // Configurar el mapa
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
            mMap.uiSettings.isZoomGesturesEnabled = true
            mMap.uiSettings.isZoomControlsEnabled = true


            // Obtener la ubicación actual del usuario y agregar un marcador
            val locationProvider = LocationServices.getFusedLocationProviderClient(this)
            locationProvider.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.addMarker(MarkerOptions().position(currentLatLng).title("Ubicación Actual"))
                }
            }


            // Obtener el JSON desde el archivo raw
            val inputStream = resources.openRawResource(R.raw.locations)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val locations = JSONObject(jsonString)

            // Acceder a las ubicaciones y agregar marcadores en el mapa
            val locationsArray = locations.getJSONArray("locationsArray")

            for (i in 0 until locationsArray.length()) {
                val location = locationsArray.getJSONObject(i)
                val latitude = location.getDouble("latitude")
                val longitude = location.getDouble("longitude")
                val name = location.getString("name")

                val poiLatLng = LatLng(latitude, longitude)
                mMap.addMarker(MarkerOptions().position(poiLatLng).title(name))
            }

            // Centrar el mapa en la última ubicación del JSON
            val lastLocation = locationsArray.getJSONObject(locationsArray.length() - 1)
            val lastLatitude = lastLocation.getDouble("latitude")
            val lastLongitude = lastLocation.getDouble("longitude")
            val lastLatLng = LatLng(lastLatitude, lastLongitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 12.0f))
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
        // Mostrar un Toast para cada nuevo usuario
        usuariosConectados = UsuariosConectados.getUsuarios()
        Toast.makeText(
            this,
            "Usuarios actualizados: ${usuariosConectados.size}",
            Toast.LENGTH_SHORT
        ).show()

        usuariosConectados.forEach() {
            Log.i("UsuariosConectados", "nombre usuario: ${it.nombreUsuario}")
        }
    }
}