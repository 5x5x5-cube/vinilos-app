package vinillos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vinillos.databinding.ActivityMainBinding
import vinillos.ui.album.AlbumFragment

class MainActivity : AppCompatActivity() {

    // Utilizando View Binding para referenciar las vistas de forma segura.
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflamos el layout de la actividad
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Si no hay un estado previo, se carga el Fragment inicial
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, AlbumFragment())
                .commitNow()
        }
    }
}
