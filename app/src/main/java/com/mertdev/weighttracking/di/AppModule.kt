package com.mertdev.weighttracking.di

import android.app.Application
import androidx.room.Room
import com.mertdev.weighttracking.data.db.WeightDao
import com.mertdev.weighttracking.data.db.WeightDatabase
import com.mertdev.weighttracking.data.db.WeightDatabase.Companion.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeightDb(app: Application): WeightDatabase {
        return Room.databaseBuilder(app, WeightDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(db: WeightDatabase): WeightDao {
        return db.getWeightDao()
    }

}