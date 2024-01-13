package com.starshas.themoviedb.presentation.feature.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.starshas.themoviedb.R
import com.starshas.themoviedb.common.AppConstants
import com.starshas.themoviedb.data.models.Movie

class MoviesAdapter(
    private val context: Context,
    private val listMovies: MutableList<Movie> = mutableListOf(),
    private val openMovieAction: (Movie) -> Unit
) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {
    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewPoster: ImageView = view.findViewById(R.id.imageViewPoster)
        val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = listMovies[position]
        holder.textViewTitle.text = movie.title
        Glide.with(context)
            .load(AppConstants.BASE_URL_IMAGES + movie.posterPath)
            .centerCrop()
            .into(holder.imageViewPoster)
        holder.imageViewPoster.setOnClickListener {
            openMovieAction(listMovies[position])
        }
    }

    override fun getItemCount() = listMovies.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Movie>) {
        listMovies.clear()
        listMovies.addAll(list)
        notifyDataSetChanged()
    }
}
