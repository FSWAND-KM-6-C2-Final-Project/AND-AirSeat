package com.c2.airseat.presentation.appintro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.c2.airseat.R
import com.c2.airseat.presentation.splashscreen.SplashScreenActivity
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroCustomLayoutFragment
import com.github.appintro.AppIntroPageTransformerType
import org.koin.androidx.viewmodel.ext.android.viewModel

class AppIntroActivity : AppIntro2() {
    private val appIntroViewModel: AppIntroViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(
            AppIntroCustomLayoutFragment.newInstance(R.layout.layout_intro_started),
        )
        setTransformer(
            AppIntroPageTransformerType.Parallax(
                titleParallaxFactor = 1.0,
                imageParallaxFactor = -1.0,
                descriptionParallaxFactor = 2.0,
            ),
        )

        isIndicatorEnabled = true

        setIndicatorColor(
            selectedIndicatorColor = getColor(R.color.md_theme_primary),
            unselectedIndicatorColor = getColor(R.color.md_theme_onError),
        )
        if (appIntroViewModel.isAppIntroShown()) {
            navigateToSplash()
        }
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        navigateToSplash()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        navigateToSplash()
    }

    private fun navigateToSplash() {
        val intent = Intent(this, SplashScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}
