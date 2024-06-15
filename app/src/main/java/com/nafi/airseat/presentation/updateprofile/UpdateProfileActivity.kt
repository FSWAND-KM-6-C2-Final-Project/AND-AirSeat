package com.nafi.airseat.presentation.updateprofile

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.nafi.airseat.R
import com.nafi.airseat.data.source.network.model.profile.UpdateProfileRequest
import com.nafi.airseat.databinding.ActivityUpdateProfileBinding
import com.nafi.airseat.utils.proceedWhen
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
        setClickListener()
    }

    private fun setEditText() {
        binding.etProfileFullName.setText(intent.getStringExtra("fullName"))
    }

    private fun setClickListener() {
        binding.btnUpdate.setOnClickListener {
            val fullName: String = binding.etProfileFullName.text.toString()
            updateProfileData(fullName)
        }
    }

    private fun updateProfileData(fullName: String) {
        viewModel.getUpdateProfile(setRequestData(fullName)).observe(this) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.btnUpdate.isActivated = false
                    binding.btnUpdate.text = ""
                },
                doOnSuccess = {
                    binding.btnUpdate.isActivated = true
                    binding.btnUpdate.text = getString(R.string.text_button_update_profile)
                    result.payload?.let {
                        showSnackbarSuccess(it.message)
                    }
                    finish()
                },
                doOnEmpty = {
                    binding.btnUpdate.isActivated = true
                    binding.btnUpdate.text = getString(R.string.text_button_update_profile)
                },
                doOnError = {
                    result.payload?.let {
                        showSnackbarError(it.message)
                    }
                    binding.btnUpdate.isActivated = true
                    binding.btnUpdate.text = getString(R.string.text_button_update_profile)
                },
            )
        }
    }

    private fun setRequestData(fullName: String): UpdateProfileRequest {
        return UpdateProfileRequest(fullName)
    }

    @SuppressLint("RestrictedApi")
    private fun showSnackbarSuccess(message: String) {
        val snackbar = Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG)
        val customView = LayoutInflater.from(this).inflate(R.layout.custom_snackbar_success, null)
        customView.findViewById<TextView>(R.id.textView1).text = message
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)

        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(customView, 0)
        snackbar.show()
    }

    @SuppressLint("RestrictedApi")
    private fun showSnackbarError(message: String) {
        val snackbar = Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG)
        val customView = LayoutInflater.from(this).inflate(R.layout.custom_snackbar_error, null)
        customView.findViewById<TextView>(R.id.textView1).text = message
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)

        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(customView, 0)
        snackbar.show()
    }
}

}
