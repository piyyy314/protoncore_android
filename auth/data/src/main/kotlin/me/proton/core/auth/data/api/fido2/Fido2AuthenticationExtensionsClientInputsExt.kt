/*
 * Copyright (c) 2024 Proton Technologies AG
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

package me.proton.core.auth.data.api.fido2

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import me.proton.core.auth.fido.domain.entity.Fido2AuthenticationExtensionsClientInputs

internal fun Fido2AuthenticationExtensionsClientInputs.toJson(): JsonObject? = JsonObject(
    buildMap {
        if (appId != null) {
            put("appid", JsonPrimitive(appId))
        }
        if (thirdPartyPayment != null) {
            put("thirdPartyPayment", JsonPrimitive(thirdPartyPayment))
        }
        if (uvm != null) {
            put("uvm", JsonPrimitive(uvm))
        }
    }
).takeIf { it.isNotEmpty() }

internal fun PublicKeyCredentialRequestOptionsResponse.toFido2AuthenticationExtensionsClientInputs() =
    Fido2AuthenticationExtensionsClientInputs(
        appId = extensions?.get("appid")?.let { jsonElement ->
            (jsonElement as? JsonPrimitive)?.takeIf { it.isString }?.content?.takeIf { it.isNotEmpty() }
        },
        thirdPartyPayment = extensions?.get("thirdPartyPayment")?.let { jsonElement ->
            (jsonElement as? JsonPrimitive)?.booleanOrNull
        },
        uvm = extensions?.get("uvm")?.let { jsonElement ->
            (jsonElement as? JsonPrimitive)?.booleanOrNull
        },
    )
