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
        var LOG_IN_STATUS: String? = null

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
    }
}