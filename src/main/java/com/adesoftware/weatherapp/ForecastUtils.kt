package com.adesoftware.weatherapp

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

fun formatTempForDisplay(temp: Float, tempDisplaySetting: TempDisplaySetting): String {
    //tempTextDetail.text = "${intent.getFloatExtra("key_temp", 0f)}\u00B0"
    return when (tempDisplaySetting) {
        TempDisplaySetting.Fahrenheit -> String.format("%.2f\u2109", temp)
        TempDisplaySetting.Celsius -> {
            val temp = (temp - 32f) * (5f/9f)
            String.format("%.2f\u2103", temp)
        }
    }
}

fun showTempDisplaySettingDialog(context: Context, tempDisplaySettingManager: TempDisplaySettingManager) {
    val dialogBuilder = AlertDialog.Builder(context)
        .setTitle("Choose Display Units")
        .setMessage("Choose which temperature unit to use for temperature display")
        .setPositiveButton("F°") { fahrenheit, temp ->
            //Toast.makeText(this, "Show using F\u00B0", Toast.LENGTH_SHORT).show()
            tempDisplaySettingManager.updateSetting(TempDisplaySetting.Fahrenheit)
        }
        .setNeutralButton("C°") { celsius, temp ->
            //Toast.makeText(this, "Show using C\u00B0", Toast.LENGTH_SHORT).show()
            tempDisplaySettingManager.updateSetting(TempDisplaySetting.Celsius)
        }
        .setOnDismissListener {
            Toast.makeText(context, "Setting will take effect on app restarting", Toast.LENGTH_SHORT).show()
        }
    dialogBuilder.show()
}
