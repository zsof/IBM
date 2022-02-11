package com.example.ibm.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.ibm.R
import com.example.ibm.databinding.FragmentDetailsBinding
import com.example.ibm.model.FilmData

class DetailsFragment : Fragment() {

    private var film: FilmData? = null
    private lateinit var binding: FragmentDetailsBinding

    companion object {
        const val FILM = "film"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            film = it.get(FILM) as FilmData?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        displayData(film)
    }

    //load data in DetailsFragment
    private fun displayData(filmData: FilmData?) = with(binding) {
        imageDetailsImageView.let {
            Glide.with(requireContext())
                .load(filmData?.avatarUrl)
                .placeholder(R.drawable.img)
                .into(it)
        }
        if (filmData != null) {
            titleDetailsTextView.text = filmData.title
            descDetailsTextView.text = filmData.description
            createdDetailsTextView.text = filmData.created
            durationDetailsTextView.text = filmData.durationInSec.toString()
            userNameDetailsTextView.text = filmData.userName
            userEmailDetailsTextView.text = filmData.email
        }
    }
}