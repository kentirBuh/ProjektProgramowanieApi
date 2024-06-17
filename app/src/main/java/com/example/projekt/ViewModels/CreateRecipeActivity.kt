package com.example.projekt.ViewModels

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt.Model.Ingredient
import com.example.projekt.Model.Recipe
import com.example.projekt.R
import android.util.Log

class CreateRecipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etRecipeName: EditText = findViewById(R.id.et_recipe_name)
        val etCookingTime: EditText = findViewById(R.id.et_cooking_time)
        val etInstructions: EditText = findViewById(R.id.et_instructions)
        val ingredientsContainer: LinearLayout = findViewById(R.id.ingredients_container)
        val btnAddIngredient: Button = findViewById(R.id.btn_add_ingredient)
        val btnSaveRecipe: Button = findViewById(R.id.btn_save_recipe)

        btnAddIngredient.setOnClickListener {

            val ingredientRow = layoutInflater.inflate(R.layout.ingredient_row, null)
            ingredientsContainer.addView(ingredientRow)
        }

        btnSaveRecipe.setOnClickListener {
            val recipeName = etRecipeName.text.toString()
            val cookingTime = etCookingTime.text.toString().toInt()
            val instructions = etInstructions.text.toString()

            val ingredients = mutableListOf<Ingredient>()
            for (i in 0 until ingredientsContainer.childCount) {
                val ingredientRow = ingredientsContainer.getChildAt(i)
                val etIngredientName: EditText = ingredientRow.findViewById(R.id.et_ingredient_name)
                val etIngredientQuantity: EditText = ingredientRow.findViewById(R.id.et_ingredient_quantity)
                val etIngredientUnit: EditText = ingredientRow.findViewById(R.id.et_ingredient_unit)

                val ingredientName = etIngredientName.text.toString()
                val ingredientQuantity = etIngredientQuantity.text.toString().toDouble()
                val ingredientUnit = etIngredientUnit.text.toString()

                val ingredient = Ingredient(name=ingredientName, quantity=ingredientQuantity, unit=ingredientUnit)
                ingredients.add(ingredient)
            }

            val recipe = Recipe(
                name = recipeName,
                cookingTime = cookingTime,
                instructions = instructions,
                ingredients = ingredients
            )

            // Handle saving the recipe object, e.g., save to database or pass to another activity
        }
    }
}