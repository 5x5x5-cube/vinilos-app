package vinillos.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import vinillos.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding
    private val albumViewModel: AlbumViewModel by viewModels {
        AlbumViewModelFactory() // si necesitas un factory para inyectar un Repository
    }

    private val albumAdapter = AlbumAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView
        binding.recyclerViewAlbums.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAlbums.adapter = albumAdapter

        // Observa el LiveData de álbumes
        albumViewModel.albums.observe(viewLifecycleOwner, Observer { albums ->
            albumAdapter.submitList(albums)
        })

        // Llama al ViewModel para cargar los álbumes
        albumViewModel.loadAlbums()

        // Configura el botón para agregar (opcional)
        binding.fabAddAlbum.setOnClickListener {
            // Aquí inicias la lógica para agregar un nuevo álbum
        }
    }
}
