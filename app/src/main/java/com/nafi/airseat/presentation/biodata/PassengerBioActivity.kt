package com.nafi.airseat.presentation.biodata

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityPassengerBioBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PassengerBioActivity : AppCompatActivity() {
    private val binding: ActivityPassengerBioBinding by lazy {
        ActivityPassengerBioBinding.inflate(layoutInflater)
    }

    private val passengerBioViewModel: PassengerBioViewModel by viewModel()

    private val itemTitle = listOf("Mr", "Ms")

    private val countryNames =
        listOf(
            "Afghanistan",
            "Albania",
            "Algeria",
            "Andorra",
            "Angola",
            "Antigua and Barbuda",
            "Argentina",
            "Armenia",
            "Australia",
            "Austria",
            "Azerbaijan",
            "Bahamas",
            "Bahrain",
            "Bangladesh",
            "Barbados",
            "Belarus",
            "Belgium",
            "Belize",
            "Benin",
            "Bhutan",
            "Bolivia",
            "Bosnia and Herzegovina",
            "Botswana",
            "Brazil",
            "Brunei",
            "Bulgaria",
            "Burkina Faso",
            "Burundi",
            "Cabo Verde",
            "Cambodia",
            "Cameroon",
            "Canada",
            "Central African Republic",
            "Chad",
            "Chile",
            "China",
            "Colombia",
            "Comoros",
            "Congo (Congo-Brazzaville)",
            "Costa Rica",
            "Croatia",
            "Cuba",
            "Cyprus",
            "Czechia (Czech Republic)",
            "Democratic Republic of the Congo",
            "Denmark",
            "Djibouti",
            "Dominica",
            "Dominican Republic",
            "Ecuador",
            "Egypt",
            "El Salvador",
            "Equatorial Guinea",
            "Eritrea",
            "Estonia",
            "Eswatini",
            "Ethiopia",
            "Fiji",
            "Finland",
            "France",
            "Gabon",
            "Gambia",
            "Georgia",
            "Germany",
            "Ghana",
            "Greece",
            "Grenada",
            "Guatemala",
            "Guinea",
            "Guinea-Bissau",
            "Guyana",
            "Haiti",
            "Honduras",
            "Hungary",
            "Iceland",
            "India",
            "Indonesia",
            "Iran",
            "Iraq",
            "Ireland",
            "Israel",
            "Italy",
            "Jamaica",
            "Japan",
            "Jordan",
            "Kazakhstan",
            "Kenya",
            "Kiribati",
            "Kuwait",
            "Kyrgyzstan",
            "Laos",
            "Latvia",
            "Lebanon",
            "Lesotho",
            "Liberia",
            "Libya",
            "Liechtenstein",
            "Lithuania",
            "Luxembourg",
            "Madagascar",
            "Malawi",
            "Malaysia",
            "Maldives",
            "Mali",
            "Malta",
            "Marshall Islands",
            "Mauritania",
            "Mauritius",
            "Mexico",
            "Micronesia",
            "Moldova",
            "Monaco",
            "Mongolia",
            "Montenegro",
            "Morocco",
            "Mozambique",
            "Myanmar (formerly Burma)",
            "Namibia",
            "Nauru",
            "Nepal",
            "Netherlands",
            "New Zealand",
            "Nicaragua",
            "Niger",
            "Nigeria",
            "North Korea",
            "North Macedonia",
            "Norway",
            "Oman",
            "Pakistan",
            "Palau",
            "Palestine State",
            "Panama",
            "Papua New Guinea",
            "Paraguay",
            "Peru",
            "Philippines",
            "Poland",
            "Portugal",
            "Qatar",
            "Romania",
            "Russia",
            "Rwanda",
            "Saint Kitts and Nevis",
            "Saint Lucia",
            "Saint Vincent and the Grenadines",
            "Samoa",
            "San Marino",
            "Sao Tome and Principe",
            "Saudi Arabia",
            "Senegal",
            "Serbia",
            "Seychelles",
            "Sierra Leone",
            "Singapore",
            "Slovakia",
            "Slovenia",
            "Solomon Islands",
            "Somalia",
            "South Africa",
            "South Korea",
            "South Sudan",
            "Spain",
            "Sri Lanka",
            "Sudan",
            "Suriname",
            "Sweden",
            "Switzerland",
            "Syria",
            "Tajikistan",
            "Tanzania",
            "Thailand",
            "Timor-Leste",
            "Togo",
            "Tonga",
            "Trinidad and Tobago",
            "Tunisia",
            "Turkey",
            "Turkmenistan",
            "Tuvalu",
            "Uganda",
            "Ukraine",
            "United Arab Emirates",
            "United Kingdom",
            "United States of America",
            "Uruguay",
            "Uzbekistan",
            "Vanuatu",
            "Vatican City",
            "Venezuela",
            "Vietnam",
            "Yemen",
            "Zambia",
            "Zimbabwe",
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupForm()
        setClickListener()
        observeInputMode()
        setupDropdownTitle()
        setupDropdownCountry()
        setupDatePicker()
    }

    private fun setupDatePicker() {
        val dateOfBirth = binding.rvFormOrderTicket1.etDateOfBirth

        dateOfBirth.setOnClickListener {
            showDatePickerDialog()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(
                this,
                { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                    binding.rvFormOrderTicket1.etDateOfBirth.setText(formattedDate)
                },
                year,
                month,
                day,
            )

        datePickerDialog.show()
    }

    private fun setupDropdownCountry() {
        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                countryNames,
            )
        binding.rvFormOrderTicket1.actvCountry.setAdapter(adapter)
    }

    private fun setupDropdownTitle() {
        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                itemTitle,
            )
        binding.rvFormOrderTicket1.actvTitle.setAdapter(adapter)
    }

    private fun setupForm() {
        with(binding.rvFormOrderTicket1) {
            tvFmName.isVisible = false
            etFamilyName.isVisible = false
            etFamilyName.isEnabled = false
        }
    }

    private fun setClickListener() {
        binding.mbSave.setOnClickListener {
            doSaveData()
        }
        binding.rvFormOrderTicket1.swFamilyName.setOnClickListener {
            passengerBioViewModel.changeInputMode()
        }
    }

    private fun observeInputMode() {
        passengerBioViewModel.isFamilyNameMode.observe(this) { isFamilyNameMode ->
            binding.rvFormOrderTicket1.etFamilyName.isVisible = isFamilyNameMode
            binding.rvFormOrderTicket1.etFamilyName.isEnabled = isFamilyNameMode
            binding.rvFormOrderTicket1.tvFmName.isVisible = isFamilyNameMode
        }
    }

    private fun isFormValid(): Boolean {
        val title = binding.rvFormOrderTicket1.actvTitle.text.toString()
        val fullName = binding.rvFormOrderTicket1.etFullname.text.toString().trim()
        val citizenship = binding.rvFormOrderTicket1.etCitizenship.text.toString().trim()
        val idCard = binding.rvFormOrderTicket1.etIdCard.text.toString().trim()
        val validId = binding.rvFormOrderTicket1.etValidId.text.toString().trim()
        val isFamilyNameMode = passengerBioViewModel.isFamilyNameMode.value ?: false

        return checkFullnameValidation(fullName) &&
            checkCitizenshipValidation(citizenship) &&
            checkIdCardValidation(idCard) &&
            checkValidIdValidation(validId) &&
            (
                !isFamilyNameMode ||
                    checkFamilyNameValidation(
                        binding.rvFormOrderTicket1.etFamilyName.text.toString().trim(),
                    )
            )
    }

    private fun checkFullnameValidation(fullName: String): Boolean {
        return if (fullName.isEmpty()) {
            binding.rvFormOrderTicket1.etFullname.error =
                getString(R.string.text_error_fullName_cannot_be_empty)
            false
        } else {
            binding.rvFormOrderTicket1.etFullname.error = null
            true
        }
    }

    private fun checkIdCardValidation(idCard: String): Boolean {
        val idCardLong = idCard.toLongOrNull()
        return if (idCard.isEmpty()) {
            binding.rvFormOrderTicket1.etIdCard.error =
                getString(R.string.text_error_id_card_cannot_be_empty)
            false
        } else if (idCardLong == null) {
            binding.rvFormOrderTicket1.etIdCard.error =
                getString(R.string.text_error_id_card_must_be_number)
            false
        } else {
            binding.rvFormOrderTicket1.etIdCard.error = null
            true
        }
    }

    private fun checkValidIdValidation(validId: String): Boolean {
        return if (validId.isEmpty()) {
            binding.rvFormOrderTicket1.etValidId.error =
                getString(R.string.text_error_the_id_card_validity_period_must_not_be_empty)
            false
        } else {
            binding.rvFormOrderTicket1.etValidId.error = null
            true
        }
    }

    private fun checkCitizenshipValidation(citizenship: String): Boolean {
        return if (citizenship.isEmpty()) {
            binding.rvFormOrderTicket1.etCitizenship.error =
                getString(R.string.text_error_citizenship_cannot_be_empty)
            false
        } else {
            binding.rvFormOrderTicket1.etCitizenship.error = null
            true
        }
    }

    private fun checkFamilyNameValidation(familyName: String): Boolean {
        return if (familyName.isEmpty()) {
            binding.rvFormOrderTicket1.etFamilyName.error =
                getString(R.string.text_error_family_name_cannot_be_empty)
            false
        } else {
            binding.rvFormOrderTicket1.etFamilyName.error = null
            true
        }
    }

    private fun doSaveData() {
        if (isFormValid()) {
            val fullName = binding.rvFormOrderTicket1.etFullname.text.toString().trim()
            val citizenship = binding.rvFormOrderTicket1.etCitizenship.text.toString().trim()
            val idCard = binding.rvFormOrderTicket1.etIdCard.text.toString().trim()
            val validId = binding.rvFormOrderTicket1.etValidId.text.toString().trim()
            val isFamilyNameMode = passengerBioViewModel.isFamilyNameMode.value ?: false
        }
    }
}
