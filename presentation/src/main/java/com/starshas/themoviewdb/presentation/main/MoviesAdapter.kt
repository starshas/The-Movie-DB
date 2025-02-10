package com.starshas.themoviewdb.presentation.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.starshas.themoviedb.domain.models.DomainMoviesInfo
import com.starshas.themoviewdb.presentation.R
import com.starshas.themoviewdb.presentation.main.MoviesAdapter.MovieViewHolder

class MoviesAdapter(
    private val context: Context,
    private val listMovies: MutableList<DomainMoviesInfo.Movie> = mutableListOf(),
    private val openMovieAction: (DomainMoviesInfo.Movie) -> Unit,
    private val setFavorite: (Int, Boolean) -> Unit,
    private val isFavoriteCallback: (Int, (Boolean) -> Unit) -> Unit,
) : RecyclerView.Adapter<MovieViewHolder>() {
    class MovieViewHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {
        val imageViewPoster: ImageView = view.findViewById(R.id.movieItemImageViewPoster)
        val textViewTitle: TextView = view.findViewById(R.id.movieItemTextViewTitle)
        val imageViewStar: ImageView = view.findViewById(R.id.movieItemImageViewStar)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int,
    ) {
        val movie = listMovies[position]
        holder.textViewTitle.text = movie.title
        Glide
            .with(context)
            .load(movie.posterUrl)
            .centerCrop()
            .into(holder.imageViewPoster)
        holder.imageViewPoster.setOnClickListener {
            openMovieAction(listMovies[position])
        }

        isFavoriteCallback(movie.id) { isFavorite ->
            setStarIcon(holder, isFavorite)
        }

        holder.imageViewStar.setOnClickListener {
            isFavoriteCallback(movie.id) { isFavorite ->
                val newValue = !isFavorite
                setFavorite(movie.id, newValue)
                setStarIcon(holder, newValue)
            }
        }
    }

    private fun setStarIcon(
        holder: MovieViewHolder,
        isFavorite: Boolean,
    ) {
        holder.imageViewStar.setImageResource(if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_empty)
    }

    override fun getItemCount() = listMovies.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<DomainMoviesInfo.Movie>) {
        listMovies.clear()
        listMovies.addAll(list)
        notifyDataSetChanged()
    }
}
