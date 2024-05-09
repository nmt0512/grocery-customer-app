package com.example.grocerystoretest.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.grocerystoretest.R
import com.example.grocerystoretest.base.BaseActivity
import com.example.grocerystoretest.databinding.ActivityMainBinding
import com.example.grocerystoretest.model.request.auth.LoginRequest
import com.example.grocerystoretest.model.request.customer_device.CreateCustomerDeviceRequest
import com.example.grocerystoretest.utils.ApplicationPreference
import com.example.grocerystoretest.viewmodel.CustomerDeviceViewModel
import com.example.grocerystoretest.viewmodel.LoginViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import io.kommunicate.Kommunicate

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var customerDeviceViewModel: CustomerDeviceViewModel

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
        startLoginOrHomeActivity()
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        loginViewModel = LoginViewModel(this)
        customerDeviceViewModel = CustomerDeviceViewModel(this)

        askNotificationPermission()
        initFirebase()
    }

    override fun initListener() {

    }

    override fun observeData() {
        loginViewModel.isSuccessfullyLoggedIn.observe(this) {
            if (it) {
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finishAffinity()
        }
    }

    private fun startLoginOrHomeActivity() {
        val savedLoginRequest = ApplicationPreference.getInstance(this)?.getLoginRequest()
        if (savedLoginRequest == LoginRequest()) {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }, 1000)
        } else {
            loginViewModel.login(savedLoginRequest?.phoneNumber!!, savedLoginRequest.password!!)
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
                startLoginOrHomeActivity()
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("initFirebase", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                task.result?.let {
                    Log.d("initFirebase", it)
                    customerDeviceViewModel.createCustomerDevice(
                        CreateCustomerDeviceRequest(getDeviceId(this), it)
                    )
                }
            },
        )
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID,
        )
    }

}