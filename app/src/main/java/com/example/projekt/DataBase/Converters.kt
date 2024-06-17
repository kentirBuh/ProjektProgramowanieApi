package com.example.projekt.DataBase

import androidx.room.TypeConverter
import com.example.projekt.Model.Ingredient
import com.example.projekt.Model.Recipe
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromIngredientList(value: List<Ingredient>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toIngredientList(value: String): List<Ingredient> {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromRecipeList(value: List<Recipe>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toRecipeList(value: String): List<Recipe> {
        return json.decodeFromString(value)
    }
}