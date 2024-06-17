package com.example.projekt

import android.content.Intent
import android.util.Log
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.projekt.DataBase.IngredientDao
import com.example.projekt.DataBase.RecipeDatabase
import com.example.projekt.Model.Ingredient
import com.example.projekt.Model.Recipe
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val etRecipeName: EditText = findViewById(R.id.et_recipe_name)
        val etCookingTime: EditText = findViewById(R.id.et_cooking_time)
        val etInstructions: EditText = findViewById(R.id.et_instructions)
        val ingredientsContainer: LinearLayout = findViewById(R.id.ingredients_container)
        val btnAddIngredient: Button = findViewById(R.id.btn_add_ingredient)
        val btnSaveRecipe: Button = findViewById(R.id.btn_save_recipe)



        val db = Room.databaseBuilder(
            applicationContext,
            RecipeDatabase::class.java,
            RecipeDatabase.NAME
        ).build()


        bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_timer -> {
                    // Handle timer navigation
                    true
                }
                R.id.navigation_create -> {

                    true
                }
                R.id.navigation_view -> {
                    startActivity(Intent(this@MainActivity, RecipeListActivity::class.java))
                    true
                }
                else -> false
            }
        })

        btnAddIngredient.setOnClickListener {
            // Inflate a new ingredient row
            val ingredientRow = layoutInflater.inflate(R.layout.ingredient_row, null)
            ingredientsContainer.addView(ingredientRow)
        }

        btnSaveRecipe.setOnClickListener {
            val recipeName = etRecipeName.text.toString()
            val cookingTimeString = etCookingTime.text.toString()
            val cookingTime = if (cookingTimeString.isNotEmpty())
            {
                cookingTimeString.toInt()
            }
            else
            {
                0
            }
            val instructions = etInstructions.text.toString()

            val ingredients = mutableListOf<Ingredient>()
            for (i in 0 until ingredientsContainer.childCount) {
                val ingredientRow = ingredientsContainer.getChildAt(i)
                val etIngredientName: EditText = ingredientRow.findViewById(R.id.et_ingredient_name)
                val etIngredientQuantity: EditText = ingredientRow.findViewById(R.id.et_ingredient_quantity)
                val etIngredientUnit: EditText = ingredientRow.findViewById(R.id.et_ingredient_unit)

                val ingredientName = etIngredientName.text.toString()
                val ingredientQuantityString = etIngredientQuantity.text.toString()


                val ingredientQuantity = ingredientQuantityString.toDoubleOrNull() ?: 0.0

                val ingredientUnit = etIngredientUnit.text.toString()

                val ingredient = Ingredient(id=0,name= ingredientName, quantity =  ingredientQuantity, unit =  ingredientUnit)
                ingredients.add(ingredient)
            }
            lifecycleScope.launch(Dispatchers.IO) {
                db.recipeDao().addIngredients(ingredients)
            }

            val recipe = Recipe(
                name = recipeName,
                cookingTime = cookingTime,
                instructions = instructions,
                ingredients = ingredients
            )

            lifecycleScope.launch(Dispatchers.IO) {

                db.recipeDao().addRecipe(recipe)
                Log.i("tag1","Recipe added")
            }


        }

    }
}
