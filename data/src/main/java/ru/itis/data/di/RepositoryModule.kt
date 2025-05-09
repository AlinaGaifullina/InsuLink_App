package ru.itis.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.data.repository_impl.ActionRepositoryImpl
import ru.itis.data.repository_impl.BasalRepositoryImpl
import ru.itis.data.repository_impl.CarbCoefRepositoryImpl
import ru.itis.data.repository_impl.InsulinSensitivityRepositoryImpl
import ru.itis.data.repository_impl.PumpRepositoryImpl
import ru.itis.data.repository_impl.TargetGlucoseRepositoryImpl
import ru.itis.data.repository_impl.UserRepositoryImpl
import ru.itis.domain.repository.ActionRepository
import ru.itis.domain.repository.BasalRepository
import ru.itis.domain.repository.CarbCoefRepository
import ru.itis.domain.repository.InsulinSensitivityRepository
import ru.itis.domain.repository.PumpRepository
import ru.itis.domain.repository.TargetGlucoseRepository
import ru.itis.domain.repository.UserRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindTargetGlucoseRepository(impl: TargetGlucoseRepositoryImpl): TargetGlucoseRepository

    @Binds
    fun bindCarbCoefRepository(impl: CarbCoefRepositoryImpl): CarbCoefRepository

    @Binds
    fun bindInsulinSensitivityRepository(impl: InsulinSensitivityRepositoryImpl): InsulinSensitivityRepository

    @Binds
    fun bindBasalRepository(impl: BasalRepositoryImpl): BasalRepository

    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    fun bindActionRepository(impl: ActionRepositoryImpl): ActionRepository

    @Binds
    fun bindPumpRepository(impl: PumpRepositoryImpl): PumpRepository
}