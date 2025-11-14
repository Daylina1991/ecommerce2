package com.tapia.ecommercep2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tapia.ecommercep2.data.room.AppDatabase
import com.tapia.ecommercep2.data.room.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).productDao()
    val products: LiveData<List<Product>> = dao.getAllProducts()

    fun addProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(product)
        }
    }
}
