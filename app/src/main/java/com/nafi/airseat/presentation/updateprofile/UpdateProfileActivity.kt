package com.nafi.airseat.presentation.updateprofile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.nafi.airseat.R
import com.nafi.airseat.data.source.network.model.profile.UpdateProfileRequest
import com.nafi.airseat.databinding.ActivityUpdateProfileBinding
import com.nafi.airseat.utils.ApiErrorException
import com.nafi.airseat.utils.NoInternetException
import com.nafi.airseat.utils.proceedWhen
import com.nafi.airseat.utils.showSnackBarError
import com.nafi.airseat.utils.showSnackBarSuccess
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdateProfileActivity : AppCompatActivity() {
    private val binding: ActivityUpdateProfileBinding by lazy {
        ActivityUpdateProfileBinding.inflate(layoutInflater)
    }

    private val viewModel: UpdateProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setEditText()
        setButtonEnable()
        setClickListener()
    }

    private fun setButtonEnable() {
        binding.etProfileFullName.doAfterTextChanged {
            binding.btnUpdate.isEnabled = true
        }
        binding.etProfilePhoneNumber.doAfterTextChanged {
            binding.btnUpdate.isEnabled = true
        }
    }

    private fun setEditText() {
        binding.etProfileFullName.setText(intent.getStringExtra("fullName"))
        binding.etProfilePhoneNumber.setText(intent.getStringExtra("phoneNumber"))
    }

    private fun setClickListener() {
        binding.btnUpdate.setOnClickListener {
            val fullName: String = binding.etProfileFullName.text.toString()
            val phoneNumber: String = binding.etProfilePhoneNumber.text.toString()
            updateProfileData(fullName, phoneNumber)
        }

        binding.ivArrowBack.setOnClickListener {
            finish()
        }
    }

    private fun updateProfileData(
        fullName: String,
        phoneNumber: String,
    ) {
        viewModel.getUpdateProfile(setRequestData(fullName, phoneNumber)).observe(this) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.btnUpdate.isEnabled = false
                    binding.pbUpdateProfile.isVisible = true
                    "".also { binding.btnUpdate.text = it }
                },
                doOnSuccess = {
                    binding.btnUpdate.isEnabled = true
                    binding.pbUpdateProfile.isVisible = true
                    binding.btnUpdate.text = getString(R.string.text_button_update_profile)
                    showSnackBarSuccess("$it")
                    finish()
                },
                doOnEmpty = {
                    binding.btnUpdate.isEnabled = true
                    binding.pbUpdateProfile.isVisible = true
                    binding.btnUpdate.text = getString(R.string.text_button_update_profile)
                },
                doOnError = {
                    if (it.exception is ApiErrorException) {
                        showSnackBarError("$it")
                    } else if (it.exception is NoInternetException) {
                        showSnackBarError(getString(R.string.text_no_internet))
                    }
                    binding.btnUpdate.isEnabled = true
                    binding.pbUpdateProfile.isVisible = true
                    binding.btnUpdate.text = getString(R.string.text_button_update_profile)
                },
            )
        }
    }

    private fun setRequestData(
        fullName: String,
        phoneNumber: String,
    ): UpdateProfileRequest {
        return UpdateProfileRequest(fullName, phoneNumber)
    }
}
