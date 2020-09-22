package com.adesoftware.weatherapp.location

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.adesoftware.weatherapp.R
import com.adesoftware.weatherapp.repository.Location
import com.adesoftware.weatherapp.repository.LocationRepository

/**
 * A simple [Fragment] subclass.
 */
class LocationEntryFragment : Fragment() {

    private lateinit var locationRepository: LocationRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationRepository = LocationRepository(requireContext())
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_location_entry, container, false)

        //Update UI, get view references
        val postCodeEditText: EditText = view.findViewById(R.id.zip_code_edit_text)
        val enterButton: Button = view.findViewById(R.id.enter_button)

        enterButton.setOnClickListener {
            val zipCode: String = postCodeEditText.text.toString()
            if (zipCode.length != 5) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.zip_code_error),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //Toast.makeText(requireContext(), "ZipCode Entered", Toast.LENGTH_SHORT).show()
                locationRepository.saveLocation(Location.ZipCode(zipCode))
                findNavController().navigateUp()
            }
        }
        return view
    }

}
