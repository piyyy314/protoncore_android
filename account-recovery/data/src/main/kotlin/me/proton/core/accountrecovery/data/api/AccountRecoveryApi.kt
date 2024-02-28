/*
 * Copyright (c) 2023 Proton AG
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

package me.proton.core.accountrecovery.data.api

import me.proton.core.accountrecovery.data.api.request.CancelRecoveryAttemptRequest
import me.proton.core.accountrecovery.data.api.request.ResetPasswordRequest
import me.proton.core.accountrecovery.data.api.response.CancelRecoveryAttemptResponse
import me.proton.core.network.data.protonApi.BaseRetrofitApi
import me.proton.core.network.data.protonApi.GenericResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AccountRecoveryApi : BaseRetrofitApi {
    @POST("account/v1/recovery/session")
    suspend fun startRecovery(): GenericResponse

    @POST("account/v1/recovery/session/abort")
    suspend fun cancelRecoveryAttempt(@Body request: CancelRecoveryAttemptRequest): CancelRecoveryAttemptResponse

    @POST("account/v4/recovery/session/consume")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): GenericResponse
}
