package com.example.grocerystoretest.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.grocerystoretest.R
import com.example.grocerystoretest.base.BaseActivity
import com.example.grocerystoretest.databinding.ActivityMainBinding
import com.example.grocerystoretest.utils.ApplicationPreference
import com.example.grocerystoretest.viewmodel.CustomerDeviceViewModel
import com.example.grocerystoretest.viewmodel.LoginViewModel

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
    }

    override fun initListener() {

    }

    override fun observeData() {

    }

    private fun startLoginOrHomeActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            val accessToken = ApplicationPreference.getInstance(this)?.getAccessToken()
            if (accessToken.isNullOrEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                startActivity(Intent(this, HomeActivity::class.java))
            }
            finishAffinity()
        }, 1000)

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

}