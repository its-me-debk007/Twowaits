package com.example.twowaits.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.datastore by preferencesDataStore("TWO_WAITS")

class Datastore(val context: Context) {
    companion object {
        val LOGIN_KEY = stringPreferencesKey("login_key")
    }

    suspend fun saveLoginData(value: String) {
        context.datastore.edit {
            it[LOGIN_KEY] = value
        }
    }

    suspend fun readLoginState() = context.datastore.data.first()[LOGIN_KEY]

    suspend fun saveTokens(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.datastore.edit {
            it[dataStoreKey] = value
        }
    }

    suspend fun readTokens(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        return context.datastore.data.first()[dataStoreKey]
    }

    suspend fun saveUserDetails(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.datastore.edit {
            it[dataStoreKey] = value
        }
    }

    suspend fun readUserDetails(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        return context.datastore.data.first()[dataStoreKey]
    }

}