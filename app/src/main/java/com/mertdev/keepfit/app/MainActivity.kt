package com.mertdev.keepfit.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mertdev.keepfit.R
import com.mertdev.keepfit.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNav()
    }

    private fun setupNav() {
        val navController = findNavController(R.id.fragmentContainer)
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.onBoardingFragment -> hideBottomNav()
                R.id.measurementContentFragment -> hideBottomNav()
                R.id.addMeasurementContentDialogFragment -> hideBottomNav()
                R.id.allMeasurementContentDialogFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE
    }

}