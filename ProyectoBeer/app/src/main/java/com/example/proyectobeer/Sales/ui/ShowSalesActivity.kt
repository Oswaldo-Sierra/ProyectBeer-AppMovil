package com.example.proyectobeer.Sales.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectobeer.SalesAdapter
import com.example.proyectobeer.MainActivity
import com.example.proyectobeer.Sales.data.SalesDataBaseHelper
import com.example.proyectobeer.databinding.ActivityShowSalesBinding

class ShowSalesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowSalesBinding
    private lateinit var bd: SalesDataBaseHelper
    private lateinit var adapter: SalesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.binding = ActivityShowSalesBinding.inflate(layoutInflater)
        this.bd = SalesDataBaseHelper(this)
        this.adapter = SalesAdapter(this.bd.getAllSales(), this)
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
        adapter.refreshData(bd.getAllSales())
    }
}