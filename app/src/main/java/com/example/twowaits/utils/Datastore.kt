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
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token_key")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token_key")
    }

    suspend fun saveLoginData(value: String) {
        context.datastore.edit {
            it[LOGIN_KEY] = value
        }
    }

    suspend fun readLoginState() = context.datastore.data.first()[LOGIN_KEY]

    suspend fun saveAccessToken(value: String) {
        context.datastore.edit {
            it[ACCESS_TOKEN_KEY] = value
        }
    }

    suspend fun saveRefreshToken(value: String) {
        context.datastore.edit {
            it[REFRESH_TOKEN_KEY] = value
        }
    }

    suspend fun getAccessToken() = context.datastore.data.first()[ACCESS_TOKEN_KEY]
    suspend fun getRefreshToken() = context.datastore.data.first()[REFRESH_TOKEN_KEY]

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