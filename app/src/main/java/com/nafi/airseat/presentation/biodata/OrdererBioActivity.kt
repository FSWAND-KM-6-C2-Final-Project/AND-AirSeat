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
        val flightId = intent.getStringExtra("id")
        val returnFlight = intent.getIntExtra("idReturn", 0)
        if (flightId != null) {
            Toast.makeText(this, "Received flight ID: $flightId & $returnFlight", Toast.LENGTH_SHORT).show()
        }
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
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.layoutFormTicketBooker.swFamilyName.setOnClickListener {
            ordererBioViewModel.changeInputMode()
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
            val airportCityCodeDeparture = intent.getStringExtra("airportCityCodeDeparture")
            val airportCityCodeDestination = intent.getStringExtra("airportCityCodeDestination")
            val seatClassChoose = intent.getStringExtra("seatClassChoose")
            val adultCount = intent.getIntExtra("adultCount", 0)
            val childCount = intent.getIntExtra("childCount", 0)
            val babyCount = intent.getIntExtra("babyCount", 0)
            val flightId = intent.getStringExtra("id")
            val idReturn = intent.getIntExtra("idReturn", 0)
            val price = intent.getIntExtra("price", 0)

            val intent =
                Intent(this, PassengerBioActivity::class.java).apply {
                    putExtra("full_name", fullName)
                    putExtra("number_phone", numberPhone)
                    putExtra("email", email)
                    putExtra("family_name", familyName)
                    putExtra("flightId", flightId)
                    putExtra("price", price)
                    putExtra("airportCityCodeDeparture", airportCityCodeDeparture)
                    putExtra("airportCityCodeDestination", airportCityCodeDestination)
                    putExtra("seatClassChoose", seatClassChoose)
                    putExtra("adultCount", adultCount)
                    putExtra("childCount", childCount)
                    putExtra("babyCount", babyCount)
                    putExtra("idReturn", idReturn)
                }
            startActivity(intent)
        }
    }
}
