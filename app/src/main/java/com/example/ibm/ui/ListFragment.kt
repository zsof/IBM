package com.example.ibm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ibm.R
import com.example.ibm.adapter.PlaylistAdapter
import com.example.ibm.databinding.FragmentListBinding
import com.example.ibm.model.FilmList
import com.example.ibm.network.Interactor


class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentListBinding.bind(view)
        binding.itemList.layoutManager = LinearLayoutManager(context)
        recyclerView = binding.itemList

        loadPlaylist()
        setToolbar()
    }

    private fun loadPlaylist() {
        val interactor = Interactor()
        interactor.getSong(onSuccess = this::addFilms, onError = this::showError)
    }

    private fun loadPlaylistReverse() {
        val interactor = Interactor()
        interactor.getSong(onSuccess = this::addFilmsReverseNextPage, onError = this::showError)
    }

    private fun addFilms(filmList: FilmList) {
        val films = filmList.playlist
        if (films != null) {
            playlistAdapter = PlaylistAdapter(films.toMutableList())
        }
        recyclerView.adapter = playlistAdapter
    }

    private fun addFilmsReverseNextPage(filmList: FilmList) {
        val films = filmList.playlist
        if (films != null) {
            playlistAdapter = PlaylistAdapter((films.toMutableList()))
            playlistAdapter.films.reverse()
        }
        recyclerView.adapter = playlistAdapter
    }

    private fun showError(e: Throwable) {
        e.printStackTrace()
    }

    private fun setToolbar() {
        binding.toolbar.inflateMenu(R.menu.toolbar_menu)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search -> {
                    val searchView = it.actionView as SearchView
                    it.expandActionView()
                    searchView.queryHint = getString(R.string.search)
                    searchView.isIconified = false

                    searchView.setOnQueryTextListener(
                        object : SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                return true
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                playlistAdapter.filter.filter(newText)
                                return true
                            }
                        }
                    )
                }
                //paging, load the API
                R.id.previousPage -> {
                    loadPlaylist()
                }
                //paging, load the API but reverse
                R.id.nextPage -> {
                    loadPlaylistReverse()
                }
            }
            true
        }
    }
}