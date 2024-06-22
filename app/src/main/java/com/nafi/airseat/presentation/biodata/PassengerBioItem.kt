package com.nafi.airseat.presentation.biodata

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import com.nafi.airseat.R
import com.nafi.airseat.databinding.LayoutFormOrderTicketBinding
import com.xwray.groupie.viewbinding.BindableItem
import java.util.Calendar
import java.util.Locale

class PassengerBioItem(
    val passengerType: String,
    private val itemTitle: List<String>,
    private val itemIdType: List<String>,
    private val countryNames: List<String>,
    private val passengerBioViewModel: PassengerBioViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val isBaby: Boolean,
) : BindableItem<LayoutFormOrderTicketBinding>() {
    lateinit var binding: LayoutFormOrderTicketBinding

    override fun getLayout(): Int = R.layout.layout_form_order_ticket

    override fun initializeViewBinding(view: View): LayoutFormOrderTicketBinding {
        return LayoutFormOrderTicketBinding.bind(view)
    }

    override fun bind(
        viewBinding: LayoutFormOrderTicketBinding,
        position: Int,
    ) {
        binding = viewBinding
        val passengerNumber = position + 1
        viewBinding.tvHeaderPassenger.text =
            viewBinding.root.context.getString(
                R.string.passenger_passengerindex,
                passengerNumber,
                passengerType,
            )
        setupTitleDropdown(viewBinding, viewBinding.root.context)
        setupIdTypeDropdown(viewBinding, viewBinding.root.context)
        setupCountryDropdown(viewBinding, viewBinding.root.context)
        viewBinding.etDateOfBirth.setOnClickListener {
            showDatePickerDialog { date -> viewBinding.etDateOfBirth.setText(date) }
        }
        viewBinding.etValidId.setOnClickListener {
            showDatePickerDialog { date -> viewBinding.etValidId.setText(date) }
        }
        val switchFamilyName = viewBinding.swFamilyName
        switchFamilyName.isChecked = passengerBioViewModel.isFamilyNameMode.value ?: false
        switchFamilyName.setOnCheckedChangeListener { _, isChecked ->
            passengerBioViewModel.setFamilyNameModeForPassenger(position, isChecked)
        }

        // Observe Family Name mode specific to this passenger
        passengerBioViewModel.getFamilyNameModeForPassenger(position)
            .observe(lifecycleOwner) { isFamilyNameMode ->
                updateFamilyNameVisibility(isFamilyNameMode)
            }

        // Set initial visibility based on switch state
        updateFamilyNameVisibility(switchFamilyName.isChecked)

        if (isBaby) {
            viewBinding.tilIdType.isVisible = false
            viewBinding.actvIdType.isVisible = false
            viewBinding.tvIdType.isVisible = false
            viewBinding.tilIdCard.isVisible = false
            viewBinding.etIdCard.isVisible = false
            viewBinding.tvIdCard.isVisible = false
            viewBinding.tilCountry.isVisible = false
            viewBinding.actvCountry.isVisible = false
            viewBinding.tvCountry.isVisible = false
            viewBinding.tilValidId.isVisible = false
            viewBinding.etValidId.isVisible = false
            viewBinding.tvValid.isVisible = false
        }
    }

    private fun updateFamilyNameVisibility(isVisible: Boolean) {
        binding.tilFamilyName.isVisible = isVisible
        binding.etFamilyName.isVisible = isVisible
        binding.etFamilyName.isEnabled = isVisible
        binding.tvFmName.isVisible = isVisible
    }

    @SuppressLint("DefaultLocale")
    private fun showDatePickerDialog(onDateSet: (String) -> Unit) {
        val context = binding.root.context
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(
                context,
                { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    val formattedDate =
                        String.format(
                            "%02d/%02d/%04d",
                            selectedDay,
                            selectedMonth + 1,
                            selectedYear,
                        )
                    onDateSet(formattedDate)
                },
                year,
                month,
                day,
            )

        datePickerDialog.show()
    }

    private fun setupTitleDropdown(
        binding: LayoutFormOrderTicketBinding,
        context: Context,
    ) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, itemTitle)
        binding.actvTitle.setAdapter(adapter)
    }

    private fun setupIdTypeDropdown(
        binding: LayoutFormOrderTicketBinding,
        context: Context,
    ) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, itemIdType)
        binding.actvIdType.setAdapter(adapter)
    }

    private fun setupCountryDropdown(
        binding: LayoutFormOrderTicketBinding,
        context: Context,
    ) {
        val adapter =
            ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, countryNames)
        binding.actvCountry.setAdapter(adapter)
    }

    fun validateInput(
        binding: LayoutFormOrderTicketBinding,
        context: Context,
    ): Boolean {
        if (isBaby) {
            val title = binding.actvTitle.text.toString().lowercase(Locale.getDefault())
            val fullName = binding.etFullname.text.toString().trim()
            val dateOfBirth = binding.etDateOfBirth.text.toString().trim()
            val citizenship = binding.etCitizenship.text.toString().trim()
            val isFamilyNameMode = passengerBioViewModel.isFamilyNameMode.value ?: false

            return ValidationUtil.checkTitleValidation(title, binding, context) &&
                ValidationUtil.checkFullnameValidation(fullName, binding, context) &&
                (
                    !isFamilyNameMode ||
                        ValidationUtil.checkFamilyNameValidation(
                            binding.etFamilyName.text.toString().trim(),
                            binding,
                            context,
                        )
                ) &&
                ValidationUtil.checkDateOfBirthValidation(dateOfBirth, binding, context) &&
                ValidationUtil.checkCitizenshipValidation(citizenship, binding, context)
        }

        val title = binding.actvTitle.text.toString().lowercase(Locale.getDefault())
        val country = binding.actvCountry.text.toString()
        val fullName = binding.etFullname.text.toString().trim()
        val citizenship = binding.etCitizenship.text.toString().trim()
        val idType = binding.actvIdType.text.toString().lowercase(Locale.getDefault())
        val idCard = binding.etIdCard.text.toString().trim()
        val validId = binding.etValidId.text.toString().trim()
        val dateOfBirth = binding.etDateOfBirth.text.toString().trim()
        val isFamilyNameMode = passengerBioViewModel.isFamilyNameMode.value ?: false

        return ValidationUtil.checkTitleValidation(title, binding, context) &&
            ValidationUtil.checkFullnameValidation(fullName, binding, context) &&
            (
                !isFamilyNameMode ||
                    ValidationUtil.checkFamilyNameValidation(
                        binding.etFamilyName.text.toString().trim(), binding, context,
                    )
            ) &&
            ValidationUtil.checkDateOfBirthValidation(dateOfBirth, binding, context) &&
            ValidationUtil.checkCitizenshipValidation(citizenship, binding, context) &&
            ValidationUtil.checkIdTypeValidation(idType, binding, context) &&
            ValidationUtil.checkIdCardValidation(idCard, binding, context) &&
            ValidationUtil.checkCountryValidation(country, binding, context) &&
            ValidationUtil.checkValidIdValidation(validId, binding, context)
    }
}

