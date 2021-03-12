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

package me.proton.core.payment.data.repository

import me.proton.core.network.data.ApiProvider
import me.proton.core.network.domain.session.SessionId
import me.proton.core.payment.data.api.PaymentsApi
import me.proton.core.payment.data.entity.CardDetailsBody
import me.proton.core.payment.data.entity.CheckSubscription
import me.proton.core.payment.data.entity.CreatePaymentToken
import me.proton.core.payment.data.entity.CreateSubscription
import me.proton.core.payment.data.entity.PaymentTypeEntity
import me.proton.core.payment.data.entity.TokenDetails
import me.proton.core.payment.data.entity.TokenTypePaymentBody
import me.proton.core.payment.data.exception.InsufficientPaymentDetails
import me.proton.core.payment.domain.entity.Card
import me.proton.core.payment.domain.entity.Currency
import me.proton.core.payment.domain.entity.PaymentBody
import me.proton.core.payment.domain.entity.PaymentMethod
import me.proton.core.payment.domain.entity.PaymentMethodType
import me.proton.core.payment.domain.entity.PaymentToken
import me.proton.core.payment.domain.entity.PaymentType
import me.proton.core.payment.domain.entity.Subscription
import me.proton.core.payment.domain.entity.SubscriptionCycle
import me.proton.core.payment.domain.entity.SubscriptionStatus
import me.proton.core.payment.domain.repository.PaymentsRepository
import me.proton.core.util.kotlin.exhaustive

class PaymentsRepositoryImpl(
    private val provider: ApiProvider
) : PaymentsRepository {

    override suspend fun createPaymentToken(
        sessionId: SessionId?,
        amount: Long,
        currency: Currency,
        paymentType: PaymentType?,
        paymentMethodId: String?
    ): PaymentToken.CreatePaymentTokenResult =
        provider.get<PaymentsApi>(sessionId = sessionId).invoke {
            val payment = when (paymentType) {
                is PaymentType.PayPal -> PaymentTypeEntity.PayPal
                is PaymentType.CreditCard -> {
                    val paymentCard = paymentType.card
                    if (paymentCard !is Card.CardWithPaymentDetails) {
                        throw InsufficientPaymentDetails
                    }
                    PaymentTypeEntity.Card(
                        CardDetailsBody(
                            number = paymentCard.number,
                            cvc = paymentCard.cvc,
                            expirationMonth = paymentCard.expirationMonth,
                            expirationYear = paymentCard.expirationYear,
                            name = paymentCard.name,
                            country = paymentCard.country,
                            zip = paymentCard.zip
                        )
                    )
                }
                else -> null
            }

            val request = CreatePaymentToken(amount, currency.name, payment, paymentMethodId)
            createPaymentToken(request).toCreatePaymentTokenResult()
        }.valueOrThrow

    override suspend fun getPaymentTokenStatus(
        sessionId: SessionId?,
        paymentToken: String
    ): PaymentToken.PaymentTokenStatusResult =
        provider.get<PaymentsApi>(sessionId = sessionId).invoke {
            getPaymentTokenStatus(paymentToken).toPaymentTokenStatusResult()
        }.valueOrThrow

    override suspend fun getAvailablePaymentMethods(sessionId: SessionId): List<PaymentMethod> =
        provider.get<PaymentsApi>(sessionId).invoke {
            getPaymentMethods().paymentMethods.map {
                PaymentMethod(it.id, PaymentMethodType.getByValue(it.type), it.toDetails())
            }
        }.valueOrThrow

    override suspend fun validateSubscription(
        sessionId: SessionId?,
        codes: List<String>?,
        planIds: List<String>,
        currency: Currency,
        cycle: SubscriptionCycle
    ): SubscriptionStatus =
        provider.get<PaymentsApi>(sessionId = sessionId).invoke {
            validateSubscription(CheckSubscription(codes, planIds, currency.name, cycle.value)).toSubscriptionStatus()
        }.valueOrThrow

    override suspend fun getSubscription(sessionId: SessionId): Subscription =
        provider.get<PaymentsApi>(sessionId).invoke {
            getCurrentSubscription().subscription.toSubscription()
        }.valueOrThrow

    override suspend fun createOrUpdateSubscription(
        sessionId: SessionId,
        amount: Long,
        currency: Currency,
        payment: PaymentBody?,
        codes: List<String>?,
        planIds: Map<String, Int>,
        cycle: SubscriptionCycle
    ): Subscription =
        provider.get<PaymentsApi>(sessionId = sessionId).invoke {
            val paymentBodyEntity = when (payment) {
                is PaymentBody.TokenPaymentBody -> TokenTypePaymentBody(tokenDetails = TokenDetails(payment.token))
                else -> null
            }.exhaustive
            createUpdateSubscription(
                CreateSubscription(
                    amount,
                    currency.name,
                    paymentBodyEntity,
                    codes,
                    planIds,
                    cycle.value
                )
            ).subscription.toSubscription()
        }.valueOrThrow
}
