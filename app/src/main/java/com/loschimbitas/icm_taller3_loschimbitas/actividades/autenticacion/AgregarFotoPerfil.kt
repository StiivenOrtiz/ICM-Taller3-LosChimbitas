package com.loschimbitas.icm_taller3_loschimbitas.actividades.autenticacion

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.loschimbitas.icm_taller3_loschimbitas.R
import com.loschimbitas.icm_taller3_loschimbitas.actividades.principal.PantallaPrincipal
import com.loschimbitas.icm_taller3_loschimbitas.databinding.ActivityAgregarFotoPerfilBinding
import com.loschimbitas.icm_taller3_loschimbitas.globales.Globales.Companion.userGlobal
import java.io.File

class AgregarFotoPerfil : AppCompatActivity() {

    private val selectSinglePhotoContract =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let { it ->
                tempImageUri = it
                val imageView = binding.imageViewFotoPerfil
                imageView.setImageURI(it)
            }
        }

    private val takePhotoContract =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                val imageView = binding.imageViewFotoPerfil
                imageView.setImageURI(tempImageUri)
            }
        }

    private var cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                tempImageUri = initTempUri()
                takePhotoContract.launch(tempImageUri)
            }
        }

    private var galleryPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                selectSinglePhotoContract.launch(PickVisualMediaRequest())
            }
        }

    private var tempImageUri: Uri? = null

    private lateinit var binding: ActivityAgregarFotoPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarFotoPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inicializar()
    }

    private fun checkGalleryPermission() {
        galleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun checkCameraPermission() {
        cameraPermission.launch(Manifest.permission.CAMERA)
    }

    private fun inicializar() {
        iniciarListeners()
    }

    private fun iniciarListeners() {
        iniciarListenerBtnTomarFoto()
        iniciarListenerBtnCargarFoto()
        iniciarListenerBtnGuardarCambios()
    }

    private fun iniciarListenerBtnCargarFoto() {
        binding.btnCargarFoto.setOnClickListener {
            checkGalleryPermission()
        }
    }

    private fun iniciarListenerBtnTomarFoto() {
        binding.btnTomarFoto.setOnClickListener {
            checkCameraPermission()
        }
    }

    private fun iniciarListenerBtnGuardarCambios() {
        binding.btnGuardarCambios.setOnClickListener {
            userGlobal.imagenContacto = tempImageUri.toString()
            Toast.makeText(this, "Configuration saved", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, PantallaPrincipal::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun initTempUri(): Uri? {
        val tempImagesDir = File(
            applicationContext.filesDir,
            getString(R.string.temp_images_dir)
        )

        tempImagesDir.mkdir()

        val tempImage = File(
            tempImagesDir,
            getString(R.string.temp_image)
        )

        return FileProvider.getUriForFile(
            applicationContext,
            getString(R.string.authorities),
            tempImage
        )
    }

}