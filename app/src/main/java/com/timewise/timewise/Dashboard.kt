package com.timewise.timewise
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Dashboard : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        bottomNavigationView = findViewById(R.id.bottomNavigator)
        bottomNavigationView.selectedItemId = R.id.dashboard

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboard -> {
                    true
                }
                R.id.projects ->{
                    startActivity(Intent(applicationContext, Projects::class.java))
                    overridePendingTransition(0, 0)
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
        auth = Firebase.auth
        val u = auth.currentUser?.email
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
}