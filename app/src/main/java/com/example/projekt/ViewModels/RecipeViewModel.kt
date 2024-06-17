import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projekt.Model.Recipe

class RecipeViewModel : ViewModel() {
    private val selectedRecipe = MutableLiveData<Recipe>()

    fun selectRecipe(recipe: Recipe) {
        selectedRecipe.value = recipe
    }

    fun getSelectedRecipe(): LiveData<Recipe> {
        return selectedRecipe
    }
}