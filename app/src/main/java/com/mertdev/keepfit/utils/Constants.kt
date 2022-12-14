package com.mertdev.keepfit.utils

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

object Constants {
    const val WEIGHT_UNIT_KEY = "weight_unit"
    const val HEIGHT_UNIT_KEY = "height_unit"
    const val TARGET_WEIGHT_KEY = "target_weight"
    const val HEIGHT_KEY = "height"
    const val NUMBER_OF_CHART_DATA_KEY = "number_of_chart_data"
    const val IS_INFO_ENTERED_KEY = "is_info_entered"
    const val GENDER_KEY = "gender"
    const val DATA_STORE_NAME = "user_properties"
    const val MALE = "male"
    const val FEMALE = "female"
    const val KG = "kg"
    const val LB = "lb"
    const val CM = "cm"
    const val IN = "in"
    const val FT = "ft"
    const val EMPTY = ""
    const val DATE_PATTERN = "dd MMM yyyy"
    const val DATE_PATTERN_WITH_DAY = "dd MMM yyyy, EEEE"
    const val DATE_PATTERN_WITHOUT_YEAR = "dd MMM"
    const val LB_TO_KG = 0.4535f
    const val FT_TO_CM = 30.4878f
    const val TAKE_LAST_SEVEN = 7
    const val NUMBER_OF_INITIAL_DATA_IN_CHART = 15
    const val LAST_THIRTY = 30
    const val LAST_NINETY = 90
    const val LAST_HUNDRED_EIGHTY = 180
    const val HOUR_OF_DAY = 9
    const val MINUTE = 30
    const val NOTIFICATION_ID = "REMINDER_NOTIFICATION"
    const val NOTIFICATION_NAME = "Reminder Notification"
    const val NOTIFY_ID = 200

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    const val NOTIFICATION_PERMISSION = Manifest.permission.POST_NOTIFICATIONS
}