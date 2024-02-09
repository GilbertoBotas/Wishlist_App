package mz.gilib.mywishlistapp

import android.content.Context
import androidx.room.Room
import mz.gilib.mywishlistapp.data.WishDatabase
import mz.gilib.mywishlistapp.data.WishRepository

object Graph {
    private lateinit var database: WishDatabase

    val wishRepository by lazy {
        WishRepository(database.wishDao())
    }

    fun provide(context: Context){
        database = Room.databaseBuilder(context, WishDatabase::class.java, "wishlist.db").build()
    }
}