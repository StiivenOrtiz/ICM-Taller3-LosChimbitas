package com.loschimbitas.icm_taller3_loschimbitas.actividades.autenticacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.loschimbitas.icm_taller3_loschimbitas.R
import com.loschimbitas.icm_taller3_loschimbitas.databinding.ActivityIniciarSesionBinding

class IniciarSesion : AppCompatActivity() {

    private lateinit var binding: ActivityIniciarSesionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIniciarSesionBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_iniciar_sesion)
    }
}