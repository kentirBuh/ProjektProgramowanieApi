package com.example.projekt.DataBase
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.projekt.Model.Ingredient
import com.example.projekt.Model.Recipe

@Dao
interface IngredientDao
{
    @Query("Select * from INGREDIENTS")
    fun getAllIngredient() : List<Ingredient>

    @Insert
    fun addIngredient(ingredient:Ingredient)

    @Query("Delete from ingredients where id = :id")
    fun deleteIngredient(id:Int)
}

@Dao
interface RecipeDao
{
    @Query("Select * from RECIPES")
    fun getAllRecipe() : List<Recipe>

    @Insert
    fun addRecipe(recipe:Recipe)

    @Insert
    fun addIngredients(ingredients:List<Ingredient>)

    @Query("Delete from recipes where id = :id")
    fun deleteRecipe(id:Int)
}