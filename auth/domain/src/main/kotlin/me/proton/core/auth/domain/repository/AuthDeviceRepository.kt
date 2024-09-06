/*
 * Copyright (c) 2024 Proton AG
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

package me.proton.core.auth.domain.repository

import kotlinx.coroutines.flow.Flow
import me.proton.core.auth.domain.entity.AuthDevice
import me.proton.core.auth.domain.entity.InitDeviceStatus
import me.proton.core.domain.entity.SessionUserId
import me.proton.core.domain.entity.UserId
import me.proton.core.user.domain.entity.AddressId

interface AuthDeviceRepository {
    fun observeByUserId(userId: UserId): Flow<List<AuthDevice>>
    fun observeByAddressId(addressId: AddressId): Flow<List<AuthDevice>>

    suspend fun getByUserId(userId: UserId): List<AuthDevice>
    suspend fun getByAddressId(addressId: AddressId): List<AuthDevice>

    /**
     * Init a new device for SSO.
     */
    suspend fun initDevice(
        sessionUserId: SessionUserId,
        name: String,
        activationToken: String
    ): InitDeviceStatus
}
