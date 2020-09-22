package com.adesoftware.weatherapp.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.load
import com.adesoftware.weatherapp.TempDisplaySettingManager
import com.adesoftware.weatherapp.databinding.FragmentForecastDetailsBinding
import com.adesoftware.weatherapp.formatTempForDisplay
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */

class ForecastDetailsFragment : Fragment() {

    private val args: ForecastDetailsFragmentArgs by navArgs()

    private lateinit var viewModelFactory: ForecastDetailsViewModelFactory
    private val viewModel: ForecastDetailsViewModel by viewModels(
        factoryProducer = { viewModelFactory}
    )

    private var _binding: FragmentForecastDetailsBinding?=null
    //This property only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val layout = inflater.inflate(R.layout.fragment_forecast_details, container, false)
        _binding = FragmentForecastDetailsBinding.inflate(inflater, container, false)
        viewModelFactory = ForecastDetailsViewModelFactory(args)
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewStateObserver = Observer<ForecastDetailsViewState> {viewState ->
            //update the UI
            binding.tempTextDetail.text = formatTempForDisplay(viewState.temp,
                tempDisplaySettingManager.getTempDisplaySetting())
            binding.descriptionTextDetail.text = viewState.description
            binding.dateTextDetail.text = viewState.date
            binding.forecastIcon.load(viewState.iconUrl)
        }
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
