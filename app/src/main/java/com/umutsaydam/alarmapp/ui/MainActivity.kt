package com.umutsaydam.alarmapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavHost
import androidx.navigation.ui.setupWithNavController
import com.umutsaydam.alarmapp.R
import com.umutsaydam.alarmapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHost
        binding.btNavigation.setupWithNavController(navHost.navController)

    }
}