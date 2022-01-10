package com.example.twowaits

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.first

class CompanionObjects {
    companion object{
        lateinit var EMAIL: String
        lateinit var PREVIOUS_PAGE: String
        lateinit var accessToken: String
        lateinit var refreshToken: String

        var dataStore: DataStore<Preferences>? = null
        suspend fun saveLoginStatus(key: String, value: String){
            val dataStoreKey = preferencesKey<String>(key)
            dataStore?.edit {
                it[dataStoreKey] = value
            }
        }
        suspend fun readLoginStatus(key:String): String?{
            val dataStoreKey = preferencesKey<String>(key)
            val preferences = dataStore?.data?.first()
            return preferences?.get(dataStoreKey)
        }
        suspend fun saveAccessToken(key: String, value: String){
            val dataStoreKey = preferencesKey<String>(key)
            dataStore?.edit {
                it[dataStoreKey] = value
            }
        }
        suspend fun readAccessToken(key:String): String?{
            val dataStoreKey = preferencesKey<String>(key)
            val preferences = dataStore?.data?.first()
            return preferences?.get(dataStoreKey)
        }
        suspend fun saveRefreshToken(key: String, value: String){
            val dataStoreKey = preferencesKey<String>(key)
            dataStore?.edit {
                it[dataStoreKey] = value
            }
        }
        suspend fun readRefreshToken(key:String): String?{
            val dataStoreKey = preferencesKey<String>(key)
            val preferences = dataStore?.data?.first()
            return preferences?.get(dataStoreKey)
        }

        fun properTimeFormat(monthNumber: String, day:String, hours: String, minutes: String): String {
            val monthName = when(monthNumber){
                "01" -> "Jan"
                "02" -> "Feb"
                "03" -> "Mar"
                "04" -> "Apr"
                "05" -> "May"
                "06" -> "June"
                "07" -> "July"
                "08" -> "Aug"
                "09" -> "Sept"
                "10" -> "Oct"
                "11" -> "Nov"
                else -> "Dec"
            }
            val hour: Int
            val meridian: String
            when{
                (hours.toInt() + 5 in 13..24) -> {
                    hour = hours.toInt() + 5 - 12
                    meridian = "pm"
                }
                (hours.toInt() + 5 in 25..28)-> {
                    hour = hours.toInt() + 5 - 24
                    meridian = "am"
                }
                else -> {
                    hour = hours.toInt() + 5 - 24
                    meridian = "am"
                }
            }
            return "Answered at $hour:$minutes $meridian, $day $monthName"
        }


    }
}