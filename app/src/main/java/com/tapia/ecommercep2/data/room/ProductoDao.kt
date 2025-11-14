package com.tapia.ecommercep2.data.room

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ProductDao {

    @Insert
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<Product>>  

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getById(id: Int): Product?
}
