package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projekt.DataBase.RecipeDao
import com.example.projekt.DataBase.RecipeDatabase
import com.example.projekt.Model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var recipeDao: RecipeDao
    private lateinit var recipes: List<Recipe>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)
        Log.d("afterSetContentView", "AfterSetContent")

        listView = findViewById(R.id.list_recipes)
        recipeDao = RecipeDatabase.getInstance(this).recipeDao()

        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("beforeDao", "This logcat happens before we get any recipes")
            recipes = recipeDao.getAllRecipe()
            withContext(Dispatchers.Main) {
                Log.d("RecipeListActivity", "Number of recipes fetched: ${recipes.size}")
                setupListView()
            }
        }
    }

    private fun setupListView() {
        val recipeNames = recipes.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, recipeNames)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedRecipe = recipes[position]
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("recipe", selectedRecipe) // Pass the Recipe object directly
            startActivity(intent)
        }
    }
}
