/*
 * Copyright (c) 2020 Proton Technologies AG
 * This file is part of Proton Technologies AG and ProtonCore.
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

package me.proton.core.payment.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.proton.core.payment.domain.entity.PaymentToken
import me.proton.core.payment.domain.entity.PaymentTokenStatus

@Serializable
internal data class CreatePaymentTokenResponse(
    @SerialName("ApprovalURL")
    var approvalUrl: String? = null,
    @SerialName("Token")
    var token: String,
    @SerialName("Status")
    var status: Int,
    @SerialName("ReturnHost")
    var returnHost: String? = null,
) {
    fun toCreatePaymentTokenResult(): PaymentToken.CreatePaymentTokenResult =
        PaymentToken.CreatePaymentTokenResult(
            status = PaymentTokenStatus.getByValue(status),
            approvalUrl = approvalUrl,
            token = token,
            returnHost = returnHost)
}
