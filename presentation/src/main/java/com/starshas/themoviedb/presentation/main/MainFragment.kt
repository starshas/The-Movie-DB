package com.starshas.themoviedb.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.starshas.themoviedb.domain.models.DomainMoviesInfo.Movie
import com.starshas.themoviedb.presentation.R
import com.starshas.themoviedb.presentation.databinding.FragmentMainBinding
import com.starshas.themoviedb.presentation.movieinfo.MovieInfoFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val moviesAdapter get() = binding.recyclerView.adapter as MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        val gridLayoutManager = GridLayoutManager(requireContext(), NUMBER_OF_RECYCLERVIEW_COLUMNS)

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter =
            MoviesAdapter(
                context = requireContext(),
                openMovieAction = {
                    navigateToAnotherFragment(it)
                },
                setFavorite = { movieId: Int, newValue: Boolean -> setFavorite(movieId, newValue) },
                isFavoriteCallback = { movieId, callback ->
                    lifecycleScope.launch {
                        val isFavorite = viewModel.isFavorite(movieId).first()
                        callback(isFavorite)
                    }
                },
            )

        binding.buttonReload.setOnClickListener {
            viewModel.fetchNowPlayingMovies()
            it.visibility = View.GONE
        }
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.listMovies.observe(viewLifecycleOwner) {
            moviesAdapter.setData(it)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast
                    .makeText(
                        requireContext(),
                        it,
                        Toast.LENGTH_SHORT,
                    ).show()
                viewModel.resetErrorMessage()
                binding.buttonReload.visibility = View.VISIBLE
            }
        }
    }

    private fun setFavorite(
        movieId: Int,
        newValue: Boolean,
    ) {
        viewModel.setFavorite(movieId, newValue)
    }

    private fun navigateToAnotherFragment(movie: Movie) {
        val destinationFragment =
            MovieInfoFragment().apply {
                arguments =
                    Bundle().apply {
                        putParcelable(MovieInfoFragment.ARGUMENT_MOVIE, movie)
                    }
            }

        requireActivity().supportFragmentManager.beginTransaction().apply {
            add(R.id.fragmentContainer, destinationFragment)
            addToBackStack("MainFragment")
            commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val NUMBER_OF_RECYCLERVIEW_COLUMNS = 2
    }
}
