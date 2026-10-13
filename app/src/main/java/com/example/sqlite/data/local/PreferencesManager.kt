package com.example.sqlite.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("talent_track_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOTAL_SALARIES = "key_total_salaries"
    }

    fun saveTotalSalaries(total: Double) {
        prefs.edit().putFloat(KEY_TOTAL_SALARIES, total.toFloat()).apply()
    }

    fun getTotalSalaries(): Double {
        return prefs.getFloat(KEY_TOTAL_SALARIES, 0f).toDouble()
    }
}
