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

package me.proton.core.countries.domain.usecase

import me.proton.core.countries.domain.repository.CountriesRepository
import javax.inject.Inject

/**
 * Use case for fetching all [me.proton.core.countries.domain.entity.Country] needed for any country chooser purpose
 * like phone or country name itself.
 * The countries are preloaded in the module and it uses [CountriesRepository] to load them.
 */
class LoadCountries @Inject constructor(private val countriesRepository: CountriesRepository) {

    /**
     * Returns the countries which include or exclude the top five most used countries.
     */
    suspend operator fun invoke() = countriesRepository.getAllCountriesSorted()
}
