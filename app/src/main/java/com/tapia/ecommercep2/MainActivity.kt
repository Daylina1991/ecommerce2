package com.tapia.ecommercep2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tapia.ecommercep2.data.room.Product
import com.tapia.ecommercep2.databinding.ActivityMainBinding
import com.tapia.ecommercep2.databinding.DialogProductBinding
import com.tapia.ecommercep2.ui.home.ProductAdapter
import com.tapia.ecommercep2.viewmodel.ProductViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vm: ProductViewModel by viewModels()
    private lateinit var adapter: ProductAdapter   // <-- late init

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar) // Asegúrate que activity_main.xml tenga toolbar con id "toolbar"

        // Inicializa el adapter AQUI (donde vm ya está listo)
        adapter = ProductAdapter(
            onEdit = { showEditDialog(it) },
            onDelete = { vm.deleteProduct(it) }
        )

        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = adapter

        // Observamos los productos desde Room
        vm.products.observe(this) { list ->
            adapter.submitList(list ?: emptyList()) // seguridad por si es null
        }

        // FAB para agregar producto
        binding.fabAdd.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val dialogBinding = DialogProductBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setTitle("Nuevo producto")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { d, _ ->
                val product = readProduct(dialogBinding)
                if (product != null) {
                    vm.addProduct(product)
                } else {
                    Toast.makeText(this, "Corrige los campos", Toast.LENGTH_SHORT).show()
                }
                d.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showEditDialog(existing: Product) {
        val dialogBinding = DialogProductBinding.inflate(layoutInflater)
        dialogBinding.etName.setText(existing.name)
        dialogBinding.etCategory.setText(existing.category)
        dialogBinding.etPrice.setText(existing.price.toString())
        dialogBinding.etStock.setText(existing.stock.toString())
        dialogBinding.etDescription.setText(existing.description)

        AlertDialog.Builder(this)
            .setTitle("Editar producto")
            .setView(dialogBinding.root)
            .setPositiveButton("Actualizar") { d, _ ->
                val updated = readProduct(dialogBinding)?.copy(id = existing.id)
                if (updated != null) {
                    vm.updateProduct(updated)
                } else {
                    Toast.makeText(this, "Corrige los campos", Toast.LENGTH_SHORT).show()
                }
                d.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun readProduct(b: DialogProductBinding): Product? {
        val name = b.etName.text.toString().trim()
        val category = b.etCategory.text.toString().trim()
        val price = b.etPrice.text.toString().toDoubleOrNull()
        val stock = b.etStock.text.toString().toIntOrNull()
        val desc = b.etDescription.text.toString().trim()

        if (name.isEmpty()) {
            b.etName.error = "El nombre es obligatorio"
            return null
        }
        if (category.isEmpty()) {
            b.etCategory.error = "La categoría es obligatoria"
            return null
        }
        if (price == null) {
            b.etPrice.error = "Precio inválido"
            return null
        }
        if (stock == null) {
            b.etStock.error = "Stock inválido"
            return null
        }

        return Product(
            name = name,
            category = category,
            price = price,
            stock = stock,
            description = desc
        )
    }
}

