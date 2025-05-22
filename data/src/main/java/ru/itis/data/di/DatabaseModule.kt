package ru.itis.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.itis.data.AppDatabase
import ru.itis.data.dao.ActionDao
import ru.itis.data.dao.BasalDao
import ru.itis.data.dao.CarbCoefDao
import ru.itis.data.dao.InsulinSensitivityDao
import ru.itis.data.dao.PumpDao
import ru.itis.data.dao.TargetGlucoseDao
import ru.itis.data.dao.UserDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideTargetGlucoseDao(db: AppDatabase): TargetGlucoseDao = db.targetGlucoseDao()

    @Provides
    fun provideCarbCoefDao(db: AppDatabase): CarbCoefDao = db.carbCoefDao()

    @Provides
    fun provideInsulinSensitivityDao(db: AppDatabase): InsulinSensitivityDao = db.insulinSensitivityDao()

    @Provides
    fun provideBasalDao(db: AppDatabase): BasalDao = db.basalDao()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun provideActionDao(db: AppDatabase): ActionDao = db.actionDao()

    @Provides
    fun providePumpDao(db: AppDatabase): PumpDao = db.pumpDao()

    @Provides
    @Singleton
    fun provideFakeKey(): ByteArray = "test_key_1234567890".toByteArray()
}