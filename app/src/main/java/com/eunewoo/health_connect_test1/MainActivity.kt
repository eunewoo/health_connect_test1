package com.eunewoo.health_connect_test1

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
//    private late init var requestPermissions: ActivityResultLauncher<Set<Permission>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCheck = findViewById<Button>(R.id.btnCheck)
        btnCheck.setOnClickListener {
            checkAvailability()
        }

//        // Build a set of permissions for required data types.
//        val ALL_PERMISSIONS = setOf(
//            Permission.createReadPermission(HeartRateRecord::class),
//            Permission.createWritePermission(HeartRateRecord::class),
//            Permission.createReadPermission(StepsRecord::class),
//            Permission.createWritePermission(StepsRecord::class)
//        )
//
//        val permissionController = HealthConnectClient.getOrCreate(context).permissionController
//
//        // Create the permissions launcher
//        requestPermissions = registerForActivityResult(permissionController.createRequestPermissionActivityContract()) { grantedPermissions ->
//            if (grantedPermissions.containsAll(ALL_PERMISSIONS)) {
//                // Permissions successfully granted.
//            } else {
//                // User denied permission.
//            }
//        }
    }

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: NameNotFoundException) {
            false
        }
    }

    private fun checkAvailability() {
        when {
            isPackageInstalled("com.google.android.apps.healthdata", packageManager) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                // Health Connect is supported and installed.
                Toast.makeText(this, "Health Connect is already installed.", Toast.LENGTH_SHORT).show()
            }
            Build.VERSION.SDK_INT < Build.VERSION_CODES.P -> {
                // Health Connect is not supported.
                Toast.makeText(this, "Health Connect is not supported on this device.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // Health Connect is not installed.
                installHealthConnect()
            }
        }
    }


//    private suspend fun checkPermissionsAndRun(permissionController: PermissionController) {
//        val targetPermissions = setof(
//            Permission.createWritePermission(HeartRateRecord::class),
//            Permission.createWritePermission(StepsRecord::class)
//        )
//        val grantedPermissions = permissionController.getGrantedPermissions(targetPermissions)
//        if (grantedPermissions.containsAll(targetPermissions)) {
//            // Permissions already granted. Proceed to read/write data.
//        } else {
//            // Re-request permissions if revoked.
//            requestPermissions.launch(targetPermissions)
//        }
//    }

    /**
     * Redirect users to Play Store to install Health Connect.
     * Include intent to redirect users to 3P app post-Health Connect onboarding flow.
     */
    private fun installHealthConnect() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setPackage("com.android.vending")
        intent.data = Uri.parse("market://details")
            .buildUpon()
            .appendQueryParameter("id", "com.google.android.apps.healthdata")
            .appendQueryParameter("url", "healthconnect://onboarding")
            .build()
        startActivity(intent)
    }
}
