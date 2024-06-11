package com.example.grocerystoretest.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.grocerystoretest.model.User

class ApplicationPreference {

    fun saveToken(refreshToken: String, accessToken: String) {
        editor = pref?.edit()
        editor?.putString("refreshToken", refreshToken)
        editor?.putString("accessToken", accessToken)
        editor?.apply()
    }

    fun getAccessToken(): String {
        return pref?.getString("accessToken", "").toString()
    }

    fun getRefreshToken(): String {
        return pref?.getString("refreshToken", "").toString()
    }

    fun saveUserInfo(user: User) {
        editor = pref?.edit()
        editor?.putString("id", user.id)
        editor?.putString("phoneNumber", user.phoneNumber)
        editor?.putString("email", user.email)
        editor?.putString("firstName", user.firstName)
        editor?.putString("lastName", user.lastName)
        editor?.apply()
    }

    fun getUserInfo(): User {
        if (pref?.getString("id", "").isNullOrBlank()) {
            return User()
        }
        return User(
            id = pref?.getString("id", ""),
            phoneNumber = pref?.getString("phoneNumber", ""),
            email = pref?.getString("email", ""),
            firstName = pref?.getString("firstName", ""),
            lastName = pref?.getString("lastName", "")
        )
    }

    fun logout() {
        editor = pref?.edit()
        editor?.remove("id")
        editor?.remove("phoneNumber")
        editor?.remove("email")
        editor?.remove("firstName")
        editor?.remove("lastName")
        editor?.remove("accessToken")
        editor?.remove("refreshToken")
        editor?.apply()
    }

    companion object {
        private var applicationPreference: ApplicationPreference? = null
        private var pref: SharedPreferences? = null
        private var editor: SharedPreferences.Editor? = null
        fun getInstance(context: Context): ApplicationPreference? {
            if (applicationPreference == null) applicationPreference = ApplicationPreference()
            if (pref == null) {
                pref = PreferenceManager.getDefaultSharedPreferences(context)
            }
            return applicationPreference
        }
    }
}
