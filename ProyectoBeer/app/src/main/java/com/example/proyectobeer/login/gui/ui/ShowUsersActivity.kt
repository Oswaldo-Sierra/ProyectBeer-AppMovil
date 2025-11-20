package com.example.proyectobeer.login.gui.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectobeer.MainActivity
import com.example.proyectobeer.login.gui.UserAdapter
import com.example.proyectobeer.databinding.ActivityShowUsersBinding
import com.example.proyectobeer.login.data.UserDataHelper

class ShowUsersActivity : AppCompatActivity() {
    /* Creación del binding */
    private lateinit var binding: ActivityShowUsersBinding
    /* Creación del utilitario de BD */
    private lateinit var bd: UserDataHelper
    /* Creación del adaptador */
    private lateinit var adapter: UserAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.binding = ActivityShowUsersBinding.inflate(layoutInflater)
        this.bd = UserDataHelper(this)
        this.adapter = UserAdapter(this.bd.getUser(), this)
        setContentView(binding.root)

        binding.idRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.idRecyclerView.adapter = this.adapter

        binding.idButtonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.refreshData(bd.getUser())
    }
}