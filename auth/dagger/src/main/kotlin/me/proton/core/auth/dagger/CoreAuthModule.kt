/*
 * Copyright (c) 2022 Proton Technologies AG
 * This file is part of Proton AG and ProtonCore.
 *
 * ProtonCore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonCore is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonCore.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.proton.core.auth.dagger

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.proton.core.auth.data.MissingScopeListenerImpl
import me.proton.core.auth.data.feature.IsFido2EnabledImpl
import me.proton.core.auth.data.repository.AuthDeviceLocalDataSourceImpl
import me.proton.core.auth.data.repository.AuthDeviceRemoteDataSourceImpl
import me.proton.core.auth.data.repository.AuthDeviceRepositoryImpl
import me.proton.core.auth.data.repository.AuthRepositoryImpl
import me.proton.core.auth.data.repository.DeviceSecretRepositoryImpl
import me.proton.core.auth.data.feature.IsCommonPasswordCheckEnabledImpl
import me.proton.core.auth.data.repository.MemberDeviceLocalDataSourceImpl
import me.proton.core.auth.data.repository.MemberDeviceRemoteDataSourceImpl
import me.proton.core.auth.data.repository.MemberDeviceRepositoryImpl
import me.proton.core.auth.data.feature.IsCredentialLessEnabledImpl
import me.proton.core.auth.data.feature.IsLoginTwoStepEnabledImpl
import me.proton.core.auth.data.feature.IsSsoCustomTabEnabledImpl
import me.proton.core.auth.data.feature.IsSsoEnabledImpl
import me.proton.core.auth.domain.feature.IsCommonPasswordCheckEnabled
import me.proton.core.auth.domain.feature.IsFido2Enabled
import me.proton.core.auth.domain.repository.AuthDeviceLocalDataSource
import me.proton.core.auth.domain.repository.AuthDeviceRemoteDataSource
import me.proton.core.auth.domain.repository.AuthDeviceRepository
import me.proton.core.auth.domain.repository.AuthRepository
import me.proton.core.auth.domain.repository.DeviceSecretRepository
import me.proton.core.auth.domain.repository.MemberDeviceLocalDataSource
import me.proton.core.auth.domain.repository.MemberDeviceRemoteDataSource
import me.proton.core.auth.domain.repository.MemberDeviceRepository
import me.proton.core.auth.domain.feature.IsCredentialLessEnabled
import me.proton.core.auth.domain.feature.IsLoginTwoStepEnabled
import me.proton.core.auth.domain.feature.IsSsoCustomTabEnabled
import me.proton.core.auth.domain.feature.IsSsoEnabled
import me.proton.core.network.domain.scopes.MissingScopeListener
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public interface CoreAuthModule {

    @Binds
    @Singleton
    public fun provideMissingScopeListener(impl: MissingScopeListenerImpl): MissingScopeListener

    @Binds
    public fun bindDeviceSecretRepository(impl: DeviceSecretRepositoryImpl): DeviceSecretRepository

    @Binds
    public fun bindAuthDeviceLocalDataSource(impl: AuthDeviceLocalDataSourceImpl): AuthDeviceLocalDataSource

    @Binds
    public fun bindAuthDeviceRemoteDataSource(impl: AuthDeviceRemoteDataSourceImpl): AuthDeviceRemoteDataSource

    @Binds
    public fun bindAuthDeviceRepository(impl: AuthDeviceRepositoryImpl): AuthDeviceRepository

    @Binds
    public fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    public fun bindMemberDeviceLocalDataSource(impl: MemberDeviceLocalDataSourceImpl): MemberDeviceLocalDataSource

    @Binds
    public fun bindMemberDeviceRemoteDataSource(impl: MemberDeviceRemoteDataSourceImpl): MemberDeviceRemoteDataSource

    @Binds
    public fun bindMemberDeviceRepository(impl: MemberDeviceRepositoryImpl): MemberDeviceRepository
}

@Module
@InstallIn(SingletonComponent::class)
public interface CoreAuthFeaturesModule {
    @Binds
    @Singleton
    public fun provideIsSsoEnabled(impl: IsSsoEnabledImpl): IsSsoEnabled

    @Binds
    @Singleton
    public fun provideIsSsoCustomTabEnabled(impl: IsSsoCustomTabEnabledImpl): IsSsoCustomTabEnabled

    @Binds
    @Singleton
    public fun provideIsLoginTwoStepEnabled(impl: IsLoginTwoStepEnabledImpl): IsLoginTwoStepEnabled

    @Binds
    @Singleton
    public fun bindIsCredentialLessEnabled(impl: IsCredentialLessEnabledImpl): IsCredentialLessEnabled

    @Binds
    @Singleton
    public fun bindIsCommonPasswordCheckEnabled(impl: IsCommonPasswordCheckEnabledImpl): IsCommonPasswordCheckEnabled

    @Binds
    public fun bindIsFido2Enabled(impl: IsFido2EnabledImpl): IsFido2Enabled
}
