package com.nafi.airseat.utils
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.nafi.airseat.R

@SuppressLint("RestrictedApi", "InflateParams")
fun Activity.showSnackBarError(message: String) {
    val snackBar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG)
    val customView = LayoutInflater.from(this).inflate(R.layout.custom_snackbar_error, null)
    customView.findViewById<TextView>(R.id.textView1).text = message
    snackBar.view.setBackgroundColor(Color.TRANSPARENT)

    val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
    snackBarLayout.setPadding(0, 0, 0, 0)
    snackBarLayout.addView(customView, 0)
    snackBar.show()
}

@SuppressLint("RestrictedApi", "InflateParams")
fun Fragment.showSnackBarError(message: String) {
    val snackBar = Snackbar.make(requireView(), "", Snackbar.LENGTH_LONG)
    val customView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_snackbar_error, null)
    customView.findViewById<TextView>(R.id.textView1).text = message
    snackBar.view.setBackgroundColor(Color.TRANSPARENT)

    val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
    snackBarLayout.setPadding(0, 0, 0, 0)
    snackBarLayout.addView(customView, 0)
    snackBar.show()
}

@SuppressLint("RestrictedApi", "InflateParams")
fun Activity.showSnackBarSuccess(message: String) {
    val snackBar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG)
    val customView = LayoutInflater.from(this).inflate(R.layout.custom_snackbar_success, null)
    customView.findViewById<TextView>(R.id.textView1).text = message
    snackBar.view.setBackgroundColor(Color.TRANSPARENT)

    val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
    snackBarLayout.setPadding(0, 0, 0, 0)
    snackBarLayout.addView(customView, 0)
    snackBar.show()
}

@SuppressLint("RestrictedApi", "InflateParams")
fun Fragment.showSnackBarSuccess(message: String) {
    val snackBar = Snackbar.make(requireView(), "", Snackbar.LENGTH_LONG)
    val customView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_snackbar_success, null)
    customView.findViewById<TextView>(R.id.textView1).text = message
    snackBar.view.setBackgroundColor(Color.TRANSPARENT)

    val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
    snackBarLayout.setPadding(0, 0, 0, 0)
    snackBarLayout.addView(customView, 0)
    snackBar.show()
}
