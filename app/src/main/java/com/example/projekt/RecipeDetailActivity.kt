package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt.Model.Recipe
import com.example.projekt.R.id
import com.example.projekt.ViewModels.DisplayAlarmsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var textRecipeName: TextView
    private lateinit var textCookingTime: TextView
    private lateinit var textInstructions: TextView
    private lateinit var containerIngredients: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        textRecipeName = findViewById(id.text_recipe_name)
        textCookingTime = findViewById(id.text_cooking_time)
        textInstructions = findViewById(id.text_instructions)
        containerIngredients = findViewById(id.container_ingredients)
        val bottomNavigationView = findViewById<BottomNavigationView>(id.bottom_navigation)

        val recipe = intent.getParcelableExtra<Recipe>("recipe")

        if (recipe != null) {
            textRecipeName.text = recipe.name
            textCookingTime.text = "Cooking Time: ${recipe.cookingTime} minutes"
            textInstructions.text = recipe.instructions

            // Populate ingredients
            recipe.ingredients.forEach { ingredient ->
                val textView = TextView(this)
                textView.text = "${ingredient.name}: ${ingredient.quantity} ${ingredient.unit}"
                containerIngredients.addView(textView)
            }
        } else {
            // Handle the case when the recipe is null
            Log.e("RecipeDetailActivity", "Recipe object is null")
            finish() // Close the activity as it cannot function without the recipe data
        }

        bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_timer -> {
                    startActivity(Intent(this@RecipeDetailActivity, DisplayAlarmsActivity::class.java))
                    true
                }

                R.id.navigation_create -> {
                    startActivity(Intent(this@RecipeDetailActivity, MainActivity::class.java))
                    true
                }

                R.id.navigation_view -> {
                    startActivity(Intent(this@RecipeDetailActivity, RecipeTimer::class.java))
                    true

                }

                else -> false
            }
        })
    }
}
