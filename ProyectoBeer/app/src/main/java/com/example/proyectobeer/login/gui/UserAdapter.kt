package com.example.proyectobeer.login.gui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectobeer.R
import com.example.proyectobeer.login.data.UserDTO
import com.example.proyectobeer.login.data.UserDataHelper
import com.google.android.material.button.MaterialButton

class UserAdapter(private var users: List<UserDTO>, private var context: Context) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val db: UserDataHelper = UserDataHelper(context)

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username = itemView.findViewById<TextView>(R.id.idUserName)
        val password = itemView.findViewById<TextView>(R.id.idUserPassword)
        val updateButton = itemView.findViewById<MaterialButton>(R.id.buttonUpdate)
        val deleteButton = itemView.findViewById<MaterialButton>(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) {
        val user = this.users[position]
        holder.username.text = user.nameuser
        holder.password.text = user.passwordUser
        holder.updateButton.setOnClickListener {

        }
        holder.deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle("Confirmar acción")
            builder.setMessage("¿Estás seguro de que quieres eliminar este registro?")

            builder.setPositiveButton("Sí") { dialog, _ ->
                db.deleteUserById(user.id)
                this.refreshData(db.getUser())
                Toast.makeText(holder.itemView.context, "¡Usuario eliminado!", Toast.LENGTH_SHORT)
                    .show()
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, FormUserActivity::class.java).apply {
                putExtra("action_type", "UPDATE")
                putExtra("user_id", user.id)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun refreshData(newUsers: List<UserDTO>) {
        this.users = newUsers
        notifyDataSetChanged()
    }


}