package ru.itis.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.itis.data.dao.ActionDao
import ru.itis.data.dao.BasalDao
import ru.itis.data.dao.CarbCoefDao
import ru.itis.data.dao.InsulinSensitivityDao
import ru.itis.data.dao.PumpDao
import ru.itis.data.dao.TargetGlucoseDao
import ru.itis.data.dao.UserDao
import ru.itis.data.entity.ActionEntity
import ru.itis.data.entity.BasalEntity
import ru.itis.data.entity.CarbCoefEntity
import ru.itis.data.entity.InsulinSensitivityEntity
import ru.itis.data.entity.PumpEntity
import ru.itis.data.entity.TargetGlucoseEntity
import ru.itis.data.entity.UserEntity

private const val DATABASE_NAME = "insulink.db"

@Database(
    entities = [
        TargetGlucoseEntity::class,
        CarbCoefEntity::class,
        InsulinSensitivityEntity::class,
        BasalEntity::class,
        UserEntity::class,
        ActionEntity::class,
        PumpEntity::class
    ],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun targetGlucoseDao(): TargetGlucoseDao
    abstract fun carbCoefDao(): CarbCoefDao
    abstract fun insulinSensitivityDao(): InsulinSensitivityDao
    abstract fun basalDao(): BasalDao
    abstract fun userDao(): UserDao
    abstract fun actionDao(): ActionDao
    abstract fun pumpDao(): PumpDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}