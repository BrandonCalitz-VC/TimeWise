package com.timewise.timewise

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class Analytics : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_analytics)

        bottomNavigationView = findViewById(R.id.bottomNavigator)
        bottomNavigationView.selectedItemId = R.id.analytics

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboard -> {
                    startActivity(Intent(applicationContext, Dashboard::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.projects ->{
                    startActivity(Intent(applicationContext, Projects::class.java))
                    overridePendingTransition(0, 0)
                    true;
                }
                R.id.analytics ->{

                    true;

                }

                else -> false
            }
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_analytics)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}