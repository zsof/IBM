package com.example.ibm.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ibm.R
import com.example.ibm.databinding.ItemListContentBinding
import com.example.ibm.model.FilmData
import com.example.ibm.ui.ListFragmentDirections

class PlaylistAdapter(
    var films: MutableList<FilmData>
) : ListAdapter<FilmData, PlaylistAdapter.ViewHolder>(ItemCallback), Filterable {

    private var filterList: MutableList<FilmData> = films

    init {
        submitList(films)
    }

    inner class ViewHolder(binding: ItemListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val textSongTitle: TextView = binding.titleListTextView
        val textSongCreated: TextView = binding.createdTimeListTextView
        val imageSong: ImageView = binding.imageListImageView
        var filmItem: FilmData? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListContentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val film = getItem(position)

        holder.filmItem = film
        holder.textSongTitle.text = film.title
        holder.textSongCreated.text = film.created

        //load image
        Glide.with(holder.imageSong)
            .load(film.avatarUrl)
            .placeholder(R.drawable.img)
            .into(holder.imageSong)
        holder.imageSong.visibility = View.VISIBLE

        //navigate to details
        holder.itemView.setOnClickListener {
            val action = ListFragmentDirections.actionListToDetails(film)
            holder.itemView.findNavController().navigate(action)
        }
    }

    //Search
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val searchString = charSequence.toString()

                filterList = if (searchString.isEmpty()) {
                    films

                } else {
                    val tempList: MutableList<FilmData> = mutableListOf()

                    for (film: FilmData in films) {
                        if (film.title.lowercase().contains(searchString.lowercase()))
                            tempList.add(film)
                    }
                    tempList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList

                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults?
            ) {
                val newList = filterResults?.values as MutableList<FilmData>
                submitList(newList)
            }
        }
    }

    companion object {
        object ItemCallback : DiffUtil.ItemCallback<FilmData>() {
            override fun areItemsTheSame(oldItem: FilmData, newItem: FilmData): Boolean {
                return oldItem.guid == newItem.guid
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: FilmData, newItem: FilmData): Boolean {
                return oldItem == newItem
            }
        }
    }
}