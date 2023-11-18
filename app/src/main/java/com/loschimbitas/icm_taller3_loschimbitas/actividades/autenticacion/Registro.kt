package com.loschimbitas.icm_taller3_loschimbitas.actividades.autenticacion

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.loschimbitas.icm_taller3_loschimbitas.R
import com.loschimbitas.icm_taller3_loschimbitas.actividades.principal.PantallaPrincipal
import com.loschimbitas.icm_taller3_loschimbitas.databinding.ActivityRegistroBinding
import com.loschimbitas.icm_taller3_loschimbitas.modelo.Usuario
import com.google.firebase.storage.ktx.storage

import java.io.File

class Registro : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    // Variables para firebase database
    private val database = FirebaseDatabase.getInstance()
    private lateinit var myRef: DatabaseReference
    // Fin variables para firebase database

    // Variables para firebase auth
    private lateinit var auth: FirebaseAuth
    // Fin variables para firebase auth

    //variables para firebase storage
    val storage = Firebase.storage
    //fin variables para firebase storage

    // User variable
    private lateinit var usuario: Usuario


    // Variables para fotos e imagen
    private lateinit var tempImageUri: Uri
    private lateinit var urlImagenUser: String
    // Fin variables para fotos e imagen

    // Contratos de foto e imagen
    private val selectSinglePhotoContract =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            // Handle the returned Uri
            uri?.let { it ->
                tempImageUri = it
                val imageView = binding.profileImage
                imageView.setImageURI(it)
            }
        }

    private val takePhotoContract =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {

            if (it) {
                // Handle the photo uri
                val imageView = binding.profileImage
                imageView.setImageURI(tempImageUri)
            }
        }
//    Fin contratos foto e imagen


    // Gestion de permisos
    // Permiso Cámara
    private var cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                takePhotoContract.launch(tempImageUri)
            }
        }

    //   Permiso Galería
    private var galleryPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                selectSinglePhotoContract.launch(PickVisualMediaRequest())
            }
        }
//    Fin gestión de permisos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        usuario = Usuario()
        inicializar()
    }

    //   Confirmación de permisos al realizar alguna actividad
    private fun checkGalleryPermission() {
        galleryPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun checkCameraPermission() {
        cameraPermission.launch(android.Manifest.permission.CAMERA)
    }


    private fun inicializar() {
        iniciarListeners()
    }

    private fun iniciarListeners() {
        iniciarListenerBtnRegistrar()
    }

    private fun iniciarListenerBtnRegistrar() {
        binding.buttonRegistrarse.setOnClickListener {
            if (validateForm()) {
                uploadFile(tempImageUri.toString())
                registrarUsuarioAuth()
            }

//            val intent = Intent(this, AgregarFotoPerfil::class.java)
        }

        binding.BtnTakePhoto.setOnClickListener {
            tempImageUri = initTempUri()
            checkCameraPermission()
        }

        binding.BtnUploadPhoto.setOnClickListener {
            checkGalleryPermission()
        }

    }

    private fun registrarUsuarioAuth() {

        var email = binding.editTextCorreo.text.toString()
        var password = binding.editTextContrasena.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful)
                    val user = auth.currentUser
                    if (user != null) {
                        registrarUsuarioDb()
// Update user info
                        updateUI(user)
                    }
                } else {
                    Toast.makeText(
                        this, "createUserWithEmail:Failure: " + task.exception.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    task.exception?.message?.let { Log.e(TAG, it) }
                }
            }
    }

    private fun registrarUsuarioDb() {
        myRef = database.getReference("usuarios/${auth.currentUser?.uid}")
        val nombre = binding.editTextNombre.text.toString()
        val apellido = binding.editTextApellido.text.toString()
        val correo = binding.editTextCorreo.text.toString()
        val contrasena = binding.editTextContrasena.text.toString()
        usuario =
            Usuario(
                numeroAutenticacion = auth.currentUser?.uid?.toString(),
                nombreUsuario = binding.editTextNombreUsuario.text.toString(),
                nombre = nombre, apellido = apellido, correo = correo,
                contrasena = contrasena, imagenContacto = urlImagenUser,
                latitud = 0.0, longitud = 0.0
            )
        myRef.setValue(usuario)
    }

    private fun updateUI(user: FirebaseUser) {
        val intent = Intent(this, PantallaPrincipal::class.java)
        intent.putExtra("user", user)
        intent.putExtra("user_username", usuario.nombreUsuario)
        intent.putExtra("user_img", usuario.imagenContacto)
        startActivity(intent)
    }

    private fun validateForm(): Boolean {
        var valid = true
        val username = binding.editTextNombreUsuario.text.toString()
        if (TextUtils.isEmpty(username)) {
            binding.editTextNombreUsuario.error = "Required."
            valid = false
        } else {
            binding.editTextNombreUsuario.error = null
        }
        val name = binding.editTextNombre.text.toString()
        if (TextUtils.isEmpty(name)) {
            binding.editTextNombre.error = "Required."
            valid = false
        } else {
            binding.editTextNombre.error = null
        }
        val lastName = binding.editTextApellido.text.toString()
        if (TextUtils.isEmpty(lastName)) {
            binding.editTextApellido.error = "Required."
            valid = false
        } else {
            binding.editTextApellido.error = null
        }
        val email = binding.editTextCorreo.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.editTextCorreo.error = "Required."
            valid = false
        } else {
            binding.editTextCorreo.error = null
        }
        val password = binding.editTextContrasena.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.editTextContrasena.error = "Required."
            valid = false
        } else {
            binding.editTextContrasena.error = null
        }
        if (binding.profileImage.drawable == null || binding.profileImage.drawable.constantState == ContextCompat.getDrawable(
                this,
                R.drawable.icon_user
            )?.constantState
        ) {
            Toast.makeText(
                this,
                "Por favor, seleccione una foto de perfil.",
                Toast.LENGTH_SHORT
            )
                .show()
            valid = false
        }
        return valid
    }


    // Lógica para la foto de perfil
    private fun initTempUri(): Uri {

        val tempImagesDir = File(
            applicationContext.filesDir, //this function gets the external cache dir
            getString(R.string.temp_images_dir)
        ) //gets the directory for the temporary images dir

        tempImagesDir.mkdir() //Create the temp_images dir

        //Creates the temp_image.jpg file
        val tempImage = File(
            tempImagesDir, //prefix the new abstract path with the temporary images dir path
            getString(R.string.temp_image)
        ) //gets the abstract temp_image file name

        //Returns the Uri object to be used with ActivityResultLauncher
        return FileProvider.getUriForFile(
            applicationContext,
            getString(R.string.authorities),
            tempImage
        )
    }
    // Fin lofica para la foto de perfil

    // Lógica para subir la foto de perfil a firebase storage
    private fun uploadFile(uri: String) {
        val imageRef = storage.reference.child(uri)
        imageRef.putFile(uri.toUri())
            .addOnSuccessListener { // Get a URL to the uploaded content
                Log.i("FBApp", "Successfully uploaded image")
            }
            .addOnFailureListener {
                // Handle unsuccessful uploads
                // ...
                Log.w("bro", "noooo, no sirvió")
            }
        urlImagenUser = imageRef.downloadUrl.toString()
    }
    // Fin lógica para subir la foto de perfil a firebase storage

}