package com.mertdev.weighttracking.utils.extensions

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun requestNotificationPermission(
    request: ActivityResultLauncher<String>,
    permission: String
) = request.launch(permission)

fun Fragment.isNotificationPermissionGranted(permission: String) =
    ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
