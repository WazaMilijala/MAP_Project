package com.example.valentinesgarage.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.valentinesgarage.databinding.FragmentFullscreenPhotoBinding

class FullscreenPhotoFragment : Fragment() {

    private var _binding: FragmentFullscreenPhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFullscreenPhotoBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUri = arguments?.getString("imageUri")

        Glide.with(requireContext())
            .load(imageUri)
            .into(binding.fullscreenImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
