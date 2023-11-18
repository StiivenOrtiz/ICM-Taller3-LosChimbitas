package com.loschimbitas.icm_taller3_loschimbitas.actividades.principal

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.loschimbitas.icm_taller3_loschimbitas.R
import com.loschimbitas.icm_taller3_loschimbitas.modelo.Usuario

class UserListAdapter(private val context: Context, private val userList: List<Usuario>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return userList.size
    }

    override fun getItem(position: Int): Any {
        return userList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(
                R.layout.list_item_user, parent, false
            )
            viewHolder = ViewHolder(
                view.findViewById(R.id.imageViewUser),
                view.findViewById(R.id.textViewUserName),
                view.findViewById(R.id.btnCurrentUserLocation)
            )
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val user = userList[position]

        viewHolder.imageViewUser.setImageResource(R.drawable.perfil)
        viewHolder.textViewUserName.text = "${user.nombre} ${user.apellido}"

        viewHolder.btnCurrentUserLocation.setOnClickListener {



        }

        return view
    }

    // ViewHolder para mejorar el rendimiento de la ListView
    private data class ViewHolder(
        val imageViewUser: ImageView,
        val textViewUserName: TextView,
        val btnCurrentUserLocation: Button
    )
}

