package com.nafi.airseat.presentation.blank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nafi.airseat.R

class BlankActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blank)
    }

    companion object {
        const val EXTRAS_ITEM = "EXTRAS_ITEM"

        fun startActivity(
            context: Context,
            id: String,
        ) {
            val intent = Intent(context, BlankActivity::class.java)
            intent.putExtra(EXTRAS_ITEM, id)
            context.startActivity(intent)
        }
    }
}
