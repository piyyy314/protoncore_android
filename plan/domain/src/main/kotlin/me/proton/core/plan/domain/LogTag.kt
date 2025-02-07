/*
 * Copyright (c) 2023 Proton  AG
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

package me.proton.core.plan.domain

object LogTag {
    /** Default tag for this module. */
    const val DEFAULT: String = "core.plan"

    /** Tag for dynamic plans parsing error. */
    const val DYN_PLANS_PARSE: String = "core.plan.dynamic.parse"

    /** Errors related to Purchases. */
    const val PURCHASE_ERROR: String = "core.plan.purchase.error"

    /** Info related to Purchases. */
    const val PURCHASE_INFO: String = "core.plan.purchase.info"

    /** Tag related to Dynamic Plan Prices. */
    const val PRICE_ERROR: String = "core.plan.purchase.price.error"
}
