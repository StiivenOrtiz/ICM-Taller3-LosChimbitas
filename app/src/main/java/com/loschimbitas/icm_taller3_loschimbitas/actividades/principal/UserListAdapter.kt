package com.loschimbitas.icm_taller3_loschimbitas.actividades.principal

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.loschimbitas.icm_taller3_loschimbitas.R
import com.loschimbitas.icm_taller3_loschimbitas.modelo.Usuario

class UserListAdapter(private val mContext: Context, private val userList: List<Usuario>) :
    ArrayAdapter<Usuario>(mContext, 0, userList) {

    @SuppressLint("ViewHolder") // Suprimir advertencia
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View { // Al crear la vista
        val layout = LayoutInflater.from(mContext)
            .inflate(R.layout.list_item_user, parent, false) // Establecer el layout

        Glide.with(mContext)
            .load(userList[position].imagenContacto)
            .into(layout.findViewById(R.id.imageViewUser))

        Log.i("UserListAdapter", "imagen: ${userList[position].imagenContacto}")

        // textViewUserName
        layout.findViewById<TextView>(R.id.textViewUserName).text =
            userList[position].nombre

        // btnCurrentUserLocation
        layout.findViewById<Button>(R.id.btnCurrentUserLocation).setOnClickListener {
            // Lanzara una activity con el mapa
            val intent = Intent(mContext, MapsActivity::class.java)
        }

        return layout // Retornar la vista
    }
}

