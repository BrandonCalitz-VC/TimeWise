package com.timewise.timewise

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class Projects : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_projects)

        bottomNavigationView = findViewById(R.id.bottomNavigator)
        bottomNavigationView.selectedItemId = R.id.projects

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboard -> {
                    startActivity(Intent(applicationContext, Dashboard::class.java))
                    overridePendingTransition(0, 0)
                    true;
                }
                R.id.projects ->{
                    true;
                }
                R.id.analytics ->{
                    startActivity(Intent(applicationContext, Analytics::class.java))
                    overridePendingTransition(0, 0)
                    true;

                }

                else -> false
            }
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_projects)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}