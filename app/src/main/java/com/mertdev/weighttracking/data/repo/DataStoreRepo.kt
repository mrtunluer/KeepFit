package com.mertdev.weighttracking.data.repo

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.mertdev.weighttracking.ui.home.model.UiModel
import com.mertdev.weighttracking.utils.Constants.DATA_STORE_NAME
import com.mertdev.weighttracking.utils.Constants.GENDER_KEY
import com.mertdev.weighttracking.utils.Constants.HEIGHT_KEY
import com.mertdev.weighttracking.utils.Constants.HEIGHT_UNIT_KEY
import com.mertdev.weighttracking.utils.Constants.IS_INFO_ENTERED_KEY
import com.mertdev.weighttracking.utils.Constants.TARGET_WEIGHT_KEY
import com.mertdev.weighttracking.utils.Constants.WEIGHT_UNIT_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(DATA_STORE_NAME)

class DataStoreRepo @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val weightUnit = stringPreferencesKey(WEIGHT_UNIT_KEY)
        val heightUnit = stringPreferencesKey(HEIGHT_UNIT_KEY)
        val targetWeight = floatPreferencesKey(TARGET_WEIGHT_KEY)
        val isInfoEntered = booleanPreferencesKey(IS_INFO_ENTERED_KEY)
        val gender = stringPreferencesKey(GENDER_KEY)
        val height = floatPreferencesKey(HEIGHT_KEY)
    }

    suspend fun saveWeightUnit(weightUnit: String) {
        context.dataStore.edit { preference ->
            preference[PreferenceKeys.weightUnit] = weightUnit
        }
    }

    suspend fun saveHeightUnit(heightUnit: String) {
        context.dataStore.edit { preference ->
            preference[PreferenceKeys.heightUnit] = heightUnit
        }
    }

    suspend fun saveTargetWeight(targetWeight: Float) {
        context.dataStore.edit { preference ->
            preference[PreferenceKeys.targetWeight] = targetWeight
        }
    }

    suspend fun saveOnBoardingState(isInfoEntered: Boolean) {
        context.dataStore.edit { preference ->
            preference[PreferenceKeys.isInfoEntered] = isInfoEntered
        }
    }

    suspend fun saveGender(gender: String) {
        context.dataStore.edit { preference ->
            preference[PreferenceKeys.gender] = gender
        }
    }

    suspend fun saveHeight(height: Float) {
        context.dataStore.edit { preference ->
            preference[PreferenceKeys.height] = height
        }
    }

    val readOnBoardingState: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preference ->
            val isInfoEntered = preference[PreferenceKeys.isInfoEntered] ?: false
            isInfoEntered
        }

    val readAllPreferences: Flow<UiModel> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preference ->
            val weightUnit = preference[PreferenceKeys.weightUnit]
            val heightUnit = preference[PreferenceKeys.heightUnit]
            val targetWeight = preference[PreferenceKeys.targetWeight]
            val height = preference[PreferenceKeys.height]
            val gender = preference[PreferenceKeys.gender]
            val uiModel = UiModel(
                weightUnit = weightUnit,
                heightUnit = heightUnit,
                targetWeight = targetWeight,
                height = height,
                gender = gender
            )
            uiModel
        }.filterNotNull()

}