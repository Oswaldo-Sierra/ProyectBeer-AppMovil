package com.example.proyectobeer.beer.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectobeer.BeerAdapter
import com.example.proyectobeer.MainActivity
import com.example.proyectobeer.beer.data.BeerDataBaseHelper
import com.example.proyectobeer.databinding.ActivityShowBeerBinding

class ShowBeerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowBeerBinding
    private lateinit var bd : BeerDataBaseHelper
    private lateinit var adapter: BeerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.binding = ActivityShowBeerBinding.inflate(layoutInflater)
        this.bd = BeerDataBaseHelper(this)
        this.adapter = BeerAdapter(this.bd.getAllBeers(),this)
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
        adapter.refreshData(bd.getAllBeers())
    }
}