package com.nafi.airseat.presentation.biodata

import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityOrdererBioBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrdererBioActivity : AppCompatActivity() {
    private val binding: ActivityOrdererBioBinding by lazy {
        ActivityOrdererBioBinding.inflate(layoutInflater)
    }

    private val ordererBioViewModel: OrdererBioViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupForm()
        setClickListener()
        observeInputMode()
    }

    private fun setupForm() {
        with(binding.layoutFormTicketBooker) {
            tvFmName.isVisible = false
            etFamilyName.isVisible = false
            etFamilyName.isEnabled = false
        }
    }

    private fun setClickListener() {
        binding.mbSave.setOnClickListener {
            doSaveData()
        }
        binding.layoutFormTicketBooker.swFamilyName.setOnClickListener {
            ordererBioViewModel.changeInputMode()
        }
    }

    private fun observeInputMode() {
        ordererBioViewModel.isFamilyNameMode.observe(this) { isFamilyNameMode ->
            binding.layoutFormTicketBooker.etFamilyName.isVisible = isFamilyNameMode
            binding.layoutFormTicketBooker.etFamilyName.isEnabled = isFamilyNameMode
            binding.layoutFormTicketBooker.tvFmName.isVisible = isFamilyNameMode
        }
    }

    private fun isFormValid(): Boolean {
        val fullName = binding.layoutFormTicketBooker.etFullname.text.toString().trim()
        val phoneNumber = binding.layoutFormTicketBooker.etNoPhone.text.toString().trim()
        val email = binding.layoutFormTicketBooker.etEmail.text.toString().trim()
        val isFamilyNameMode = ordererBioViewModel.isFamilyNameMode.value ?: false

        return checkFullNameValidation(fullName) &&
            checkPhoneNumberValidation(phoneNumber) &&
            checkEmailValidation(email) &&
            (!isFamilyNameMode || checkFamilyNameValidation(binding.layoutFormTicketBooker.etFamilyName.text.toString().trim()))
    }

    private fun checkFullNameValidation(fullName: String): Boolean {
        return if (fullName.isEmpty()) {
            binding.layoutFormTicketBooker.etFullname.error =
                getString(R.string.text_error_fullName_cannot_be_empty)
            false
        } else {
            binding.layoutFormTicketBooker.etFullname.error = null
            true
        }
    }

    private fun checkPhoneNumberValidation(phoneNumber: String): Boolean {
        return if (phoneNumber.isEmpty()) {
            binding.layoutFormTicketBooker.etNoPhone.error =
                getString(R.string.text_error_number_phone_cannot_be_empty)
            false
        } else {
            binding.layoutFormTicketBooker.etNoPhone.error = null
            true
        }
    }

    private fun checkEmailValidation(email: String): Boolean {
        return if (email.isEmpty()) {
            binding.layoutFormTicketBooker.etEmail.error =
                getString(R.string.text_error_email_should_not_be_empty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutFormTicketBooker.etEmail.error =
                getString(R.string.text_error_enter_a_valid_format_email)
            false
        } else {
            binding.layoutFormTicketBooker.etEmail.error = null
            true
        }
    }

    private fun checkFamilyNameValidation(familyName: String): Boolean {
        return if (familyName.isEmpty()) {
            binding.layoutFormTicketBooker.etFamilyName.error =
                getString(R.string.text_error_family_name_cannot_be_empty)
            false
        } else {
            binding.layoutFormTicketBooker.etFamilyName.error = null
            true
        }
    }

    private fun doSaveData() {
        if (isFormValid()) {
            val fullName = binding.layoutFormTicketBooker.etFullname.text.toString().trim()
            val numberPhone = binding.layoutFormTicketBooker.etNoPhone.text.toString().trim()
            val email = binding.layoutFormTicketBooker.etEmail.text.toString().trim()
            val isFamilyNameMode = ordererBioViewModel.isFamilyNameMode.value ?: false

            if (isFamilyNameMode) {
                val familyName = binding.layoutFormTicketBooker.etFamilyName.text.toString().trim()
                saveDataWithFamilyName(fullName, numberPhone, email, familyName)
            } else {
                saveDataWithoutFamilyName(fullName, numberPhone, email)
            }
        }
    }

    private fun saveDataWithoutFamilyName(
        fullName: String,
        numberPhone: String,
        email: String,
    ) {
        TODO("Not yet implemented")
    }

    private fun saveDataWithFamilyName(
        fullName: String,
        numberPhone: String,
        email: String,
        familyName: String,
    ) {
        TODO("Not yet implemented")
    }
}
