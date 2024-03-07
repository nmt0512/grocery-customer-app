package com.example.grocerystoretest.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.grocerystoretest.model.User
import com.example.grocerystoretest.model.request.auth.LoginRequest

class ApplicationPreference {

    fun saveAccessToken(accessToken: String) {
        editor = pref?.edit()
        editor?.putString("accessToken", accessToken)
        editor?.apply()
    }

    fun getAccessToken(): String {
        return pref?.getString("accessToken", "").toString()
    }

    fun saveLoginRequest(loginRequest: LoginRequest) {
        editor?.putString("phoneNumber", loginRequest.phoneNumber)
        editor?.putString("password", loginRequest.password)
        editor?.apply()
    }

    fun getLoginRequest(): LoginRequest {
        val phoneNumber = pref?.getString("phoneNumber", "")
        val password = pref?.getString("password", "")
        if (phoneNumber.isNullOrBlank() || password.isNullOrBlank()) {
            return LoginRequest()
        }
        return LoginRequest(phoneNumber, password)
    }

    fun saveUserInfo(user: User) {
        editor = pref?.edit()
        editor?.putString("id", user.id)
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
        editor?.remove("password")
        editor?.remove("email")
        editor?.remove("firstName")
        editor?.remove("lastName")
        editor?.remove("accessToken")
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
