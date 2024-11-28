import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pexeso.viewmodel.GameViewModel

/**
 * GameViewModelFactory is a factory class responsible for creating instances of the GameViewModel.
 * It implements the ViewModelProvider.Factory interface to provide the GameViewModel with a listener.
 */
class GameViewModelFactory (
    private val listener: GameViewModel.GameListener
): ViewModelProvider.Factory {

    /**
     * This method creates and returns an instance of the GameViewModel.
     * It provides the GameViewModel with the necessary listener dependency.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(listener) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}