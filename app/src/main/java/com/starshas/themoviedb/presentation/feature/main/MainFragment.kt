package com.starshas.themoviedb.presentation.feature.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.starshas.themoviedb.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val moviesAdapter get() = binding.recyclerView.adapter as MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        val gridLayoutManager = GridLayoutManager(requireContext(), NUMBER_OF_RECYCLERVIEW_COLUMNS)

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = MoviesAdapter(
            context = requireContext(),
            openMovieAction = {}
        )
        binding.buttonReload.setOnClickListener {
            viewModel.fetchNowPlayingMovies()
            it.visibility = View.GONE
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.listMovie.observe(viewLifecycleOwner) {
            moviesAdapter.setData(it)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(
                    requireContext(),
                    it,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetErrorMessage()
                binding.buttonReload.visibility = View.VISIBLE
            }
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
