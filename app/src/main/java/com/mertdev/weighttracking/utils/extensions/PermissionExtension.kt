package com.mertdev.weighttracking.utils.extensions

import androidx.activity.result.ActivityResultLauncher

fun requestNotificationPermission(
    request: ActivityResultLauncher<String>,
    permission: String
) = request.launch(permission)