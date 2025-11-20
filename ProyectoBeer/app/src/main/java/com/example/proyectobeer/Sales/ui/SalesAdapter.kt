package com.example.proyectobeer

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectobeer.Sales.data.SalesDTO
import com.example.proyectobeer.Sales.data.SalesDataBaseHelper
import com.example.proyectobeer.Sales.ui.FormSalesActivity
import com.google.android.material.button.MaterialButton


class SalesAdapter(private var sales: List<SalesDTO>, private var context: Context) :
    RecyclerView.Adapter<SalesAdapter.SalesViewHolder>() {

    private val db: SalesDataBaseHelper = SalesDataBaseHelper(context)

    class SalesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val customerName = itemView.findViewById<TextView>(R.id.idCustomer)
        val salesId = itemView.findViewById<TextView>(R.id.idSales)
        val updateButton = itemView.findViewById<MaterialButton>(R.id.buttonUpdate)
        val deleteButton = itemView.findViewById<MaterialButton>(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SalesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_sales, parent, false)
        return SalesViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SalesViewHolder,
        position: Int
    ) {
        val sale = this.sales[position]
        holder.salesId.text = sale.salesId.toString()
        holder.customerName.text = sale.customerName

        // Click en el nombre del cliente para ver más información
        holder.customerName.setOnClickListener {
            showSalesDetailsDialog(sale)
        }

        holder.deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle("Confirmar acción")
            builder.setMessage("¿Estás seguro de que quieres eliminar esta venta?")

            builder.setPositiveButton("Sí") { dialog, _ ->
                db.deleteSaleById(sale.salesId)
                this.refreshData(db.getAllSales())
                Toast.makeText(holder.itemView.context, "¡Venta eliminada!", Toast.LENGTH_SHORT)
                    .show()
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, FormSalesActivity::class.java).apply {
                putExtra("action_type", "UPDATE")
                putExtra("sales_id", sale.salesId)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return sales.size
    }

    fun refreshData(newSales: List<SalesDTO>) {
        this.sales = newSales
        notifyDataSetChanged()
    }

    private fun showSalesDetailsDialog(sale: SalesDTO) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Detalles de Venta #${sale.salesId}")

        val message = """
            ID de Venta: ${sale.salesId}
            Número de Serie de Cerveza: ${sale.numberSerialofBeer}
            Cantidad Vendida: ${sale.numberofBeerSold}
            Precio Total: ${sale.priceTotal}
            Fecha de Venta: ${sale.dataofSale}
            Vendedor: ${sale.userName}
            Cliente: ${sale.customerName}
        """.trimIndent()

        builder.setMessage(message)
        builder.setPositiveButton("Cerrar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}