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

package me.proton.core.auth.presentation.ui.signup

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.proton.core.auth.presentation.R
import me.proton.core.auth.presentation.databinding.FragmentSignupRecoveryBinding
import me.proton.core.auth.presentation.entity.signup.RecoveryMethodType
import me.proton.core.auth.presentation.viewmodel.signup.RecoveryMethodViewModel
import me.proton.core.auth.presentation.viewmodel.signup.SignupViewModel
import me.proton.core.presentation.ui.view.ProtonProgressButton
import me.proton.core.presentation.utils.hideKeyboard
import me.proton.core.presentation.utils.onClick
import me.proton.core.presentation.viewmodel.ViewModelResult
import me.proton.core.util.kotlin.exhaustive

@AndroidEntryPoint
class RecoveryMethodFragment : SignupFragment<FragmentSignupRecoveryBinding>() {

    private val viewModel by viewModels<RecoveryMethodViewModel>()
    private val signupViewModel by activityViewModels<SignupViewModel>()

    override fun layoutId() = R.layout.fragment_signup_recovery

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            closeButton.onClick {
                if (nextButton.currentState == ProtonProgressButton.State.IDLE) {
                    parentFragmentManager.popBackStackImmediate()
                }
            }
            initTabs()
            initTermsAndConditions()
            skipButton.onClick(::showSkip)
            nextButton.onClick(::onNextClicked)
        }

        viewModel.recoveryMethodUpdate
            .onEach { setActiveVerificationMethod(it) }
            .launchIn(lifecycleScope)

        viewModel.validationResult.onEach {
            when (it) {
                is ViewModelResult.None -> {
                }
                is ViewModelResult.Error -> showError(it.throwable?.message)
                is ViewModelResult.Processing -> showLoading(true)
                is ViewModelResult.Success -> {
                    if (it.value) {
                        // if recovery destination is valid
                        val recoveryMethod = viewModel.recoveryMethod
                        signupViewModel.setRecoveryMethod(recoveryMethod)
                    } else {
                        showError(getString(R.string.auth_signup_error_validation_recovery_destination))
                    }
                }
            }.exhaustive
        }.launchIn(lifecycleScope)

        observeForHumanVerificationFailed()
    }

    private fun initTabs() = with(binding) {
        recoveryOptions.apply {
            addTab(newTab().apply {
                text = getString(R.string.auth_signup_recovery_method_email)
                tag = RecoveryMethodType.EMAIL
            })
            addTab(newTab().apply {
                text = getString(R.string.auth_signup_recovery_method_phone)
                tag = RecoveryMethodType.SMS
            })

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        val recoveryMethod = tab.tag as RecoveryMethodType
                        viewModel.setActiveRecoveryMethod(recoveryMethod)
                    }
                }
            })
        }
    }

    private fun initTermsAndConditions() = with(binding.termsConditionsText) {
        val clickableString = getString(R.string.auth_signup_terms_conditions)
        val spannableString = SpannableString(getString(R.string.auth_signup_terms_conditions_full))
        val startIndex = spannableString.indexOf(clickableString)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                childFragmentManager.showTermsConditions()
            }
        }
        spannableString.setSpan(
            clickableSpan,
            startIndex,
            startIndex + clickableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text = spannableString
        movementMethod = LinkMovementMethod.getInstance()
    }

    private fun observeForHumanVerificationFailed() {
        signupViewModel.userCreationState.onEach {
            // this fragment is only interested in HV and error states, all other are handled by the activity
            when (it) {
                is SignupViewModel.State.Error.HumanVerification,
                is SignupViewModel.State.Error.PlanChooserCancel,
                is SignupViewModel.State.Error.Message -> showLoading(false)
                is SignupViewModel.State.Idle,
                is SignupViewModel.State.Processing,
                is SignupViewModel.State.Success -> {
                }
            }.exhaustive
        }.launchIn(lifecycleScope)
    }

    private fun setActiveVerificationMethod(methodType: RecoveryMethodType) {
        when (methodType) {
            RecoveryMethodType.EMAIL ->
                childFragmentManager.showEmailRecoveryMethodFragment(containerId = binding.fragmentOptionsContainer.id)
            RecoveryMethodType.SMS ->
                childFragmentManager.showSMSRecoveryMethodFragment(containerId = binding.fragmentOptionsContainer.id)
        }.exhaustive
    }

    private fun onNextClicked() {
        hideKeyboard()
        if (viewModel.recoveryMethod.isSet) {
            viewModel.validateRecoveryDestinationInput()
        } else {
            showSkip()
        }
    }

    private fun showSkip() {
        with(requireContext()) {
            childFragmentManager.showSkipRecoveryDialog(this) {
                showLoading()
                signupViewModel.skipRecoveryMethod()
            }
        }
    }

    override fun showLoading(loading: Boolean) = with(binding) {
        if (loading) {
            nextButton.setLoading()
        } else {
            nextButton.setIdle()
        }
    }

    companion object {
        operator fun invoke() = RecoveryMethodFragment()
    }
}
