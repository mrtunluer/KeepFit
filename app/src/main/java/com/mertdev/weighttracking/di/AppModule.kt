package com.mertdev.weighttracking.di

import android.app.Application
import androidx.room.Room
import com.mertdev.weighttracking.data.db.UserInfoDao
import com.mertdev.weighttracking.data.db.UserInfoDatabase
import com.mertdev.weighttracking.data.db.UserInfoDatabase.Companion.DB_NAME
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
    fun provideUserInfoDb(app: Application): UserInfoDatabase {
        return Room.databaseBuilder(app, UserInfoDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(db: UserInfoDatabase): UserInfoDao {
        return db.getUserInfoDao()
    }

}