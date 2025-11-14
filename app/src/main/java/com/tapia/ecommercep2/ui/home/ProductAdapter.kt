package com.tapia.ecommercep2.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tapia.ecommercep2.R
import com.tapia.ecommercep2.data.room.Product

class ProductAdapter(
    private val onEdit: (Product) -> Unit,
    private val onDelete: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var products: List<Product> = emptyList()

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTv: TextView = itemView.findViewById(R.id.tvName)
        val categoryTv: TextView = itemView.findViewById(R.id.tvCategory)
        val priceTv: TextView = itemView.findViewById(R.id.tvPrice)
        val btnEdit: ImageButton? = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton? = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val p = products[position]
        holder.nameTv.text = p.name
        holder.categoryTv.text = p.category
        holder.priceTv.text = "$${p.price}"

        holder.btnEdit?.setOnClickListener { onEdit(p) }
        holder.btnDelete?.setOnClickListener { onDelete(p) }
    }

    override fun getItemCount(): Int = products.size

    fun submitList(list: List<Product>) {
        products = list
        notifyDataSetChanged()
    }
}
