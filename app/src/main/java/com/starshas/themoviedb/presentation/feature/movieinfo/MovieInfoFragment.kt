package com.starshas.themoviedb.presentation.feature.movieinfo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.starshas.themoviedb.R
import com.starshas.themoviedb.common.AppConstants
import com.starshas.themoviedb.data.models.Movie
import com.starshas.themoviedb.databinding.FragmentMovieInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieInfoFragment : Fragment() {
    private var _binding: FragmentMovieInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movie: Movie = requireArguments().getParcelable(ARGUMENT_MOVIE, Movie::class.java)!!
        binding.movieInfoTextViewMovieTitle.text = movie.title
        binding.movieInfoTextViewMovieReleaseDate.text =
            getString(R.string.movie_info_release_date, movie.releaseDate)
        binding.movieInfoTextViewMovieRating.text =
            getString(R.string.movie_info_rating, movie.voteAverage.toString())
        binding.movieInfoTextViewMovieOverview.text =
            getString(R.string.movie_info_overview, movie.overview)
        Glide.with(requireContext())
            .load(AppConstants.BASE_URL_IMAGES + movie.backdropPath)
            .centerCrop()
            .into(binding.movieInfoImageViewMoviePoster)

        lifecycleScope.launch {
            viewModel.isFavorite(movie.id).collect {
                setStarIcon(it)
            }
        }

        lifecycleScope.launch {
            setStarIcon(isFavorite(movie.id))
        }

        binding.movieInfoImageViewStar.setOnClickListener {
            lifecycleScope.launch {
                setFavorite(movie.id)
            }
        }
    }

    private suspend fun setFavorite(movieId: Int) {
        val isFavorite = isFavorite(movieId)
        viewModel.setFavorite(movieId, !isFavorite)
    }

    private suspend fun isFavorite(movieId: Int) = viewModel.isFavorite(movieId).first()

    private fun setStarIcon(isFavorite: Boolean) {
        binding.movieInfoImageViewStar.setImageResource(
            if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_empty
        )
    }

    companion object {
        const val ARGUMENT_MOVIE = "movie_key"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
