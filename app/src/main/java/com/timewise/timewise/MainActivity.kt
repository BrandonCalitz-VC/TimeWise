package com.timewise.timewise

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.timewise.timewise.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Firebase.auth.currentUser == null) {
            startActivity(Intent(this,Auth::class.java))
            return
        }

        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.bottomNavigator.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboard -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainer.id,DashboardFragment())
                        .addToBackStack(null)
                        .commit()

                    true;
                }
                R.id.projects ->{
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainer.id,ProjectsFragment())
                        .addToBackStack(null)
                        .commit()
                    true;
                }
                R.id.analytics ->{
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainer.id,AnalyticsFragment())
                        .addToBackStack(null)
                        .commit()
                    true;

                }

                else -> false
            }
        }


        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id,DashboardFragment())
            .addToBackStack(null)
            .commit();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }



    }
}