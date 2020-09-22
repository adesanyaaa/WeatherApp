package com.adesoftware.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.adesoftware.weatherapp.api.DailyForecast
import java.text.SimpleDateFormat
import java.util.*

val DATE_FORMAT = SimpleDateFormat("MM-dd-yyyy")

class DailyForecastViewHolder(
    view: View,
    private val tempDisplaySettingManager: TempDisplaySettingManager)
    : RecyclerView.ViewHolder(view) {
    private  val tempText = view.findViewById<TextView>(R.id.temp_text)
    private val descriptionText = view.findViewById<TextView>(R.id.description_text)
    private val dateText = view.findViewById<TextView>(R.id.date_text)
    private val forecastIcon = view.findViewById<ImageView>(R.id.forecast_icon)

    fun bind(dailyForecast: DailyForecast) {
        //tempText.text = String.format("%.2f", dailyForecast.temp)
        tempText.text = formatTempForDisplay(dailyForecast.temp.max, tempDisplaySettingManager.getTempDisplaySetting())
        descriptionText.text = dailyForecast.weather[0].description
        dateText.text = DATE_FORMAT.format(Date(dailyForecast.date * 1000))

        val iconId = dailyForecast.weather[0].icon
        forecastIcon.load(" http://openweathermap.org/img/wn/${iconId}@2x.png")

    }
}

class DailyForecastListAdapter(
    private val tempDisplaySettingManager: TempDisplaySettingManager,
    private val clickHandler:(DailyForecast) -> Unit
) : ListAdapter<DailyForecast, DailyForecastViewHolder>(DIFF_CONFIG) {

    companion object {
        val DIFF_CONFIG = object: DiffUtil.ItemCallback<DailyForecast>() {
            override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: DailyForecast,
                newItem: DailyForecast
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_daily_forecast, parent, false)
        return DailyForecastViewHolder(itemView, tempDisplaySettingManager)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            clickHandler(getItem(position))
        }
    }
}