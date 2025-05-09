package ru.itis.data.repository_impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.itis.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {
    companion object {
        val USER_ID_KEY = stringPreferencesKey("user_id")
        val PUMP_ID_KEY = stringPreferencesKey("pump_id")
    }

    // User ID
    override val userId: Flow<String?> = dataStore.data
        .map { it[USER_ID_KEY] }

    override suspend fun setUserId(userId: String) {
        dataStore.edit { it[USER_ID_KEY] = userId }
    }

    // Pump ID
    override val pumpId: Flow<String?> = dataStore.data
        .map { it[PUMP_ID_KEY] }

    override suspend fun setPumpId(pumpId: String) {
        dataStore.edit { it[PUMP_ID_KEY] = pumpId }
    }

    // Очистка
    override suspend fun clearAll() {
        dataStore.edit {
            it.remove(USER_ID_KEY)
            it.remove(PUMP_ID_KEY)
        }
    }
}