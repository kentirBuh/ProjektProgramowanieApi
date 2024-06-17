package com.example.projekt

import android.app.Application
import androidx.room.Room
import com.example.projekt.DataBase.RecipeDao
import com.example.projekt.DataBase.RecipeDatabase

class MainAplication : Application() {
    companion object {
        lateinit var recipeDatabase: RecipeDatabase
    }

    override fun onCreate() {
        super.onCreate()
        recipeDatabase = Room.databaseBuilder(
            applicationContext,
            RecipeDatabase::class.java,
            RecipeDatabase.NAME
        ).build()
    }
}