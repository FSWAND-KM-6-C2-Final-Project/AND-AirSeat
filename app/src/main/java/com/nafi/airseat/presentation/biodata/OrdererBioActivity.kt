package com.nafi.airseat.presentation.biodata

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
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
        val flightId = intent.getStringExtra("flightId")
        if (flightId != null) {
            Toast.makeText(this, "Received flight ID: $flightId", Toast.LENGTH_SHORT).show()
        }
        setupForm()
        setClickListener()
        observeData()
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
        binding.minusAdult.setOnClickListener {
            ordererBioViewModel.subAdult()
        }
        binding.plusAdult.setOnClickListener {
            ordererBioViewModel.addAdult()
        }
        binding.minusChild.setOnClickListener {
            ordererBioViewModel.subChild()
        }
        binding.plusChild.setOnClickListener {
            ordererBioViewModel.addChild()
        }
        binding.minusBaby.setOnClickListener {
            ordererBioViewModel.subBaby()
        }
        binding.plusBaby.setOnClickListener {
            ordererBioViewModel.addBaby()
        }
    }

    private fun observeData() {
        ordererBioViewModel.adultCount.observe(this) {
            binding.adultCount.text = it.toString()
        }
        ordererBioViewModel.childCount.observe(this) {
            binding.childCount.text = it.toString()
        }
        ordererBioViewModel.babyCount.observe(this) {
            binding.babyCount.text = it.toString()
        }
    }

    private fun observeInputMode() {
        ordererBioViewModel.isFamilyNameMode.observe(this) { isFamilyNameMode ->
            binding.layoutFormTicketBooker.tilFamilyName.isVisible = isFamilyNameMode
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
            (
                !isFamilyNameMode ||
                    checkFamilyNameValidation(
                        binding.layoutFormTicketBooker.etFamilyName.text.toString().trim(),
                    )
            )
    }

    private fun checkFullNameValidation(fullName: String): Boolean {
        return if (fullName.isEmpty()) {
            binding.layoutFormTicketBooker.tilFullname.isErrorEnabled = true
            binding.layoutFormTicketBooker.tilFullname.error =
                getString(R.string.text_error_fullName_cannot_be_empty)
            false
        } else {
            binding.layoutFormTicketBooker.tilFullname.isErrorEnabled = false
            true
        }
    }

    private fun checkPhoneNumberValidation(phoneNumber: String): Boolean {
        return if (phoneNumber.isEmpty()) {
            binding.layoutFormTicketBooker.tilNoPhone.isErrorEnabled = true
            binding.layoutFormTicketBooker.tilNoPhone.error =
                getString(R.string.text_error_number_phone_cannot_be_empty)
            false
        } else {
            binding.layoutFormTicketBooker.tilNoPhone.isErrorEnabled = false
            true
        }
    }

    private fun checkEmailValidation(email: String): Boolean {
        return if (email.isEmpty()) {
            binding.layoutFormTicketBooker.tilEmail.isErrorEnabled = true
            binding.layoutFormTicketBooker.tilEmail.error =
                getString(R.string.text_error_email_should_not_be_empty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutFormTicketBooker.tilEmail.error =
                getString(R.string.text_error_enter_a_valid_format_email)
            false
        } else {
            binding.layoutFormTicketBooker.tilEmail.isErrorEnabled = false
            true
        }
    }

    private fun checkFamilyNameValidation(familyName: String): Boolean {
        return if (familyName.isEmpty()) {
            binding.layoutFormTicketBooker.tilFamilyName.isErrorEnabled = true
            binding.layoutFormTicketBooker.tilFamilyName.error =
                getString(R.string.text_error_family_name_cannot_be_empty)
            false
        } else {
            binding.layoutFormTicketBooker.tilFamilyName.isErrorEnabled = false
            true
        }
    }

    private fun doSaveData() {
        if (isFormValid()) {
            val fullName = binding.layoutFormTicketBooker.etFullname.text.toString().trim()
            val numberPhone = binding.layoutFormTicketBooker.etNoPhone.text.toString().trim()
            val email = binding.layoutFormTicketBooker.etEmail.text.toString().trim()
            val isFamilyNameMode = ordererBioViewModel.isFamilyNameMode.value ?: false
            val familyName = if (isFamilyNameMode) binding.layoutFormTicketBooker.etFamilyName.text.toString().trim() else ""

            val intent =
                Intent(this, PassengerBioActivity::class.java).apply {
                    putExtra("full_name", fullName)
                    putExtra("number_phone", numberPhone)
                    putExtra("email", email)
                    putExtra("family_name", familyName)
                    putExtra("adult_count", ordererBioViewModel.adultCount.value ?: 0)
                    putExtra("child_count", ordererBioViewModel.childCount.value ?: 0)
                    putExtra("baby_count", ordererBioViewModel.babyCount.value ?: 0)
                }
            startActivity(intent)
        }
    }
}
