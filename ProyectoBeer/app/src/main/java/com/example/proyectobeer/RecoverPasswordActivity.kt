package com.example.proyectobeer

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectobeer.databinding.ActivityRecoverPasswordBinding
import com.example.proyectobeer.login.data.UserDTO
import com.example.proyectobeer.login.data.UserDataHelper
import com.example.proyectobeer.login.gui.LoginActivity
import com.google.android.material.button.MaterialButton

class RecoverPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecoverPasswordBinding
    private lateinit var db: UserDataHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityRecoverPasswordBinding.inflate(layoutInflater)
        this.db = UserDataHelper(this)
        setContentView(binding.root)

        // Botón Recuperar
        this.binding.buttonAccept.setOnClickListener {
            /* Manejo del evento del click del botón recuperar */
            val username = this.binding.inputUsername.text.toString().trim()
            val password = this.binding.inputPassword.text.toString().trim()
            val confirmPassword = this.binding.inputConfirmPassword.text.toString().trim()

            // Validar campos vacíos
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // Validar coincidencia de contraseñas
            if (password != confirmPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificar si el usuario existe
            val user = this.db.getUserByUsername(username)

            if (user == null) {
                Toast.makeText(this, "El usuario ingresado no existe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Actualizar la contraseña
            val updatedUser = UserDTO(user.id, username, password)

            this.db.updateUser(updatedUser)
            Toast.makeText(this, "Contraseña actualizada correctamente", Toast.LENGTH_LONG).show()

            // Limpiar campos
            this.binding.inputUsername.text?.clear()
            this.binding.inputPassword.text?.clear()
            this.binding.inputConfirmPassword.text?.clear()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Botón Volver
        this.binding.buttonBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
