/*
 * Copyright (c) 2021 Proton Technologies AG
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

package me.proton.core.test.android.plugins.data

import kotlinx.serialization.Serializable
import me.proton.core.test.android.instrumented.utils.StringUtils.randomString
import me.proton.core.test.android.instrumented.utils.StringUtils.stringFromResource
import java.util.Calendar
import java.util.Locale

@Serializable
data class Card(
    val number: String = "",
    val expMonth: String = "",
    val expYear: String = "",
    val name: String = "",
    val cvc: String = "",
    val country: String = "",
    val zip: String = ""
) {
    val brand: Brand = when (number.take(1).toInt()) {
        3 -> Brand.AmericanExpress
        4 -> Brand.Visa
        5 -> Brand.Mastercard
        else -> Brand.Unknown
    }

    val details: String
        get() = stringFromResource(
            me.proton.core.payment.presentation.R.string.payment_cc_list_item,
            brand.name,
            number.takeLast(4),
            expMonth,
            expYear
        )

    @Serializable
    enum class Brand(name: String) {
        Mastercard("MasterCard"),
        AmericanExpress("American Express"),
        Visa("Visa"),
        Unknown(""),
    }

    companion object {
        val default: Card = Card(
            number = "4242424242424242",
            expMonth = String.format(Locale.ROOT, "%02d", Calendar.getInstance().get(Calendar.MONTH) + 1),
            expYear = (Calendar.getInstance().get(Calendar.YEAR) + 1).toString(),
            name = "Test Account",
            cvc = (111..999).random().toString(),
            country = "Angola",
            zip = randomString(stringLength = 4)
        )
    }
}