object ValidationUtil {
    fun checkIdTypeValidation(
        idType: String,
        binding: LayoutFormOrderTicketBinding,
        context: Context,
    ): Boolean {
        return if (idType.isEmpty()) {
            binding.tilIdType.isErrorEnabled = true
            binding.tilIdType.error =
                context.getString(R.string.text_error_title_cannot_be_empty)
            false
        } else {
            binding.tilIdType.isErrorEnabled = false
            true
        }
    }

    fun checkDateOfBirthValidation(
        dateOfBirth: String,
        binding: LayoutFormOrderTicketBinding,
        context: Context,
    ): Boolean {
        return if (dateOfBirth.isEmpty()) {
            binding.tilDate.isErrorEnabled = true
            binding.tilDate.error =
                context.getString(R.string.text_error_date_of_birth_is_required)
            false
        } else {
            binding.tilDate.isErrorEnabled = false
            true
        }
    }

    fun checkCountryValidation(
        country: String,
        binding: LayoutFormOrderTicketBinding,
        context: Context,
    ): Boolean {
        return if (country.isEmpty()) {
            binding.tilCountry.isErrorEnabled = true
            binding.tilCountry.error =
                context.getString(R.string.hint_select_country)
            false
        } else {
            binding.tilCountry.isErrorEnabled = false
            true
        }
    }

    fun checkTitleValidation(
        title: String,
        binding: LayoutFormOrderTicketBinding,
        context: Context,
    ): Boolean {
        return if (title.isEmpty()) {
            binding.tilTitle.isErrorEnabled = true
            binding.tilTitle.error =
                context.getString(R.string.text_error_title_cannot_be_empty)
            false
        } else {
            binding.tilTitle.isErrorEnabled = false
            true
        }
    }

    fun checkFullnameValidation(
        fullName: String,
        binding: LayoutFormOrderTicketBinding,
        context: Context,
    ): Boolean {
        return if (fullName.isEmpty()) {
            binding.tilFullname.isErrorEnabled = true
            binding.tilFullname.error =
                context.getString(R.string.text_error_fullName_cannot_be_empty)
            false
        } else {
            binding.tilFullname.isErrorEnabled = false
            true
        }
    }

    fun checkIdCardValidation(
        idCard: String,
        binding: LayoutFormOrderTicketBinding,
        context: Context,
    ): Boolean {
        return if (idCard.isEmpty()) {
            binding.tilIdCard.isErrorEnabled = true
            binding.tilIdCard.error =
                context.getString(R.string.text_error_id_card_cannot_be_empty)
            false
        } else {
            binding.tilIdCard.isErrorEnabled = false
            true
        }
    }

    fun checkValidIdValidation(
        validId: String,
        binding: LayoutFormOrderTicketBinding,
        context: Context,
    ): Boolean {
        return if (validId.isEmpty()) {
            binding.tilValidId.isErrorEnabled = true
            binding.tilValidId.error =
                context.getString(R.string.text_error_the_id_card_validity_period_must_not_be_empty)
            false
        } else {
            binding.tilValidId.isErrorEnabled = false
            true
        }
    }

    fun checkCitizenshipValidation(
        citizenship: String,
        binding: LayoutFormOrderTicketBinding,
        context: Context,
    ): Boolean {
        return if (citizenship.isEmpty()) {
            binding.tilCitizenship.isErrorEnabled = true
            binding.tilCitizenship.error =
                context.getString(R.string.text_error_citizenship_cannot_be_empty)
            false
        } else {
            binding.tilCitizenship.isErrorEnabled = false
            true
        }
    }

    fun checkFamilyNameValidation(
        familyName: String,
        binding: LayoutFormOrderTicketBinding,
        context: Context,
    ): Boolean {
        return if (familyName.isEmpty()) {
            binding.tilFamilyName.isErrorEnabled = true
            binding.tilFamilyName.error =
                context.getString(R.string.text_error_family_name_cannot_be_empty)
            false
        } else {
            binding.tilFamilyName.isErrorEnabled = false
            true
        }
    }
}
