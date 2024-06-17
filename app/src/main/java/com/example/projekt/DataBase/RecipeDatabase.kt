package com.example.projekt.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.projekt.Model.Ingredient
import com.example.projekt.Model.Recipe

@Database(entities = [Recipe::class, Ingredient::class], version = 1)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun ingredientDao(): IngredientDao

    abstract fun recipeDao(): RecipeDao

    companion object {

        const val NAME = "Recipe_DB"

        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getInstance(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }


}