package com.example.proyectobeer.login.gui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectobeer.IntroActivity
import com.example.proyectobeer.MainActivity
import com.example.proyectobeer.databinding.ActivitySingUpUserBinding
import com.example.proyectobeer.login.data.UserDTO
import com.example.proyectobeer.login.data.UserDataHelper

class SingUpUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingUpUserBinding
    private lateinit var bd: UserDataHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        this.binding = ActivitySingUpUserBinding.inflate(layoutInflater)
        this.bd = UserDataHelper(this)
        setContentView(binding.root)

        this.binding.buttonAccept.setOnClickListener {
            val username = this.binding.inputUsername.text.toString()
            val password = this.binding.inputPassword.text.toString()
            val confirmationPassword = this.binding.inputConfirmPassword.text.toString()
            // Validar campos vacíos
            if (username.isEmpty() || password.isEmpty() || confirmationPassword.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar contraseña
            if (password != confirmationPassword) {
                Toast.makeText(this, "La contraseña no coincide", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /* Validar si el nombre de usuario ya existe */
            if (this.bd.getUserByUsername(username) != null) {
                /* El nombre de usuario ya está tomado */
                Toast.makeText(
                    this, "¡El nombre de usuario ya existe!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                /* Se agrega el registro en BD*/
                val userDTO = UserDTO(0, username, password)
                this.bd.insertUser(userDTO)
                Toast.makeText(
                    this, "¡Registro de usuario agregado!",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("logged_user_name", username)
            }
            /* Se limpian los datos */
            this.binding.inputUsername.setText("")
            this.binding.inputPassword.setText("")
            this.binding.inputConfirmPassword.setText("")


        }


        this.binding.buttonBack.setOnClickListener {
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }


    }
}