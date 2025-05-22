package ru.itis.data.repository_impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val PUMP_ID_KEY = stringPreferencesKey("pump_id")
        private val INSULIN_INJECTION_KEY = booleanPreferencesKey("is_insulin_injection")
    }

    // User ID
    override val userId: Flow<String?> = dataStore.data
        .map { it[USER_ID_KEY] }

    override suspend fun setUserId(userId: String) {
        dataStore.edit { it[USER_ID_KEY] = userId }
    }
    override suspend fun deleteUserId() {
        dataStore.edit { it.remove(USER_ID_KEY) }
    }

    // Pump ID
    override val pumpId: Flow<String?> = dataStore.data
        .map { it[PUMP_ID_KEY] }

    override suspend fun setPumpId(pumpId: String) {
        dataStore.edit { it[PUMP_ID_KEY] = pumpId }
    }

    // Insulin Injection Flag
    override val isInsulinInjection: Flow<Boolean> = dataStore.data
        .map { it[INSULIN_INJECTION_KEY] ?: false }

    override suspend fun setIsInsulinInjection(isInjection: Boolean) {
        dataStore.edit { it[INSULIN_INJECTION_KEY] = isInjection }
    }

    // Очистка
    override suspend fun clearAll() {
        dataStore.edit {
            it.remove(USER_ID_KEY)
            it.remove(PUMP_ID_KEY)
            it.remove(INSULIN_INJECTION_KEY)
        }
    }
}