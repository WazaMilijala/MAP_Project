package com.example.valentinesgarage.presentation.fragments

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.valentinesgarage.R
import com.example.valentinesgarage.databinding.FragmentCheckInBinding
import com.example.valentinesgarage.presentation.viewmodels.TruckCheckInViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File

@AndroidEntryPoint
class CheckInFragment : Fragment() {

    // Holds the URI for the current image being captured
    private var imageUri: Uri? = null
    // Stores all captured image URIs before truck submission
    private val capturedPhotos = mutableListOf<String>()

    private var _binding: FragmentCheckInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TruckCheckInViewModel by viewModels()

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->

        if (granted) {

            imageUri = createImageFile()

            cameraLauncher.launch(imageUri)

        } else {

            Toast.makeText(
                context,
                "Camera permission denied",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    //Android camera launcher using Activity Result APIs
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->

        // If the photo was successfully taken
        if (success && imageUri != null) {
            // Save the image URI locally until the truck is submitted
            capturedPhotos.add(imageUri.toString())




            Toast.makeText(
                context,
                "Photo captured successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        // Fixed: Use ArrayAdapter for AutoCompleteTextView
        val conditionAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            arrayOf("Excellent", "Good", "Fair", "Poor")
        )
        binding.conditionInput.setAdapter(conditionAdapter)

        val damageAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            arrayOf(
                "No Damage",
                "Minor Scratches",
                "Dents Present",
                "Major Damage",
                "Multiple Issues"
            )
        )
        binding.damageAssessmentInput.setAdapter(damageAdapter)
    }

    private fun setupListeners() {

        binding.submitButton.setOnClickListener {
            validateAndSubmit()
        }

        // Launch camera when user presses capture button
        binding.capturePhotosButton.setOnClickListener {

            cameraPermissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    }

    private fun validateAndSubmit() {

        val licensePlate = binding.licensePlateInput.text.toString()
        val model = binding.modelInput.text.toString()
        val brand = binding.brandInput.text.toString()
        val year = binding.yearInput.text.toString().toIntOrNull()
        val ownerName = binding.ownerNameInput.text.toString()
        val ownerPhone = binding.ownerPhoneInput.text.toString()
        val kilometers = binding.kilometerInput.text.toString().toIntOrNull()
        val condition = binding.conditionInput.text.toString()
        val damages = binding.damageAssessmentInput.text.toString()
        val notes = binding.notesInput.text.toString()

        if (
            licensePlate.isEmpty() ||
            model.isEmpty() ||
            brand.isEmpty() ||
            year == null ||
            ownerName.isEmpty() ||
            ownerPhone.isEmpty() ||
            kilometers == null ||
            condition.isEmpty()
        ) {

            Toast.makeText(
                context,
                "Please fill all required fields",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        viewModel.checkInTruck(
            licensePlate = licensePlate,
            model = model,
            brand = brand,
            year = year,
            ownerName = ownerName,
            ownerPhone = ownerPhone,
            kilometers = kilometers,
            condition = condition,
            damages = damages,
            notes = notes,

            onSuccess = { truckId ->

                // Save every captured image to database
                capturedPhotos.forEach { uri ->

                    viewModel.saveTruckPhoto(
                        truckId = truckId,
                        imageUri = uri,
                        photoType = "inspection"
                    )
                }

                showSuccessDialog(truckId)
            },

            onError = { error ->

                Toast.makeText(
                    context,
                    error,
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }

    // Creates a unique image file and returns secure URI
    private fun createImageFile(): Uri {

        val imageFile = File(
            requireContext().getExternalFilesDir("Pictures"),
            "truck_${System.currentTimeMillis()}.jpg"
        )

        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            imageFile
        )
    }

    private fun clearForm() {

        binding.licensePlateInput.text?.clear()
        binding.modelInput.text?.clear()
        binding.brandInput.text?.clear()
        binding.yearInput.text?.clear()
        binding.ownerNameInput.text?.clear()
        binding.ownerPhoneInput.text?.clear()
        binding.kilometerInput.text?.clear()
        binding.notesInput.text?.clear()

        binding.conditionInput.setText("")
        binding.damageAssessmentInput.setText("")
    }

    private fun showSuccessDialog(truckId: Long) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Check-in Successful")
            .setMessage(
                "Truck has been checked in successfully.\nTruck ID: $truckId"
            )
            .setPositiveButton("OK") { _, _ ->

                findNavController().navigate(R.id.navigation_service)
            }
            .setNegativeButton("Check In Another") { _, _ ->

                clearForm()
            }
            .show()
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}