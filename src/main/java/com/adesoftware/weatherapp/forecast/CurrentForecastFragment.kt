package com.adesoftware.weatherapp.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.adesoftware.weatherapp.R
import com.adesoftware.weatherapp.TempDisplaySettingManager
import com.adesoftware.weatherapp.api.CurrentWeather
import com.adesoftware.weatherapp.formatTempForDisplay
import com.adesoftware.weatherapp.repository.ForecastRepository
import com.adesoftware.weatherapp.repository.Location
import com.adesoftware.weatherapp.repository.LocationRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_current_forecast.*

/**
 * A simple [Fragment] subclass.
 */
class CurrentForecastFragment : Fragment() {

    private val forecastRepository = ForecastRepository()
    private lateinit var locationRepository: LocationRepository
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_current_forecast, container, false)
        val locationName: TextView = view.findViewById(R.id.location_name)
        val tempText: TextView = view.findViewById(R.id.temp_text)
        val emptyText = view.findViewById<TextView>(R.id.empty_text)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        val zipCode = arguments?.getString((WeeklyForecastFragment.KEY_ZIPCODE))?: ""

        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        //Create the observer which updates the UI in response to forecast updates
        val currentWeatherObserver = Observer<CurrentWeather> { weather ->
            emptyText.visibility = View.GONE
            progressBar.visibility = View.GONE
            locationName.visibility = View.VISIBLE
            tempText.visibility = View.VISIBLE

            location_name.text = weather.name
            tempText.text = formatTempForDisplay(weather.forecast.temp, tempDisplaySettingManager.getTempDisplaySetting())
        }
        forecastRepository.currentWeather.observe(viewLifecycleOwner, currentWeatherObserver)


        val locationEntryButton = view.findViewById<FloatingActionButton>(R.id.location_entry_button)
        locationEntryButton.setOnClickListener {
            showLocationEntry()
        }

        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> { savedLocation ->
            when(savedLocation) {
                is Location.ZipCode -> {
                    progressBar.visibility = View.VISIBLE
                    forecastRepository.loadCurrentForecast(savedLocation.zipCode)
                }
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)

        //forecastRepository.loadForecast(zipCode)

        return view
    }

    private fun showLocationEntry() {
        val action = CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }

    /*
    private fun showForecastDetails(forecast: DailyForecast) {
        val action = CurrentForecastFragmentDirections.actionCurrentForecastFragmentToForecastDetailsFragment(forecast.temp, forecast.description)
        findNavController().navigate(action)
    }
    */


    companion object {
        const val KEY_ZIPCODE = "key_zipCode"

        fun newInstance(zipCode: String): CurrentForecastFragment {
            val fragment = CurrentForecastFragment()

            val args = Bundle()
            args.putString(KEY_ZIPCODE, zipCode)
            fragment.arguments = args

            return fragment
        }
    }
}
